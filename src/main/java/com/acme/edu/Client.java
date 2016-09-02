package com.acme.edu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * main client class
 */
public class Client {
    private static final String ERROR_CAN_T_CONNECT_TO_SERVER = "[ERROR] Can't connect to server" + System.lineSeparator() + "Press Enter to exit.";
    private ClientSession clientSession;
    private volatile boolean closed = false;

    /**
     * constructor for testing
     * @param port
     * @param serverName
     */
    public Client(int port, String serverName) throws IOException {
        clientSession = new ClientSession(port, serverName);
        clientSession.createSession();
    }

    /**
     * main constructor
     * @param clientSession 
     */
    public Client(ClientSession clientSession) throws IOException {
        this.clientSession = clientSession;
        clientSession.createSession();
    }

    private static void printErrorMessageToConsole(String message) {
        System.err.println(message);
    }

    private void process() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ExecutorService pool = Executors.newFixedThreadPool(2);

        pool.execute(() -> receiveRunningThread(pool));
        pool.execute(() -> sendRunningThread(reader, pool));
    }

    void sendRunningThread(BufferedReader reader, ExecutorService pool) {
        try {
            while(!this.isClosed()) {
                if (processAndSendInputString(reader))
                    return;
            }
        } catch (ExitClientException e) {
            this.close(true);
            pool.shutdownNow();
        }
    }

    void receiveRunningThread(ExecutorService pool) {
        try {
            while (!this.isClosed()) {
                String message;
                if ((message = this.receive()) != null) {
                    System.out.println(this.receive());
                } else {
                    throw new SocketException("Can't connect to server");
                }
            }
        } catch (SocketException e) {
            if(!"Socket closed".equals(e.getMessage())) {
                printErrorMessageToConsole(ERROR_CAN_T_CONNECT_TO_SERVER);
                this.close(false);
                pool.shutdownNow();
            }
        } catch (IOException e) {
            printErrorMessageToConsole(ERROR_CAN_T_CONNECT_TO_SERVER);
            this.close(false);
            pool.shutdownNow();
        }
    }

    private boolean processAndSendInputString(BufferedReader reader) throws ExitClientException {
        try {
            this.send(reader.readLine());
        } catch (IOException e) {
            printErrorMessageToConsole(ERROR_CAN_T_CONNECT_TO_SERVER);
            return true;
        }
        return false;
    }

    /**
     * @param message message to send
     */
    int send(String message) throws ExitClientException {
        if (closed)
            return -5;
        Pattern p = Pattern.compile("^/(\\w+)(.*)$");
        Matcher m = p.matcher(message);
        if(m.matches()) {
            Integer x = processInputLine(m);
            if (x != null)
                return x;
        }
        else {
            printErrorMessageToConsole("[WRONG INPUT] Your command contains a mistake." + System.lineSeparator() +
                    "[WRONG INPUT] Your message should be separated from command with space.");
            return -10;
        }
        return 0;
    }

    private Integer processInputLine(Matcher m) throws ExitClientException {
        switch (m.group(1)) {
            case "snd":
                String text = m.group(2);
                Integer x = processSendCommand(text);
                if (x != null)
                    return x;
                break;
            case "exit":
                throw new ExitClientException();
            default:
                printErrorMessageToConsole("[WRONG COMMAND] Inapplicable command.");
                return -1;
        }
        return null;
    }

    private Integer processSendCommand(String text) {
        if(text.length() < 2) {
            printErrorMessageToConsole("[EMPTY MESSAGE] Provide at least 1 character.");
            return -2;
        } else if (text.length() > 151) {
            printErrorMessageToConsole("[TOO LONG MESSAGE] Max length is 150 characters.");
            return -3;
        }
        String textWithoutFirstSpace = text.substring(1);
        try {
            clientSession.sendMessage(textWithoutFirstSpace);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    String receive() throws IOException {
        if(!isClosed()) {
            return clientSession.receiveMessage();
        }
        return "";
    }

    private synchronized void close(boolean serverAvailable) {
        closed = true;
        if(serverAvailable) {
            clientSession.closeSession();
        }
    }

    boolean isClosed() {
        return closed;
    }

    /**
     * main single client method
     * @param args
     */
    public static void main(String[] args) {
        Client client;
        try {
            client = new Client(new ClientSession(1111, "localhost"));
            client.process();
        } catch (IOException e) {
            System.err.println("Can't connect to server. Press Enter to exit...");
            new java.util.Scanner(System.in).nextLine();
        }
    }
}


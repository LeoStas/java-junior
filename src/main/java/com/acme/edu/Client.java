package com.acme.edu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Client {
    private static final String ERROR_CAN_T_CONNECT_TO_SERVER = "[ERROR] Can't connect to server" + System.lineSeparator() + "Press Enter to exit.";
    private ClientSession clientSession;
    private volatile boolean closed = false;

    public Client(int port, String serverName) {
        clientSession = new ClientSession(port, serverName);
        clientSession.createSession();
    }

    public Client(int port, String serverName, ClientSession cs) {
        clientSession = cs;
        clientSession.createSession();
    }

    public static void printErrorMessageToConsole(String message) {
        System.err.println(message);
    }

    /**
     * @param message message to send
     */
    public int send(String message) throws ExitClientException {
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

    private String receive() throws IOException {
        if(!closed) {
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

    private boolean isClosed() {
        return closed;
    }

    public void process() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        ExecutorService pool = Executors.newFixedThreadPool(2);

        pool.execute(() -> {
            try {
                while (!this.isClosed()) {
                    System.out.println(this.receive());
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

        });

        pool.execute(() -> {
            try {
                while(!this.isClosed()) {
                    if (processAndSendInputString(reader)) return;
                }
            } catch (ExitClientException e) {
                this.close(true);
                pool.shutdownNow();
            }
        });
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

    public static void main(String[] args) {
        Client client = new Client(1111, "localhost", new ClientSession(1111, "localhost"));
        client.process();
    }
}


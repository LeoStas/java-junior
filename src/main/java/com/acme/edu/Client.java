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
    private static final String ERROR_CAN_T_CONNECT_TO_SERVER = "[ERROR] Can't connect to server! Press Enter to exit...";
    private ClientSession clientSession;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;
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
        System.err.println("\r"+message);
    }

    private void process() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        if(identifyUser(reader) == 0) {
            ExecutorService pool = Executors.newFixedThreadPool(2);

            pool.execute(() -> receiveRunningThread(pool));
            pool.execute(() -> sendRunningThread(reader, pool));
        }
    }

    private int identifyUser(BufferedReader reader) {
        try {
            while(true) {
                System.out.print("Enter your name: ");
                String name = reader.readLine();
                name = name.trim();
                if((name.length() > 0) && (name.length() < 20)) {
                    clientSession.sendMessage(name);
                    String userOk = receive();
                    if ("true".equals(userOk)) {
                        System.out.println("\rSuccessfully connected!");
                        userName = name;
                        return 0;
                    } else {
                        System.out.println("\rTry again!");
                    }
                } else {
                    System.err.println("Name should be max 20 characters long!!");
                }
            }
        } catch (SocketException e) {
            if(!"Socket closed".equals(e.getMessage())) {
                printErrorMessageToConsole(ERROR_CAN_T_CONNECT_TO_SERVER);
                this.close(false);
                new java.util.Scanner(System.in).nextLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
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
                    System.out.println(message);
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
                text = text.trim();
                if(text.length() < 1) {
                    printErrorMessageToConsole("[EMPTY MESSAGE] Provide at least 1 character.");
                    return -2;
                } else if (text.length() > 150) {
                    printErrorMessageToConsole("[TOO LONG MESSAGE] Max length is 150 characters.");
                    return -3;
                }
                text = "/snd " + userName + " -> " + text;
                sendCommand(text);
                break;
            case "hist":
                sendCommand("/hist");
                break;
            case "continue":
                sendCommand("/continue");
                break;
            case "stop":
                sendCommand("/stop");
                break;
            case "exit":
                sendCommand("/exit");
                throw new ExitClientException();
            default:
                printErrorMessageToConsole("[WRONG COMMAND] Inapplicable command.");
                return -1;
        }
        return null;
    }

    private void sendCommand(String text) {
        try {
            clientSession.sendMessage(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            client = new Client(new ClientSession(49003, "localhost"));
            client.process();
        } catch (IOException e) {
            System.err.println("Can't connect to server. Press Enter to exit...");
            new java.util.Scanner(System.in).nextLine();
        }
    }
}


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

    public static void PrintErrorMessageToConsole(String message) {
        System.out.println(TextColor.ANSI_RED + message + TextColor.ANSI_RESET);
    }

    /**
     * @param message message to send
     */
    public int send(String message) throws ExitClientException {
        if (closed) return -5;
        Pattern p = Pattern.compile("^/(\\w+)(.*)$");
        Matcher m = p.matcher(message);
        if(m.matches()) {
            switch (m.group(1)) {
                case "snd":
                    String text = m.group(2);
                    if(text.length() < 2) {
                        PrintErrorMessageToConsole("[EMPTY MESSAGE] Provide at least 1 character.");
                        return -2;
                    } else if (text.length() > 151) {
                        PrintErrorMessageToConsole("[TOO LONG MESSAGE] Max length is 150 characters.");
                        return -3;
                    }
                    text = text.substring(1);
                    try {
                        clientSession.sendMessage(text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "exit":
                    throw new ExitClientException();
                default:
                    PrintErrorMessageToConsole("[WRONG COMMAND] Inapplicable command.");
                    return -1;
            }
        }
        else {
            PrintErrorMessageToConsole("[WRONG INPUT] Your command contains a mistake." + System.lineSeparator() +
                    "[WRONG INPUT] Your message should be separated from command with space.");
            return -10;
        }
        return 0;
    }

    public String receive() throws IOException {
        if(!closed) {
            return clientSession.receiveMessage();
        }
        return "";
    }

    public synchronized void close(boolean serverAvailable) {
        closed = true;
        if(serverAvailable) {
            clientSession.closeSession();
        }
    }

    public boolean isClosed() {
        return closed;
    }

    public static void main(String[] args) {
        Client client = new Client(1111, "localhost", new ClientSession(1111, "localhost"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        ExecutorService pool = Executors.newCachedThreadPool();

        pool.execute(() -> {
            while (!client.isClosed()) {
                try {
                    System.out.println(client.receive());
                } catch (SocketException e) {
                    if(!e.getMessage().equals("Socket closed")) {
                        PrintErrorMessageToConsole("[ERROR] Can't connect to server");
                        client.close(false);
                        return;
                    }
                } catch (IOException e) {
                    PrintErrorMessageToConsole("[ERROR] Can't connect to server");
                    client.close(false);
                    pool.shutdownNow();
                    return;
                }
            }
        });

        pool.execute(() -> {
            try {
                while(!client.isClosed()) {
                    try {
                        client.send(reader.readLine());
                    } catch (IOException e) {
                        PrintErrorMessageToConsole("[ERROR] Can't connect to server");
                        return;
                    }
                }
            } catch (ExitClientException e) {
                client.close(true);
                pool.shutdownNow();
            }
        });
    }
}


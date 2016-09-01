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

    /**
     * @param message message to send
     */
    public void send(String message) throws ExitClientException {
        Pattern p = Pattern.compile("^/(\\w+)(.*)$");
        Matcher m = p.matcher(message);
        if(m.matches()) {
            switch (m.group(1)) {
                case "snd":
                    String text = m.group(2);
                    if(text.length() < 2) {
                        System.out.println("[EMPTY MESSAGE] Provide at least 1 character.");
                        return;
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
                    System.out.println("[WRONG COMMAND] Inapplicable command.");
            }
        }
        else {
            System.out.println("[WRONG INPUT] Your command contains a mistake." + System.lineSeparator() +
                    "[WRONG INPUT] Your message should be separated from command with space.");
        }
    }

    public String receive() throws IOException {
        if(!closed) {
            return clientSession.receiveMessage();
        }
        return "";
    }

    public synchronized void close() {
        closed = true;
        clientSession.closeSession();
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
                        System.out.println("[ERROR] Can't connect to server");
                        break;
                    }
                } catch (IOException e) {
                    System.out.println("[ERROR] Can't connect to server");
                    break;
                }
            }
        });

        pool.execute(() -> {
            try {
                while(true) {
                    try {
                        client.send(reader.readLine());
                    } catch (IOException e) {
                        System.out.println("[ERROR] Can't connect to server");
                        break;
                    }
                }
            } catch (ExitClientException e) {
                client.close();
                pool.shutdownNow();
            }
        });
    }
}


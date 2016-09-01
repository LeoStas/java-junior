package com.acme.edu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public String receive() {
        try {
            return clientSession.receiveMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void close() {
        clientSession.closeSession();
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    public static void main(String[] args) {
        Client client = new Client(1111, "localhost");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        ExecutorService pool = Executors.newCachedThreadPool();

        pool.execute(() -> {
            while(!client.isClosed()) {
                client.receive();
            }
        });
        pool.execute(() -> {
            try {
                while(true) {
                    try {
                        client.send(reader.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (ExitClientException e) {
                client.close();
                pool.shutdown();
            }
        });
    }
}


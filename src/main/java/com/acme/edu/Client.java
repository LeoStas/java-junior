package com.acme.edu;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ExitClientException extends Exception {
    public ExitClientException() {
        super();
    }
}

public class Client {
    private ClientSession clientSession;

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

    public void receive() {
        try {
            clientSession.receiveMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        clientSession.closeSession();
    }

    public static void main(String[] args) {
        Client client = new Client(1111, "localhost");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        ExecutorService pool = Executors.newCachedThreadPool();

        pool.execute(() -> {
            while(true) {
                client.receive();
            }
        });

        pool.execute(() -> {
            while(true) {
                try {
                    client.send(reader.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ExitClientException e) {
                    client.close();
                }
            }
        });

        client.close();
    }
}


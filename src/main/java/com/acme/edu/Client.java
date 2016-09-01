package com.acme.edu;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private ClientSession clientSession;

    public Client(int port, String serverName) {
        clientSession = new ClientSession(port, serverName);
        clientSession.createSession();
    }

    /**
     * @param message message to send
     */
    public void send(String message) {
        try {
            clientSession.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
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
//        pool.execute(() -> {
//            while(true) {
//                client.receive();
//            }
//        });

        pool.execute(() -> {
            while(true) {
                try {
                    String s = reader.readLine();
                    client.send(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        client.close();
    }
}


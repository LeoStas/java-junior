package com.acme.edu;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.application.Platform.exit;

class Server {
    private final Collection<SessionHandler> sessionHandlerList =
            Collections.synchronizedList(new LinkedList<SessionHandler>());
    private final Collection<String> users = Collections.synchronizedSet(new HashSet<String>());
    private ServerSocket serverSocket;
    private volatile boolean running = true;

    Server() throws IOException {
        serverSocket = new ServerSocket(49003);
    }

    /**
     * Start server.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        try {
            new Server().runServer();
        } catch (NullPointerException | IOException e) {
            System.err.println("/Cannot start service");
            System.err.println("/Exit");
            exit();
        }
    }

    void runServer() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.execute(() -> {
            try {
                String s = reader.readLine();
                Pattern p = Pattern.compile("^/(\\w+)(.*)$");
                Matcher m = p.matcher(s);
                if(m.matches() && "exit".equals(m.group(1))) {
                    running = false;
                    Server.this.shutdownServer();
                    pool.shutdown();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        while (running) {
            try {
                Socket client = serverSocket.accept();
                SessionHandler sessionHandler = new SessionHandler(client, sessionHandlerList, users);
                sessionHandlerList.add(sessionHandler);
                sessionHandler.start();
            } catch (IOException ignored) {}
        }
    }

    synchronized void shutdownServer() {
        sessionHandlerList.forEach(Thread::interrupt);

        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("/Cannot finish server");
        }
    }
}



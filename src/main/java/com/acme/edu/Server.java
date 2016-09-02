package com.acme.edu;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Server {
    private final Set<SessionHandler> sessionHandlerSet =
            Collections.synchronizedSet(new HashSet<SessionHandler>());
    private ServerSocket serverSocket;
    private volatile boolean running = true;

    private Server() {
        try {
            serverSocket = new ServerSocket(1111);
        } catch (IOException e) {
            System.err.println("/Cannot start service");
        }
    }

    /**
     * Start server.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new Server().runServer();
    }

    private void runServer() {
        final int maxClientNumber = 10000;
        int curClientNumber = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.execute(() -> {
            try {
                String s = reader.readLine();
                Pattern p = Pattern.compile("^/(\\w+)(.*)$");
                Matcher m = p.matcher(s);
                if(m.matches()) {
                    if("exit".equals(m.group(1))) {
                        running = false;
                        Server.this.shutdownServer();
                        pool.shutdown();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        while (running && (curClientNumber < maxClientNumber)) {
            curClientNumber++;
            try {
                Socket client = serverSocket.accept();
                SessionHandler sessionHandler = new SessionHandler(client);
                sessionHandlerSet.add(sessionHandler);
                sessionHandler.start();
            } catch (IOException e) {
                System.err.println("/Cannot accept client");
            }
        }
    }

    private synchronized void shutdownServer() {
        sessionHandlerSet.forEach(SessionHandler::close);
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("/Cannot finish server");
        }
    }

    private class SessionHandler extends Thread {
        private final Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        SessionHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(
                        new InputStreamReader(
                                new BufferedInputStream(
                                        socket.getInputStream()
                                ), StandardCharsets.UTF_8
                        )
                );
                out = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        new BufferedOutputStream(
                                                socket.getOutputStream()
                                        ), StandardCharsets.UTF_8
                                )
                        )
                );
            } catch (IOException e) {
                System.err.println("/Cannot open client socket");
            }
        }

        @Override
        public void run() {
            String msg;
            try {
                while (true) {
                    msg = in.readLine();
                    if (msg == null) {
                        break;
                    }
                    send(msg);
                }
            } catch (IOException ignored) {}

            close();
        }

        private synchronized void send(String msg) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss dd.MM.yyyy] ");
            String sndMsg = dateFormat.format(new Date()) + msg;
            for (SessionHandler sessionHandler:sessionHandlerSet) {
                sessionHandler.out.println(sndMsg);
                sessionHandler.out.flush();
            }
            System.out.println(sndMsg);
        }

        private void close() {
            System.err.println("/Close connection");
            sessionHandlerSet.remove(this);
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("/Cannot close client connection");
            }
        }
    }
}



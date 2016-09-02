package com.acme.edu;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

class Server {
    private Set<SessionHandler> sessionHandlerList =
            Collections.synchronizedSet(new HashSet<SessionHandler>());

    private Server() {
        final int maxClientNumber = 10000;
        int curClientNumber = 0;
        try (
                ServerSocket serverSocket = new ServerSocket(1111))
        {
            while (curClientNumber < maxClientNumber) {
                curClientNumber++;
                Socket client = serverSocket.accept();
                SessionHandler sessionHandler = new SessionHandler(client);
                sessionHandlerList.add(sessionHandler);
                sessionHandler.start();
            }
        } catch (IOException e) {
            shutdownServer();
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }

    private synchronized void shutdownServer() {
        sessionHandlerList.forEach(SessionHandler::close);
    }

    private class SessionHandler extends Thread {
        private Socket socket;
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
                e.printStackTrace();
            }
        }

        public void run() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss dd.MM.yyyy] ");
            while (true) {
                try {
                    String msg = in.readLine();
                    if (msg == null) {
                        break;
                    }
                    send(dateFormat.format(new Date()) + msg);
                    System.out.println(msg);
                } catch (IOException e) {
                    break;
                }
            }

            close();
        }

        private synchronized void send(String msg) {
            for (SessionHandler sessionHandler:sessionHandlerList) {
                sessionHandler.out.println(msg);
                sessionHandler.out.flush();
            }
        }

        private void close() {
            System.out.println("/Close connection");
            sessionHandlerList.remove(this);
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



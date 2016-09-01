package com.acme.edu;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Server {
    public static List<SessionHandler> sessionHandlerList =
            Collections.synchronizedList(new ArrayList<SessionHandler>());

    public static void main(String[] args) {
        Server server = new Server();
    }

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
            e.printStackTrace();
        }
    }

    private class SessionHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public SessionHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new BufferedReader(
                        new InputStreamReader(
                                new BufferedInputStream(
                                        socket.getInputStream()
                                )
                        )
                );
                out = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        new BufferedOutputStream(
                                                socket.getOutputStream()
                                        )
                                )
                        )
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (true) {
                try {
                    String rcv_msg = in.readLine();
                    if (rcv_msg == null) break;

                    String msg = rcv_msg + " [" + LocalDateTime.now() + "]";
                    System.out.println(msg);
                    for (SessionHandler sessionHandler:sessionHandlerList) {
                        sessionHandler.send(msg);
                    }
                } catch (IOException e) {
                    break;
                }
            }

            close();
        }

        public synchronized void send(String msg) {
            out.println(msg);
            out.flush();
        }

        void close() {
            System.out.println("Close connection");
            System.out.println();
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



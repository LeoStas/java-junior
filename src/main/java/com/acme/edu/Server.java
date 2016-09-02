package com.acme.edu;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class Server {
    public static Set<SessionHandler> sessionHandlerList =
            Collections.synchronizedSet(new HashSet<SessionHandler>());

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
            shutdownServer();
            e.printStackTrace();
        }
    }

    private synchronized void shutdownServer() {
        for (SessionHandler sessionHandler: sessionHandlerList) {
            sessionHandler.close();
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
                                ),
                                StandardCharsets.UTF_8
                        )
                );
                out = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        new BufferedOutputStream(
                                                socket.getOutputStream()
                                        ),
                                        StandardCharsets.UTF_8
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
                    String rcv_msg = in.readLine();
                    if (rcv_msg == null) break;

                    String msg = "";
                    for (SessionHandler sessionHandler:sessionHandlerList) {
                        if (sessionHandler == this) {
                            //msg = TextColor.ANSI_GREEN + dateFormat.format(new Date()) + "<- " + rcv_msg + TextColor.ANSI_RESET;
                            msg = dateFormat.format(new Date()) + "<- " + rcv_msg;
                        } else {
                            //msg = TextColor.ANSI_CYAN + dateFormat.format(new Date()) + "-> " + rcv_msg + TextColor.ANSI_RESET;
                            msg = dateFormat.format(new Date()) + "-> " + rcv_msg;
                        }
                        sessionHandler.send(msg);
                    }
                    System.out.println(msg);
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



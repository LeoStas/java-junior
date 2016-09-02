package com.acme.edu;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

class SessionHandler extends Thread {
    private final Set<SessionHandler> sessionHandlerSet;
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    SessionHandler(Socket socket, Set<SessionHandler> sessionHandlerSet) {
        this.sessionHandlerSet = sessionHandlerSet;
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
            while (!this.isInterrupted()) {
                if (!in.ready()) {
                    sleep(50);
                } else {
                    msg = in.readLine();
                    if (msg == null) {
                        break;
                    }
                    send(msg);
                }
            }
        } catch (IOException | InterruptedException ignored) {}

        close();
        sessionHandlerSet.remove(this);
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
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("/Cannot close client connection");
        }
    }
}
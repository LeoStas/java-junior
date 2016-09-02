package com.acme.edu;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

class SessionHandler extends Thread {
    private final Collection<SessionHandler> sessionHandlerList;
    private final Collection<String> users;
    private final Collection<String> history;
    private final Socket socket;
    private final int histMsgNum = 5;
    private String user = "anonymous";
    private int lastNum = 0;
    private PrintWriter out;
    private BufferedReader in;


    SessionHandler(Socket socket, Collection<SessionHandler> sessionHandlerList, Collection<String> users,
                   Collection<String> history) {
        this.sessionHandlerList = sessionHandlerList;
        this.users = users;
        this.history = history;
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
        boolean hasName = false;
        try {
            while (!this.isInterrupted()) {
                if (!in.ready()) {
                    sleep(50);
                } else {
                    msg = in.readLine();
                    if (msg == null) {
                        break;
                    }
                    if (hasName) {
                        char com = msg.charAt(1);
                        if (com == 'e') {
                            break;
                        } else if (com == 's') {
                            send(msg.substring(5));
                        } else if (com == 'h'){
                            ;
                        }
                    } else {
                        if (users.add(msg)) {
                            out.println("true");
                            user = msg;
                            System.err.println("/New user: " + user);
                            hasName = true;
                        } else {
                            out.println("false");
                            System.err.println("/Duplicate user: " + msg);
                        }
                        out.flush();
                    }
                }
            }
        } catch (IOException | InterruptedException ignored) {}

        close();

        sessionHandlerList.remove(this);
        users.remove(user);
    }

    private synchronized void send(String msg) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss dd.MM.yyyy] ");
        String sndMsg = dateFormat.format(new Date()) + msg;
        for (SessionHandler sessionHandler: sessionHandlerList) {
            sessionHandler.out.println(sndMsg);
            sessionHandler.out.flush();
        }
        System.out.println(sndMsg);
    }

    private void close() {
        System.err.println("/Exit user: " + user);
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("/Cannot close client connection");
        }
    }
}
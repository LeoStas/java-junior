package com.acme.edu;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * Implement all connections with the concrete client.
 */
class SessionHandler extends Thread {
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
                out.println(msg);
                out.flush();
            } catch (IOException e) {
                break;
            }
        }

        close();
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

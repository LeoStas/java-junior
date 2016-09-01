package com.acme.edu;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

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
                String msg = in.readLine() + " [" + LocalDateTime.now() + "]";
                System.out.println(msg);
                out.println(msg);
                out.flush();
            } catch (IOException e) {
                //System.out.println("Close connection");
                //System.out.println();
                break;
            }
        }

        close();
    }

    void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

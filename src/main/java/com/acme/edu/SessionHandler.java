package com.acme.edu;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * Implement all connections with the concrete client.
 */
class SessionHandler extends Thread {
    private Socket socket;

    public SessionHandler(Socket socket) {
        this.socket = socket;
        try (
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new BufferedInputStream(
                                    socket.getInputStream()
                            )
                    )
                );
                PrintWriter out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new BufferedOutputStream(
                                            socket.getOutputStream()
                                    )
                            )
                    )
                )
        ) {
            while (true) {
                try {
                    String msg = in.readLine() + " [" + LocalDateTime.now() + "]";
                    System.out.println(msg);
                    out.println(msg);
                } catch (IOException e) {
                    System.out.println("Close connection");
                    System.out.println();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.acme.edu;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Implement all connections with the concrete client.
 */
class SessionHandler extends Thread {
    private BufferedReader in;
    private PrintWriter  out;
    private Socket socket;

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


        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

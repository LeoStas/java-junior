package com.acme.edu;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Implement all connections with the concrete client.
 */
public class SessionHandler {
    public SessionHandler(Socket client) {
        try (
                ObjectInputStream in = new ObjectInputStream(
                        new BufferedInputStream(
                                client.getInputStream()
                        )
                );
                ObjectOutputStream out = new ObjectOutputStream(
                        new BufferedOutputStream(
                                client.getOutputStream()
                        )
                )
        ) {
            while (true) {
                try {
                    Message msg = (Message) in.readObject();
                    msg.setDate(LocalDateTime.now());
                    System.out.println(msg.getData() + " [" + msg.getDate() + "]");
                    out.writeObject(msg);
                } catch (IOException e) {
                    System.out.println("Close connection");
                    System.out.println();
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

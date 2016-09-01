package com.acme.edu;

import java.io.IOException;

/**
 * Created by Java_12 on 31.08.2016.
 */
public class Client {
    private ClientSession clientSession;

    public Client(int port, String serverName) {
        clientSession = new ClientSession(port, serverName);
    }

    /**
     * @param message message to send
     */
    public void send(Message message) throws SenderException {
        try {
            clientSession.createSession();
            clientSession.sendMessage(message);
            clientSession.closeSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client(1111, "localhost");
        try {
            client.send(new Message("bla"));

        } catch (SenderException e) {
            e.printStackTrace();
        }
    }
}


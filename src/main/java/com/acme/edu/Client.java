package com.acme.edu;

import java.io.IOException;

/**
 * Created by Java_12 on 31.08.2016.
 */
public class Client {
    private ClientSession clientSession;

    public Client(int port, String serverName) {
        clientSession = new ClientSession(port, serverName);
        clientSession.createSession();
    }

    /**
     * @param message message to send
     */
    public void send(Message message) throws SenderException {
        try {
            clientSession.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Message receive() throws SenderException {
        Message res = null;
        try {
            res = clientSession.receiveMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void close() {
        clientSession.closeSession();
    }

    public static void main(String[] args) {
        Client client = new Client(1111, "localhost");
        try {
            client.send(new Message("bla"));
            Message mess = client.receive();
            System.out.println(mess.getData() + mess.getDate());
            client.close();

        } catch (SenderException e) {
            e.printStackTrace();
        }
    }
}


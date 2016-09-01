package com.acme.edu;

import java.io.IOException;

/**
 * Work with network output
 */
public class ClientSession {
    private Connector connector;

    public ClientSession(int port, String serverName) {
        this.connector = new Connector(port, serverName);
    }

    public void createSession() {
        connector.connect();
    }
    public void sendMessage(Message message) throws IOException {
        try {
            connector.getOutput().writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Can not send the message!", e);
        }
    }


    public Message receiveMessage(Message message) throws IOException {
        Message result = null;
        try {
            result = (Message)connector.getInput().readObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Can not receive the message!", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void closeSession() {
        connector.disconnect();
    }
}

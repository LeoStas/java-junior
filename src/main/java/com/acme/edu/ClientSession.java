package com.acme.edu;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

/**
 * Work with network output
 */
public class ClientSession {
    private Connector connector;
    private ObjectOutputStream out;

    public ClientSession(int port, String serverName) {
        this.connector = new Connector(port, serverName);
    }

    public void createSession() throws IOException {
        try {
            connector.connect();
            out = connector.getOutput();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Can not create the session!", e);
        }
    }
    public void sendMessage(Message message) throws IOException {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Can not send the message!", e);
        }
    }

    public void recieveMessage(Message message) throws IOException {
    }

    public void closeSession() {
        connector.disconnect();
    }
}

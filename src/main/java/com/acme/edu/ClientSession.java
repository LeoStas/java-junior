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
    public void sendMessage(String message) throws IOException {
        try {
            connector.getOutput().println(message);
            connector.getOutput().flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Can not send the message!", e);
        }
    }


    public String receiveMessage() throws IOException {
        try {
            //while (!connector.getInput().ready()) {}
            return connector.getInput().readLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Can not receive the message!", e);
        }
    }

    public void closeSession() {
        connector.disconnect();
    }
}

package com.acme.edu;

import java.io.IOException;

/**
 * Work with network output
 */
public class ClientSession {
    private Connector connector;
    private final Object monitor1 = new Object();

    public ClientSession(int port, String serverName) {
        this.connector = new Connector(port, serverName);
    }

    public void createSession() {
        connector.connect();
    }
    public void sendMessage(String message) throws IOException {
        connector.getOutput().println(message);
        connector.getOutput().flush();

    }


    public String receiveMessage() throws IOException {
        String s;
        synchronized (monitor1) {
            return connector.getInput().readLine();
        }
    }

    public void closeSession() {
        connector.disconnect();
    }
}

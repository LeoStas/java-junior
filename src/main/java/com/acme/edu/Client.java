package com.acme.edu;

/**
 */
public class Client {
    private Connector connector;

    public Client(int port, String serverName) {
        this.connector = new Connector(port, serverName);
    }
    public void sendMessage(String message) {

    }
}

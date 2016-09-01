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
        connector.connect();
    }
    public void sendMessage(String message) throws IOException {

    }
}

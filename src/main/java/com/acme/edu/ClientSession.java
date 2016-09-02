package com.acme.edu;

import java.io.IOException;

/**
 * Work with network output
 */
class ClientSession {
    private Connector connector;
    private final Object monitor1 = new Object();

    /**
     * Constructor
     * @param port port
     * @param serverName asdf
     */
    ClientSession(int port, String serverName) {
        this.connector = new Connector(port, serverName);
    }

    /**
     * connects to all streams
     */
    void createSession() {
        connector.connect();
    }

    /**
     * Send string message through TCP.
     * method getOutput().println() synchronized inside
     * @param message 1
     * @throws IOException can't read
     */
    void sendMessage(String message) throws IOException {
        connector.getOutput().println(message);
        connector.getOutput().flush();

    }

    /**
     * receiving strng message to be printed.
     * Only one thread should read at same time. So it synchronized inside.
     * @return string to be printed
     * @throws IOException can't connect
     */
    String receiveMessage() throws IOException {
//        synchronized (monitor1) {
            return connector.getInput().readLine();
//        }
    }

    /**
     * disconnect from every stream and socket
     */
    void closeSession() {
        connector.disconnect();
    }
}

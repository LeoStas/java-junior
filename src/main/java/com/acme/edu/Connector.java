package com.acme.edu;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Connects to remote server
 */
public class Connector {
    int port;
    String serverName;

    public OutputStreamWriter getOutput() throws IOException {
        if (!isConnected()) {
            throw new IOException("Connection is not established!");
        }
        return out;
    }

    OutputStreamWriter out;

    /**
     *
     * @param port port on server
     * @param serverName server name for send
     */
    public Connector(int port, String serverName) {
        this.port = port;
        this.serverName = serverName;
    }

    public Connector(int port, String serverName, OutputStreamWriter out) {
        this.port = port;
        this.serverName = serverName;
        this.out = out;
    }

    public boolean isConnected() {
        return out != null;
    }

    public void connect() {
        if (isConnected()) {
            return;
        }
        try {
                out = new OutputStreamWriter(new DataOutputStream(
                            new BufferedOutputStream(
                                    new Socket(serverName, port).getOutputStream()
                            )
                    )
                );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void disconnect() {
        if (!isConnected()) {
            return;
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

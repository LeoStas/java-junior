package com.acme.edu;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Connects to remote server
 */

public class Connector {
    int port;
    String serverName;
    ObjectOutputStream out;
    ObjectInputStream in;
    Socket socket;

    /**
     *
     * @param port port on server
     * @param serverName server name for connect
     */
    public Connector(int port, String serverName) {
        this.port = port;
        this.serverName = serverName;
    }


    /**
     *
     * @param port port on server
     * @param serverName server name
     * @param out stream to write
     */
    public Connector(int port, String serverName, ObjectOutputStream out) {
        this.port = port;
        this.serverName = serverName;
        this.out = out;
    }

    /**
     *
     * @return Network output stream if connection is established
     * @throws IOException if connection wasn't established
     */
    public ObjectOutputStream getOutput() throws IOException {
        if (out == null) {
            out = new ObjectOutputStream(
                    new BufferedOutputStream(
                            socket.getOutputStream()
                    )
            );
        }
        return out;
    }

    /**
     *
     * @return true if connection is established and stream created
     */
    public boolean isConnected() {
        return out != null || in != null;
    }

    /**
     * connects to server and sets output stream
     */
    public void connect() {
        if (isConnected()) {
            return;
        }
        try {
            socket = new Socket(serverName, port);
            out = new ObjectOutputStream(
                    new BufferedOutputStream(
                            socket.getOutputStream()
                    )
            );
            in = new ObjectInputStream(
                    new BufferedInputStream(
                            socket.getInputStream()
                    )
            );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * closes output stream
     */
    public void disconnect() {
        if (!isConnected()) {
            return;
        }
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectInputStream getInput() throws IOException {
        if (in == null) {
            in = new ObjectInputStream(
                    new BufferedInputStream(
                            socket.getInputStream()
                    )
            );
        }
        return in;
    }
}

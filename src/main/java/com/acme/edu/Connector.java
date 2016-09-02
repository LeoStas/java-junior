package com.acme.edu;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Connects to remote server
 */

public class Connector {
    private int port;
    private String serverName;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    /**
     * @param port port on server
     * @param serverName server name for connect
     */
    public Connector(int port, String serverName) {
        this.port = port;
        this.serverName = serverName;
    }

    /**
     *
     * @return Network output stream if connection is established
     * @throws IOException if connection wasn't established
     */
    PrintWriter getOutput() throws IOException {
        if (out == null) {
            out = new PrintWriter(
                    new BufferedWriter(
                        new OutputStreamWriter(
                                new BufferedOutputStream(
                                        socket.getOutputStream()
                                ),
                                StandardCharsets.UTF_8
                        )
                    )
            );
        }
        return out;
    }

    BufferedReader getInput() throws IOException {
        if (in == null) {
            in = new BufferedReader(
                    new InputStreamReader(
                            new BufferedInputStream(
                                    socket.getInputStream()
                            ),
                            StandardCharsets.UTF_8
                    )
            );
        }
        return in;
    }

    /**
     *
     * @return true if connection is established and stream created
     */
    private boolean isConnected() {
        return out != null || in != null;
    }

    /**
     * connects to server and sets output stream
     */
    void connect() {
        if (isConnected()) {
            return;
        }
        try {
            socket = new Socket(serverName, port);
            out = getOutput();
            in = getInput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * closes output stream
     */
    void disconnect() {
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
}

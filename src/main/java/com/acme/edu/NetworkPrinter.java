package com.acme.edu;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Java_12 on 31.08.2016.
 */
public class NetworkPrinter implements Printer {
    Connector connector;

    public NetworkPrinter(int port, String serverName) {
        this.connector = new Connector(port, serverName);
    }

    /**
     * @param msg Message, that we need to print
     */
    @Override
    public void print(String msg) throws PrinterException {
        if (!connector.isConnected()) {
            throw new PrinterException("Connection not established!");
        }
        try {
            connector.getOutput().write(msg);
        } catch (IOException e) {
            e.printStackTrace();
            throw new PrinterException("Connection not established!", e);
        }
    }

    /**
     * Opens printer session
     *
     * @throws PrinterException
     */
    @Override
    public void openPrinter() throws PrinterException {
        connector.connect();
    }

    /**
     * Closes printer session
     *
     * @throws PrinterException
     */
    @Override
    public void closePrinter() throws PrinterException {
        connector.disconnect();
    }
}

class Connector {
    int port;
    String serverName;

    public OutputStreamWriter getOutput() throws IOException {
        if (!isConnected()) {
            throw new IOException("Connection is not established!");
        }
        return out;
    }

    OutputStreamWriter out;

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

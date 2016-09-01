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


package com.acme.edu;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RemotePrinterServer {
    public static void main(String[] args) {
        try (
            ServerSocket serverSocket = new ServerSocket(1111))
            {
                Socket client = serverSocket.accept();
                SessionHandler sessionHandler = new SessionHandler(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



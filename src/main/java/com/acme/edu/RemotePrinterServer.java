package com.acme.edu;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RemotePrinterServer {
    public static void main(String[] args) {
        int maxClientNumber = 10000;
        int curClientNumber = 0;
        try (
            ServerSocket serverSocket = new ServerSocket(1111))
            {
                while (curClientNumber < maxClientNumber) {
                    curClientNumber++;
                    Socket client = serverSocket.accept();
                    SessionHandler sessionHandler = new SessionHandler(client);
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



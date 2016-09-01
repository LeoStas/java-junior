package com.acme.edu;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    private static List<SessionHandler> sessionHandlerList =
            Collections.synchronizedList(new ArrayList<SessionHandler>());

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
                sessionHandlerList.add(sessionHandler);
                //sessionHandler.run();
                sessionHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



package com.acme.edu;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RemotePrinterServer {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(1111);
            Socket client = null;
            client = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(
                            client.getInputStream()
                    )
            ));
            System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



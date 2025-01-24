package com.example.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultThreadServer {
    public static void main(String[] args) {
        try {
            List<Socket > clients = new ArrayList<>();
            ServerSocket serverSocket = new ServerSocket(9999);
            ExecutorService executorService = Executors.newFixedThreadPool(3);
            while (true) {
                Socket socket = serverSocket.accept();
                clients.add(socket);
                 executorService.submit ((new ClientHandler(socket , clients)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

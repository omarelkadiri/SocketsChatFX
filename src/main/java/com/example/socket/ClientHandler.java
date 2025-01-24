package com.example.socket;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket socket;
    private String clientName ;
    private List<Socket> clients ;

    public ClientHandler(Socket socket, List<Socket> clients) {
        this.socket = socket;
        this.clients = clients;
    }



    @Override
    public void run() {
        PrintWriter pw = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(), true);


            pw.println("Veuillez taper votre nom : ");
                clientName = br.readLine();


            while (true){
                String msg = br.readLine();

                clients.forEach(socket -> {
                    if (socket == this.socket)
                        return;
                    try {
                        PrintWriter pw2 = new PrintWriter(socket.getOutputStream(), true);
                        pw2.println(clientName + " : " + msg);

                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                 });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

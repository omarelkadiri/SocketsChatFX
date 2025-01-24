package com.example.socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientConsole {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter pw;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public ClientConsole(String host, int port) {
        try {
            socket = new Socket(host, port);


            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(), true);

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connexion établie avec le serveur.");
        } catch (IOException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }
    }

    public void startChat() {
        try (Scanner scanner = new Scanner(System.in)) {
            // Lire les messages de bienvenue du serveur
            System.out.println(br.readLine());
            System.out.println(br.readLine());

            // Lire et afficher l'objet LongEntry reçu
           // LongEntry logExample = (LongEntry) objectInputStream.readObject();
           // System.out.println("Exemple de log reçu : " + logExample);

            // Lancer un thread pour écouter le serveur
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = br.readLine()) != null) {
                        System.out.println("Serveur : " + serverMessage);
                    }
                } catch (IOException e) {
                    System.err.println("Connexion au serveur perdue.");
                }
            }).start();

            // Lire l'entrée de l'utilisateur et envoyer au serveur
            String message;
            while (true) {
                message = scanner.nextLine();
                pw.println(message);
            }
        } catch (IOException  e) {
            System.err.println("Erreur de communication : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ClientConsole client = new ClientConsole("localhost", 9999);
        client.startChat();
    }
}

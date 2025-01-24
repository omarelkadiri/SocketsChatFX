package com.example.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerConsole {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader br;
    private PrintWriter pw;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public ServerConsole(int port) {
        try {
            System.out.println("Serveur en attente d'un client...");
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            System.out.println("Client connecté !");

            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            pw = new PrintWriter(clientSocket.getOutputStream(), true);

            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation du serveur : " + e.getMessage());
        }
    }

    public void startChat() {
        try (Scanner scanner = new Scanner(System.in)) {
            // Envoyer message d'accueil
            pw.println("Bonjour, cher client !");
            pw.println("Voici un exemple de log du firewall.");

            // Envoyer un exemple d'objet LongEntry
            LongEntry logExample = new LongEntry("192.168.1.1", "192.168.1.2", 5080, 80, 500, "in");
           // objectOutputStream.writeObject(logExample);
           // objectOutputStream.flush();

            // Lancer un thread pour écouter le client
            new Thread(() -> {
                try {
                    String clientMessage;
                    while ((clientMessage = br.readLine()) != null) {
                        System.out.println("Client : " + clientMessage);
                    }
                } catch (IOException e) {
                    System.err.println("Connexion au client perdue.");
                }
            }).start();

            // Lire l'entrée de l'utilisateur et envoyer au client
            String message;
            while (true) {
                System.out.print("Moi : ");
                message = scanner.nextLine();
                pw.println(message);
            }
        } catch (Exception e) {
            System.err.println("Erreur de communication : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ServerConsole server = new ServerConsole(9999);
        server.startChat();
    }
}

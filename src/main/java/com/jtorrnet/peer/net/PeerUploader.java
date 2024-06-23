package com.jtorrnet.peer.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class PeerUploader extends Thread{
    private final int port;
    private final ServerSocket serverSocket;
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public PeerUploader(int port) {
        this.port = port;
        try {
            System.out.println("Peer File Server is Starting in Port "+port);
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // receive file function is start here

    private static void sendFile(String path) throws Exception {
        int bytes = 0;
        // Open the File where he located in your pc
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        // Here we send the File to Server
        dataOutputStream.writeLong(file.length());
        // Here we  break file into chunks
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer)) != -1) {
          // Send the file to Server Socket
          dataOutputStream.write(buffer, 0, bytes);
            dataOutputStream.flush();
        }
        // close the file here
        fileInputStream.close();
    }



    @Override public void run() {
         try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New Peer connection from " + clientSocket.getInetAddress().getHostAddress());

                // Create a new thread to handle the client connection
                Thread clientThread = new Thread(() -> {
                    try {
                        handleClient(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                clientThread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private void handleClient(Socket clientSocket) throws IOException {
        try (DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream())) {

            // Read the file path from the client
            String filePath = dis.readUTF();
            System.out.println("Received file request for: " + filePath);

            // Send the file
            try {
                sendFile(filePath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } finally {
            clientSocket.close();
        }
    }



    public int getPort() {
        return port;
    }
}

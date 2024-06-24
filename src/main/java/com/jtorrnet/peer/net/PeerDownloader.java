package com.jtorrnet.peer.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class PeerDownloader {
    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;

    public PeerDownloader(int port) {
        try (Socket socket = new Socket("localhost", port)) {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveFile(String fileSavePath) throws Exception {
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileSavePath);

        long size = dataInputStream.readLong(); // read file size
        byte[] buffer = new byte[4 * 1024];
        while (size > 0 && (bytes = dataInputStream
                .read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
            // Here we write the file using write method
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes; // read up to file size
        }

        // Here we received file
        System.out.println("File is Received");
        fileOutputStream.close();
        dataInputStream.close();
        dataOutputStream.close();
    }


}

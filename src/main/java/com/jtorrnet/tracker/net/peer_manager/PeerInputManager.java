package com.jtorrnet.tracker.net.peer_manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class PeerInputManager extends Thread {
    private final Socket socket;
    private final InputStream socketInputStream;
    private final BufferedReader socketBufferedReader;

    public PeerInputManager(Socket peer) throws IOException {
        this.socket = peer;
        this.socketInputStream = peer.getInputStream();
        this.socketBufferedReader = new BufferedReader(new InputStreamReader(this.socketInputStream));
    }

    public void run() {
        // Read from socketInputStream
        while (socket.isConnected()) {

            try {

                String line = socketBufferedReader.readLine();

                if(line == null)
                    throw new IOException("Disconnected from the socket. " + socket);

                System.out.println(socket.isClosed() + " Has said " + line);

            } catch (IOException e) {
                System.out.println("Warning: " + e);
                break;
            }
        }
        System.out.println("Info (PeerInputReader): disconnected from " + socket);
    }
}

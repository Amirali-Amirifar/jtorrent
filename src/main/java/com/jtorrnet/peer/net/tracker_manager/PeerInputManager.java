package com.jtorrnet.peer.net.tracker_manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class PeerInputManager extends Thread {

    private final Socket trackerSocket;
    private final InputStream socketInputStream;
    private final BufferedReader socketBufferedReader;

    public PeerInputManager(Socket peer) throws IOException {
        this.trackerSocket = peer;
        this.socketInputStream = peer.getInputStream();
        this.socketBufferedReader = new BufferedReader(new InputStreamReader(this.socketInputStream));
    }

    public void run() {
        // Read from socketInputStream
        while (trackerSocket.isConnected()) {

            try {

                String line = socketBufferedReader.readLine();

                if (line == null)
                    throw new IOException("Disconnected from the socket. " + trackerSocket);

                System.out.println(trackerSocket.isClosed() + " Has said " + line);

            } catch (IOException e) {
                System.out.println("Warning: " + e);
                break;
            }
        }
        System.out.println("Info (PeerInputReader): disconnected from " + trackerSocket);
    }


}

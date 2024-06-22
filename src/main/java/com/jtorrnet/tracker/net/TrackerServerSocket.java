package com.jtorrnet.tracker.net;

import com.jtorrnet.tracker.net.peer_manager.PeerInputManager;
import com.jtorrnet.tracker.net.peer_manager.PeerManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TrackerServerSocket extends ServerSocket {

    Thread serverThread;
    boolean running;

    public TrackerServerSocket(int port) throws IOException {
        super(port);
        serverThread = new Thread(this::listen);
        running = true;
    }

    public void startThreadAndListen() {
        serverThread.start();
    }

    private void listen() {
        while (running) {
            System.out.println("Waiting for peers. ");
            try {
                Socket peer = this.accept();
                new PeerManager(peer); // todo persist in memory.
                System.out.println("Peer connected. " + peer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
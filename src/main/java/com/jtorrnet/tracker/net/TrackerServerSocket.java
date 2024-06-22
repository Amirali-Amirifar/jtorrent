package com.jtorrnet.tracker.net;

import com.jtorrnet.tracker.net.peer_manager.TrackerStreamManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TrackerServerSocket extends ServerSocket {

    Thread serverThread;
    boolean running;
    private final List<Socket> sockets = new ArrayList<>();

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
                sockets.add(peer);

                new TrackerStreamManager(peer); // todo persist in memory.

                System.out.println("Peer connected. " + peer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

package com.jtorrnet.tracker.net;

import com.jtorrnet.tracker.net.peer_manager.TrackerStreamManager;
import com.jtorrnet.tracker.state.StateManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class TrackerServerSocket extends ServerSocket {

    private final Thread serverThread;
    private final StateManager stateManager;

    private boolean running;

    public TrackerServerSocket(int port, StateManager stateManager) throws IOException {
        super(port);
        this.stateManager = stateManager;
        this.serverThread = new Thread(this::listen);
        this.running = true;
    }

    public void startThreadAndListen() {
        this.serverThread.start();
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.running = false;
        serverThread.interrupt();
    }

    private void listen() {
        while (running) {
            System.out.println("Waiting for peers. ");
            try {
                Socket peer = this.accept();
                new TrackerStreamManager(peer, stateManager);

                System.out.println("Peer connected. " + peer);
            } catch (IOException e) {
                // noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }
}

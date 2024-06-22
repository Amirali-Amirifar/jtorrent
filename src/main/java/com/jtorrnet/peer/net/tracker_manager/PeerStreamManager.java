package com.jtorrnet.peer.net.tracker_manager;

import java.io.IOException;
import java.net.Socket;

public class PeerStreamManager {
    private final PeerInputManager peerInputManager;
    private final PeerOutputManager peerOutputManager;

    public PeerStreamManager(Socket tracker) throws IOException {
        // Run output manager:
        this.peerOutputManager = new PeerOutputManager(tracker);
        this.peerInputManager = new PeerInputManager(tracker);

        peerInputManager.start();
        peerOutputManager.start();
    }

    public void sendMessage(String message) {
        peerOutputManager.sendMessage(message);
    }
}

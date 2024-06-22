package com.jtorrnet.tracker.net.peer_manager;

import java.io.IOException;
import java.net.Socket;

public class PeerManager {
    public PeerManager(Socket socket) throws IOException {
        // Run PeerInputManager
        PeerInputManager peerInputManager = new PeerInputManager(socket);
        peerInputManager.start();
        // Run PeerOutputManager


    }
}

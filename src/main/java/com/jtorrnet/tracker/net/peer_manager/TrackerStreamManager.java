package com.jtorrnet.tracker.net.peer_manager;

import java.io.IOException;
import java.net.Socket;

public class TrackerStreamManager {
    public TrackerStreamManager(Socket socket) throws IOException {
        // Run PeerInputManager
        TrackerInputManager peerInputManager = new TrackerInputManager(socket);
        peerInputManager.start();
        // Run PeerOutputManager
        TrackerOutputManager peerOutputManager = new TrackerOutputManager(socket);
        peerOutputManager.start();

    }
}

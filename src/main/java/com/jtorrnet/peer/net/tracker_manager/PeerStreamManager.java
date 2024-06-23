package com.jtorrnet.peer.net.tracker_manager;

import java.io.IOException;
import java.net.Socket;

public class PeerStreamManager {
    private final PeerOutputManager peerOutputManager;

    public PeerStreamManager(Socket tracker) throws IOException {
        this.peerOutputManager = new PeerOutputManager(tracker);
        peerOutputManager.start();
    }
}

package com.jtorrnet.peer.net.tracker_manager;

import java.io.IOException;
import java.net.Socket;

public class PeerStreamManager {
    public PeerStreamManager(Socket tracker) throws IOException {
        // Run output manager:
        PeerOutputManager trackerOutputManager = new PeerOutputManager(tracker);
        trackerOutputManager.start();
    }
}

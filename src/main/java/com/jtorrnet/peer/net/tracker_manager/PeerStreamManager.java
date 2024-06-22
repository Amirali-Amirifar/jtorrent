package com.jtorrnet.peer.net.tracker_manager;

import com.jtorrnet.tracker.net.peer_manager.TrackerInputManager;

import java.io.IOException;
import java.net.Socket;

public class PeerStreamManager {
    public PeerStreamManager(Socket tracker) throws IOException {
        // Run output manager:
        PeerOutputManager trackerOutputManager = new PeerOutputManager(tracker);
        trackerOutputManager.start();
        TrackerInputManager trackerInputManager = new TrackerInputManager(tracker);
        trackerInputManager.start();
    }
}

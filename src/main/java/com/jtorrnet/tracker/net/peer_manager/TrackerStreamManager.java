package com.jtorrnet.tracker.net.peer_manager;

import java.io.IOException;
import java.net.Socket;

public class TrackerStreamManager {
    private final TrackerInputManager trackerInputManager;
    private final TrackerOutputManager trackerOutputManager;

    public TrackerStreamManager(Socket socket) throws IOException {
        // Run PeerInputManager and PeerOutputManager
        trackerInputManager = new TrackerInputManager(socket);
        trackerOutputManager = new TrackerOutputManager(socket);

        trackerInputManager.start();
        trackerOutputManager.start();

        trackerInputManager.addStreamManager(this);
        trackerOutputManager.addStreamManager(this);
    }

    protected void handleOnDisconnected() {
        trackerInputManager.interrupt();
        trackerOutputManager.interrupt();
    }
}

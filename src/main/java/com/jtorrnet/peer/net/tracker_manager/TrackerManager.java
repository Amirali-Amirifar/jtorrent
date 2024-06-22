package com.jtorrnet.peer.net.tracker_manager;

import java.io.IOException;
import java.net.Socket;

public class TrackerManager {
    public TrackerManager(Socket tracker) throws IOException {
        // Run output manager:
        TrackerOutputManager trackerOutputManager = new TrackerOutputManager(tracker);
        trackerOutputManager.start();
    }
}

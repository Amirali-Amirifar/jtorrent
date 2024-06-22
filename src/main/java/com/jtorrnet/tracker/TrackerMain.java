package com.jtorrnet.tracker;

import com.jtorrnet.tracker.net.TrackerServerSocket;
import com.jtorrnet.tracker.state.StateManager;

import java.io.IOException;

public class TrackerMain {
    public static void main(String[] args) throws IOException {
        // Run the tracker server.
        StateManager stateManager = new StateManager();
        TrackerServerSocket serverSocket = new TrackerServerSocket(3000, stateManager);

        serverSocket.startThreadAndListen();
    }
}

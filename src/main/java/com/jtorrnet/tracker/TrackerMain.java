package com.jtorrnet.tracker;

import com.jtorrnet.tracker.net.TrackerServerSocket;
import com.jtorrnet.tracker.state.StateManager;

import java.io.IOException;

public class TrackerMain {

    public static void main(String[] args) {
        StateManager stateManager = new StateManager();

        final int PORT = 3000;
        try {
            TrackerServerSocket serverSocket = new TrackerServerSocket(PORT, stateManager);
            serverSocket.startThreadAndListen();
        } catch (IOException e) {
            throw new RuntimeException("Could not start the tracker server.");
        }

    }
}

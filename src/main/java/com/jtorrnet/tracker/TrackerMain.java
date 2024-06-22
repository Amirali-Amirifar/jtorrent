package com.jtorrnet.tracker;

import com.jtorrnet.tracker.net.TrackerServerSocket;

import java.io.IOException;

public class TrackerMain {
    public static void main(String[] args) throws IOException {
        // Run the tracker server.
        TrackerServerSocket serverSocket = new TrackerServerSocket(3000);
        serverSocket.startThreadAndListen();
    }
}

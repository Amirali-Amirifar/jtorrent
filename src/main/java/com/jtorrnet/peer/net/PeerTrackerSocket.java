package com.jtorrnet.peer.net;

import com.jtorrnet.peer.net.tracker_manager.PeerStreamManager;

import java.io.IOException;
import java.net.Socket;

public class PeerTrackerSocket extends Socket {
    PeerStreamManager peerStreamManager;

    public PeerTrackerSocket(String host, int port) throws IOException {
        super(host, port);
        System.out.println("Connected to " + host + ":" + port);
        peerStreamManager = new PeerStreamManager(this);
    }

    public void sendMessage(String message) {
        peerStreamManager.sendMessage(message);
    }
}

package com.jtorrnet.peer;

import com.jtorrnet.peer.net.PeerTrackerSocket;

import java.io.IOException;

public class PeerMain {
    public static void main(String[] args) throws IOException {
        // Peer-Tracker handshake.
        // Start Peer client.
        System.out.println("Attemtpting to connect to the peer");
        PeerTrackerSocket socket = new PeerTrackerSocket("127.0.0.1", 3000);

        while (socket.isConnected()){
            // wait;

        }
        System.out.println("Disconnected");
    }
}

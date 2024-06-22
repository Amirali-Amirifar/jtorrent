package com.jtorrnet.peer.net;

import com.jtorrnet.peer.net.tracker_manager.TrackerManager;

import javax.sound.midi.Track;
import java.io.IOException;
import java.net.Socket;

public class PeerTrackerSocket extends Socket {
    public PeerTrackerSocket(String host, int port) throws IOException {
        super(host, port);
        System.out.println("Connected to " + host + ":" + port);
        TrackerManager trackerManager = new TrackerManager(this);
    }

}

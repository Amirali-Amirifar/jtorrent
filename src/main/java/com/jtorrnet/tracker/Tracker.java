package com.jtorrnet.tracker;

import com.jtorrnet.models.File;
import com.jtorrnet.peer.Peer;

import java.util.ArrayList;
import java.util.List;

public class Tracker {
    private List<Peer> peers;


    // List of all files available on the network.
    private List<File> files;

    public Tracker() {
        peers = new ArrayList<Peer>();
    }


}

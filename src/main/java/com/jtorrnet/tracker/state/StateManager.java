package com.jtorrnet.tracker.state;

import com.jtorrnet.lib.models.PeerModel;

import java.util.ArrayList;
import java.util.List;

public class StateManager {
    private final List<PeerModel> peers;

    public StateManager() {
        this.peers = new ArrayList<>();
    }

    public void addPeer(PeerModel peerModel) {
        peers.add(peerModel);
    }
    public void removePeer(PeerModel peerModel) {
        peers.remove(peerModel);
    }

    public List<PeerModel> getPeers() {
        return peers;
    }

    public List<String> getFiles() {
        List<String> files = new ArrayList<>();
        for (PeerModel peer : peers) {
            files.addAll(peer.files);
        }
        return files;
    }
}

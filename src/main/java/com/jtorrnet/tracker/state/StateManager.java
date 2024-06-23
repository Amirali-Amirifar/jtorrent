package com.jtorrnet.tracker.state;

import com.jtorrnet.lib.models.PeerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StateManager {
    private final CopyOnWriteArrayList<PeerModel> peers;

    public StateManager() {
        this.peers = new CopyOnWriteArrayList<>();
    }

    public void addPeer(PeerModel peerModel) {
        for (PeerModel pm : peers) {
            if (pm.isIpAndPortEqual(peerModel)) {
                peers.remove(pm);
                peers.add(peerModel);
                return;
            }
        }
        System.out.println("new peer connected. " + peerModel.getIp() + ":" + peerModel.getPort());
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
            files.addAll(peer.getFiles());
        }
        return files;
    }

}

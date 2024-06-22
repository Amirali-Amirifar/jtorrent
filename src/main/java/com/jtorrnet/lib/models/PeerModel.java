package com.jtorrnet.lib.models;

import com.jtorrnet.tracker.net.TrackerServerSocket;
import com.jtorrnet.tracker.net.peer_manager.TrackerStreamManager;

import java.util.List;

public class PeerModel {
    public String name;
    public String ip;
    public String port;
    public List<String> files;
    public String udpPort;
    public TrackerStreamManager trackerStreamManager;
    public PeerModel(String name, String ip, String port, List<String> files) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.files = files;
    }
}

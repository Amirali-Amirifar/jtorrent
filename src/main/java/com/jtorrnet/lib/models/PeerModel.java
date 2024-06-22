package com.jtorrnet.lib.models;

import java.util.List;

public class PeerModel {
    public String name;
    public String ip;
    public String port;
    public List<String> files;
    public String udpPort;
    public PeerModel(String name, String ip, String port, List<String> files) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.files = files;
    }
}

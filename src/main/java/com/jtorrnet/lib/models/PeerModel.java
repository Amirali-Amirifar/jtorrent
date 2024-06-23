package com.jtorrnet.lib.models;

import com.jtorrnet.tracker.net.peer_manager.TrackerStreamManager;

import java.util.List;

public class PeerModel {
    private String name;
    private String ip;
    private String port;
    private List<String> files;
    private String tcpPort;
    private TrackerStreamManager trackerStreamManager;
    private long lastInteraction;

    public PeerModel(String name, String ip, String port, List<String> files) {
        this.setName(name);
        this.setIp(ip);
        this.setPort(port);
        this.setFiles(files);
        setLastInteraction(System.currentTimeMillis());
    }


    public boolean isIpAndPortEqual(PeerModel obj) {
        return this.getIp().equals(obj.getIp()) && this.getPort().equals(obj.getPort());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(String tcpPort) {
        this.tcpPort = tcpPort;
    }

    public TrackerStreamManager getTrackerStreamManager() {
        return trackerStreamManager;
    }

    public void setTrackerStreamManager(TrackerStreamManager trackerStreamManager) {
        this.trackerStreamManager = trackerStreamManager;
    }

    public long getLastInteraction() {
        return lastInteraction;
    }

    public void setLastInteraction(long lastInteraction) {
        this.lastInteraction = lastInteraction;
    }
}

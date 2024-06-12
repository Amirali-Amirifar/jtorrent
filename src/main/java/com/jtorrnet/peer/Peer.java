package com.jtorrnet.peer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Peer {
    // Represents a peer on the network.
    public final UUID uuid;
    private final int port;
    private final String ip;
    private final String name;
    private List<File> shartedFiles;
    public Peer(UUID uuid, int port, String ip, String name) {
        this.uuid = uuid;
        this.port = port;
        this.ip = ip;
        this.name = name;
        this.shartedFiles = new ArrayList<File>();
    }
}

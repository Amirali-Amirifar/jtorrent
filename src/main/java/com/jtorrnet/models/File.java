package com.jtorrnet.models;

import java.util.UUID;

public class File {
    // or maybe use private final Peer Owner?
    private final UUID ownerId;
    private final String filename;

    public File(UUID ownerId, String filename) {
        this.ownerId = ownerId;
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public UUID getOwnerId() {
        return ownerId;
    }
}

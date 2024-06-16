package com.jtorrnet.tracker;

import com.jtorrnet.models.File;
import com.jtorrnet.peer.Peer;

import java.util.ArrayList;
import java.util.List;

public class Tracker {
    private final List<Peer> peers;


    // List of all files available on the network.
    private final List<File> files;

    public Tracker() {
        peers = new ArrayList<Peer>();
        files = new ArrayList<>();


        File file1 = new File(null, "app.txt");
        File file2 = new File(null, "project.mp4");
        File file3 = new File(null, "pink floyd - shine on you crazy diamond.mp3");
        File file4 = new File(null, "");
        files.add(file1);
        files.add(file2);
        files.add(file3);
        files.add(file4);

    }


    public String getFileNames() {
        StringBuilder sb = new StringBuilder();
        for (File f : files) {
            sb.append(f.getFilename()).append(" \r\n");
        }
        return sb.toString();
    }

}

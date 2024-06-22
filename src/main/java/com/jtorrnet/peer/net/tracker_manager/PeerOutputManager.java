package com.jtorrnet.peer.net.tracker_manager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;

public class PeerOutputManager extends Thread {
    private final Socket trackerSocket;
    private final OutputStream outputStream;
    private final PrintWriter outputStreamWriter;

    public PeerOutputManager(Socket tracker) throws IOException {
        this.trackerSocket = tracker;
        this.outputStream = tracker.getOutputStream();
        this.outputStreamWriter = new PrintWriter(outputStream);
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Writing...");
                outputStreamWriter.println("I am connected, " + trackerSocket + " Time: " + new Date(System.currentTimeMillis()).getTime());
                outputStreamWriter.flush();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Failed - " + e);
                return;
            }
        }
    }
}

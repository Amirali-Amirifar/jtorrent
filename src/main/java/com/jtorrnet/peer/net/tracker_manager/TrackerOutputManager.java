package com.jtorrnet.peer.net.tracker_manager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;

public class  TrackerOutputManager extends Thread {
    private final Socket socket;
    private final OutputStream trackerOutputStream;
    private final PrintWriter trackerOutputWriter;

    public TrackerOutputManager(Socket tracker) throws IOException {
        this.socket = tracker;
        this.trackerOutputStream = tracker.getOutputStream();
        this.trackerOutputWriter = new PrintWriter(trackerOutputStream);
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Writing...");
                trackerOutputWriter.println("I am connected, " + socket + " Time: " + new Date(System.currentTimeMillis()).getTime());
                trackerOutputWriter.flush();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Failed - " + e);
                return;
            }
        }
    }
}

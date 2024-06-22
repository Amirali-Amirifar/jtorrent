package com.jtorrnet.tracker.net.peer_manager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TrackerOutputManager extends Thread{
    private final PrintWriter outputStreamWriter;

    public TrackerOutputManager(Socket peerSocket) throws IOException {
        OutputStream outputStream = peerSocket.getOutputStream();
        this.outputStreamWriter = new PrintWriter(outputStream);
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(20_000);
            } catch (InterruptedException e) {
                this.interrupt();
                return;
            }
        }
    }

    public void sendMessage(String message) {
        outputStreamWriter.println(message);
        outputStreamWriter.flush();
    }
}

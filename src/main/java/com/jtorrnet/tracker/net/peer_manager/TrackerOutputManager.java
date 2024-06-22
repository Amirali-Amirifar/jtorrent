package com.jtorrnet.tracker.net.peer_manager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;

public class TrackerOutputManager extends Thread{
    private final Socket peerSocket;
    private final OutputStream outputStream;
    private final PrintWriter outputStreamWriter;

    public TrackerOutputManager(Socket peerSocket) throws IOException {
        this.peerSocket = peerSocket;
        this.outputStream = peerSocket.getOutputStream();
        this.outputStreamWriter = new PrintWriter(outputStream);
    }

    @Override
    public void run() {
        while(true) {
            try {
                System.out.println("Writing...");
                outputStreamWriter.println("I am connected, " + peerSocket + " Time: " + new Date(System.currentTimeMillis()).getTime());
                outputStreamWriter.flush();
                Thread.sleep(5000);
                if(peerSocket.isClosed())
                    throw new IOException("Connection closed to " + peerSocket);

            } catch (InterruptedException | IOException e) {
                System.out.println(e);
                return;
            }
        }
    }
}

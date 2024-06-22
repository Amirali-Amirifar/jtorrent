package com.jtorrnet.peer.net.tracker_manager;

import com.jtorrnet.lib.messaging.Message;
import com.jtorrnet.lib.messaging.typing.MessageType;
import com.jtorrnet.lib.messaging.typing.RequestType;

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
                outputStreamWriter.println(new Message(MessageType.REQUEST, RequestType.KEEP_ALIVE, "").getMessage());
                outputStreamWriter.flush();
                Thread.sleep(20_000);
            } catch (InterruptedException e) {
                System.out.println("Failed - " + e);
                return;
            }
        }
    }

    public void sendMessage(String message) {
        outputStreamWriter.println(message);
        outputStreamWriter.flush();
    }
}

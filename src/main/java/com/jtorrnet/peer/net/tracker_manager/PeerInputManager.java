package com.jtorrnet.peer.net.tracker_manager;

import com.jtorrnet.lib.messaging.Message;
import com.jtorrnet.lib.messaging.typing.MessageType;
import com.jtorrnet.lib.messaging.typing.RequestType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class PeerInputManager extends Thread {

    private final Socket trackerSocket;
    private final InputStream socketInputStream;
    private final BufferedReader socketBufferedReader;

    public PeerInputManager(Socket peer) throws IOException {
        this.trackerSocket = peer;
        this.socketInputStream = peer.getInputStream();
        this.socketBufferedReader = new BufferedReader(new InputStreamReader(this.socketInputStream));
    }

    public void run() {
        // Read from socketInputStream
        while (trackerSocket.isConnected()) {

            try {

                String line = socketBufferedReader.readLine();

                if (line == null)
                    throw new IOException("Disconnected from the socket. " + trackerSocket);

                Message msg = new Message(line);

                String show = cleanMessage(msg);

                System.out.println(show);

            } catch (IOException e) {
                System.out.println("Warning: " + e);
                break;
            }
        }
        System.out.println("Info (PeerInputReader): disconnected from " + trackerSocket);
    }

    private String cleanMessage(Message msg) {
        if (msg.getType().equals(MessageType.REQUEST))
            return msg.getMessage();


        if (msg.getRequestType().equals(RequestType.GET_PEERS) || msg.getRequestType().equals(RequestType.LIST_FILES))
            return "Tracker said: \n" + msg.getBody().replace("\\n", "\n");


        return "OOPS " + msg.getMessage();
    }


}

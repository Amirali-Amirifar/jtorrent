package com.jtorrnet.tracker.net.peer_manager;

import com.jtorrnet.lib.messaging.Message;
import com.jtorrnet.lib.messaging.typing.RequestType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TrackerInputManager extends Thread {
    private final Socket peerSocket;
    private final BufferedReader socketBufferedReader;
    private TrackerStreamManager trackerStreamManager;

    public TrackerInputManager(Socket peer) throws IOException {
        this.peerSocket = peer;
        InputStream socketInputStream = peer.getInputStream();
        this.socketBufferedReader = new BufferedReader(new InputStreamReader(socketInputStream));
    }

    @Override
    public void run() {
        // Read from socketInputStream
        while (peerSocket.isConnected()) {

            try {

                String message = socketBufferedReader.readLine();

                if (message == null)
                    throw new IOException("Disconnected from the socket. " + peerSocket);


                Message msg = new Message(message);
                trackerStreamManager.handleMessage(msg);
                if (!msg.getRequestType().equals(RequestType.KEEP_ALIVE))
                    System.out.println(peerSocket.getPort() + " Has said " + message);

            } catch (IOException e) {
                System.out.println("Info (PeerInputReader): disconnected from " + peerSocket.getInetAddress() + peerSocket.getPort());
                break;
            }
        }

        trackerStreamManager.handleOnDisconnected();
    }

    public void addStreamManager(TrackerStreamManager trackerStreamManager) {
        this.trackerStreamManager = trackerStreamManager;
    }
}

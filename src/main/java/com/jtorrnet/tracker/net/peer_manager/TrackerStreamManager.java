package com.jtorrnet.tracker.net.peer_manager;

import com.jtorrnet.lib.messaging.Message;
import com.jtorrnet.lib.messaging.typing.MessageType;
import com.jtorrnet.lib.messaging.typing.RequestType;

import java.io.IOException;
import java.net.Socket;

public class TrackerStreamManager {
    private final Socket peerSocket;
    private final TrackerInputManager trackerInputManager;
    private final TrackerOutputManager trackerOutputManager;

    public TrackerStreamManager(Socket socket) throws IOException {
        this.peerSocket = socket;

        // Run PeerInputManager and PeerOutputManager
        trackerInputManager = new TrackerInputManager(socket);
        trackerOutputManager = new TrackerOutputManager(socket);

        trackerInputManager.start();
        trackerOutputManager.start();

        trackerInputManager.addStreamManager(this);
        trackerOutputManager.addStreamManager(this);
    }

    protected void handleOnDisconnected() {
        trackerInputManager.interrupt();
        trackerOutputManager.interrupt();
    }

    public void handleMessage(Message msg) {
        Message newMessage = new Message(MessageType.RESPONSE, RequestType.RESPONSE, "Here are the peers local port: 1342, 2342,523234,12352");
        trackerOutputManager.sendMessage(newMessage.getMessage());
    }
}

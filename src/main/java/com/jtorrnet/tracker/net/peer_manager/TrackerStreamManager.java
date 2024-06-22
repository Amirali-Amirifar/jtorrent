package com.jtorrnet.tracker.net.peer_manager;

import com.jtorrnet.lib.messaging.Message;
import com.jtorrnet.lib.messaging.typing.MessageType;
import com.jtorrnet.lib.messaging.typing.RequestType;
import com.jtorrnet.lib.models.PeerModel;
import com.jtorrnet.tracker.state.StateManager;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TrackerStreamManager {
    private final TrackerInputManager trackerInputManager;
    private final TrackerOutputManager trackerOutputManager;
    private final StateManager stateManager;
    private final PeerModel peerModel;

    public TrackerStreamManager(Socket socket, StateManager stateManager) throws IOException {
        this.stateManager = stateManager;

        this.peerModel = new PeerModel("unnamed",
                socket.getInetAddress().toString(),
                String.valueOf(socket.getPort()),
                new ArrayList<>());

        // Run PeerInputManager and PeerOutputManager
        trackerInputManager = new TrackerInputManager(socket);
        trackerOutputManager = new TrackerOutputManager(socket);

        trackerInputManager.start();
        trackerOutputManager.start();

        trackerInputManager.addStreamManager(this);

        stateManager.addPeer(this.peerModel);

    }

    protected void handleOnDisconnected() {
        trackerInputManager.interrupt();
        trackerOutputManager.interrupt();
        stateManager.removePeer(this.peerModel);
    }

    public void handleMessage(Message msg) {

        RequestType ansRequestType = msg.getRequestType();

        if (msg.getRequestType().equals(RequestType.GET_PEERS)) {
            List<PeerModel> peers = stateManager.getPeers();
            String[] list = peers.stream().map(peer -> peer.name
                    + " - "
                    + peer.ip
                    + ":"
                    + peer.port)
                    .toArray(String[]::new);

            String body = String.join(" \\n", list);

            Message newMessage = new Message(MessageType.RESPONSE, ansRequestType, body);
            trackerOutputManager.sendMessage(newMessage.getMessage());
            return;
        }

        if (msg.getRequestType().equals(RequestType.LIST_FILES)) {
            List<PeerModel> peers = stateManager.getPeers();
            String[] list = peers.stream()
                    .map(peer -> peer.name
                            + " - "
                            + peer.ip
                            + ":"
                            + peer.port
                            + " Has shared \\n "
                            + String.join("\\n", peer.files))
                    .toArray(String[]::new);

            String body = String.join(" \\n", list);

            Message newMessage = new Message(MessageType.RESPONSE, ansRequestType, body);
            trackerOutputManager.sendMessage(newMessage.getMessage());
        }

        if (msg.getRequestType().equals(RequestType.SHARE)) {
            this.peerModel.files.add(msg.getBody());
            trackerOutputManager.sendMessage(msg.getMessage());
        }

        if (msg.getRequestType().equals(RequestType.SET_NAME)) {
            this.peerModel.name = msg.getBody();
            trackerOutputManager.sendMessage(msg.getMessage());

        }

    }
}

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
import java.util.stream.Stream;

public class TrackerStreamManager {
    private final Socket peerSocket;
    private final TrackerInputManager trackerInputManager;
    private final TrackerOutputManager trackerOutputManager;
    private final StateManager stateManager;
    private final PeerModel peerModel;

    public TrackerStreamManager(Socket socket, StateManager stateManager) throws IOException {
        this.peerSocket = socket;
        this.stateManager = stateManager;

        this.peerModel = new PeerModel("unnamed", peerSocket.getInetAddress().toString(),
                String.valueOf(peerSocket.getPort()), new ArrayList<>());

        // Run PeerInputManager and PeerOutputManager
        trackerInputManager = new TrackerInputManager(socket);
        trackerOutputManager = new TrackerOutputManager(socket);

        trackerInputManager.start();
        trackerOutputManager.start();

        trackerInputManager.addStreamManager(this);
        trackerOutputManager.addStreamManager(this);

        stateManager.addPeer(this.peerModel);

    }

    protected void handleOnDisconnected() {
        trackerInputManager.interrupt();
        trackerOutputManager.interrupt();
        stateManager.removePeer(this.peerModel);
    }

    public void handleMessage(Message msg) {

        RequestType ansRequestType = msg.getRequestType();

        if (msg.getRequestType() == RequestType.GET_PEERS) {
            List<PeerModel> peers = stateManager.getPeers();
            String[] list = peers.stream().map(peer-> peer.ip + ":" +peer.port).toArray(String[]::new);
            String body = String.join(" \\n", list);

            Message newMessage = new Message(MessageType.RESPONSE, ansRequestType , body);
            trackerOutputManager.sendMessage(newMessage.getMessage());
        }

    }
}

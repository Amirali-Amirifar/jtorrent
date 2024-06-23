package com.jtorrnet.tracker.net.peer_manager;

import com.jtorrnet.lib.messaging.Message;
import com.jtorrnet.lib.messaging.typing.MessageType;
import com.jtorrnet.lib.messaging.typing.RequestType;
import com.jtorrnet.lib.models.PeerModel;
import com.jtorrnet.tracker.state.StateManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class TrackerStreamManager {
    //    private final TrackerInputManager trackerInputManager;
    public final TrackerOutputManager trackerOutputManager;
    private final StateManager stateManager;
    private final DatagramSocket socket;
    private PeerModel peerModel;

    public TrackerStreamManager(DatagramPacket packet, DatagramSocket datagramSocket, StateManager stateManager) throws IOException {
        this.stateManager = stateManager;
        this.socket = datagramSocket;

        // see if exists:
        for (PeerModel pm : stateManager.getPeers()) {
            if (Integer.parseInt(pm.port) == (packet.getPort())) {
                this.peerModel = pm;
                break;
            }
        }

        if (peerModel == null) {
            this.peerModel = new PeerModel("unnamed",
                    packet.getAddress().toString(),
                    String.valueOf(packet.getPort()),
                    new ArrayList<>());
        }
        // Run PeerInputManager and PeerOutputManager
//        trackerInputManager = new TrackerInputManager(socket);
        trackerOutputManager = new TrackerOutputManager(packet, datagramSocket);
//
//        trackerInputManager.start();
//        trackerOutputManager.start();

//        trackerInputManager.addStreamManager(this);

        peerModel.trackerStreamManager = this;
        stateManager.addPeer(this.peerModel);

        // convert packet to string
        String message = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Peer connected. " + message);
        this.handleMessage(message);


    }

//    protected void handleOnDisconnected() {
//        trackerInputManager.interrupt();
//        trackerOutputManager.interrupt();
//        stateManager.removePeer(this.peerModel);
//    }

    public void handleMessage(String message) {
        Message msg = new Message(message);
        handleMessage(msg);
    }

    public void handleMessage(Message msg) {
        System.out.println("Handling msg");
        RequestType ansRequestType = msg.getRequestType();
        this.peerModel.lastInteraction = System.currentTimeMillis();

        if (msg.getRequestType().equals(RequestType.KEEP_ALIVE)) {
            trackerOutputManager.sendMessage(msg.getMessage());
            System.out.println("got keep alive from " + this.peerModel.name + " port " + this.peerModel.port);
            return;
        }
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
                            + peer.port + "//UDP: " + peer.udpPort
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
        if (msg.getRequestType().equals(RequestType.UDPPORT)) {
            this.peerModel.udpPort = msg.getBody();
            trackerOutputManager.sendMessage(msg.getMessage());
        }

        if (msg.getRequestType().equals(RequestType.GET)) {
            String[] args = msg.getBody().split("\\|");
            String port = args[0];
            String filename = args[1];
            // Find which peer has such port and filename
            List<PeerModel> seeder = stateManager.getPeers();
            for (PeerModel se : seeder) {
                if (se.udpPort.equals(port)) {
                    String body = this.peerModel.udpPort + "|" + filename;
                    System.out.println("FOUND PEER " + se);
                    se.trackerStreamManager.trackerOutputManager.sendMessage(new Message(MessageType.REQUEST, RequestType.GET, body).getMessage());
                    break;
                }
            }
            System.out.println("Finished");

        }
    }
}

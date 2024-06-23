package com.jtorrnet.tracker.net.peer_manager;

import com.jtorrnet.lib.messaging.Message;
import com.jtorrnet.lib.messaging.typing.MessageType;
import com.jtorrnet.lib.messaging.typing.RequestType;
import com.jtorrnet.lib.models.PeerModel;
import com.jtorrnet.tracker.state.StateManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrackerStreamManager {
    public final TrackerOutputManager trackerOutputManager;
    private final StateManager stateManager;
    private PeerModel peerModel;

    public TrackerStreamManager(DatagramPacket packet, DatagramSocket datagramSocket, StateManager stateManager) {
        this.stateManager = stateManager;

        // see if exists:
        for (PeerModel pm : stateManager.getPeers()) {
            if (Integer.parseInt(pm.getPort()) == (packet.getPort())) {
                this.peerModel = pm;
                break;
            }
        }

        if (peerModel == null)
            this.peerModel = new PeerModel("unnamed" + new Random().nextInt(100),
                    packet.getAddress().toString(),
                    String.valueOf(packet.getPort()),
                    new ArrayList<>());



        trackerOutputManager = new TrackerOutputManager(packet, datagramSocket);

        peerModel.setTrackerStreamManager(this);

        stateManager.addPeer(this.peerModel);

        // convert packet to string
        String message = new String(packet.getData(), 0, packet.getLength());
        this.handleMessage(message);
    }


    public void handleMessage(String message) {
        Message msg = new Message(message);
        handleMessage(msg);
    }

    /**
     * Get the message which peer has sent, process and return another message in response.
     *
     * @param msg Message object received by the server from peer.
     */
    public void handleMessage(Message msg) {
        System.out.println(this.peerModel.getName() + "/" + this.peerModel.getPort() + " - " +
                msg.getMessage().replace("$", " ").substring(0, msg.getMessage().length() - 3));

        this.peerModel.setLastInteraction(System.currentTimeMillis());

        switch(msg.getRequestType()){
            case GET_PEERS:
                handleGetPeersMsg();
                break;
            case LIST_FILES:
                handleListFilesMsg();
                break;
            case SHARE:
                handleShareMsg(msg);
                break;
            case SET_NAME:
                handleSetNameMsg(msg);
                break;
            case TCP_PORT:
                handleTCPPortMsg(msg);
                break;
            case GET:
                handleGetFileMsg(msg);
                break;
            case KEEP_ALIVE:
                trackerOutputManager.sendMessage(msg);
                break;
        }
    }

    private void handleTCPPortMsg(Message msg) {
        this.peerModel.setTcpPort(msg.getBody());
        trackerOutputManager.sendMessage(msg);
    }

    private void handleSetNameMsg(Message msg) {
        this.peerModel.setName(msg.getBody());
        trackerOutputManager.sendMessage(msg);
    }

    private void handleShareMsg(Message msg) {
        this.peerModel.getFiles().add(msg.getBody().strip());
        trackerOutputManager.sendMessage(msg);
    }


    private void handleGetFileMsg(Message msg) {
        String[] args = msg.getBody().split("\\|");
        String port = args[0];
        String filename = args[1];
        // Find which peer has such port and filename
        List<PeerModel> seeder = stateManager.getPeers();
        for (PeerModel se : seeder) {
            if (se.getTcpPort().equals(port)) {
                String body = this.peerModel.getTcpPort() + "|" + filename;
                System.out.println("FOUND PEER " + se);
                se.getTrackerStreamManager().trackerOutputManager
                        .sendMessage(new Message(MessageType.REQUEST, RequestType.GET, body));
                break;
            }
        }
    }

    private void handleListFilesMsg() {
        List<PeerModel> peers = stateManager.getPeers();
        String[] list = peers.stream()
                .map(peer -> peer.getName()
                        + " - "
                        + peer.getIp()
                        + ":"
                        + peer.getPort() + " With P2P TCP Port: " + peer.getTcpPort()
                        + " - Seeds: \\n -- "
                        + String.join("\\n -- ", peer.getFiles()))
                .toArray(String[]::new);

        String body = String.join(" \\n", list);

        Message newMessage
                = new Message(MessageType.RESPONSE, RequestType.LIST_FILES, body);
        trackerOutputManager.sendMessage(newMessage);
    }

    private void handleGetPeersMsg() {
        List<PeerModel> peers = stateManager.getPeers();
        String[] list = peers.stream().map(peer -> peer.getName()
                        + " - "
                        + peer.getIp()
                        + ":"
                        + peer.getPort())
                .toArray(String[]::new);

        String body = String.join(" \\n", list);

        Message newMessage = new Message(MessageType.RESPONSE, RequestType.GET_PEERS, body);
        trackerOutputManager.sendMessage(newMessage);
    }
}

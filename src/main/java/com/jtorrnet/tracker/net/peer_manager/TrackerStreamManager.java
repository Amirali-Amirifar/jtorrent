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
import java.util.Random;

public class TrackerStreamManager {
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
            this.peerModel = new PeerModel("unnamed" + new Random().nextInt(100),
                    packet.getAddress().toString(),
                    String.valueOf(packet.getPort()),
                    new ArrayList<>());
        }


        trackerOutputManager = new TrackerOutputManager(packet, datagramSocket);

        peerModel.trackerStreamManager = this;

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
        System.out.println(this.peerModel.name + "/" + this.peerModel.port + " - " +
                msg.getMessage().replace("$", " ").substring(0, msg.getMessage().length() - 3));

        this.peerModel.lastInteraction = System.currentTimeMillis();

        switch(msg.getRequestType()){
            case GET_PEERS:
                handleGetPeersMsg();
                break;
            case LIST_FILES:
                handleListFilesMsg();
                break;
            case SHARE:
                this.peerModel.files.add(msg.getBody().strip());
                trackerOutputManager.sendMessage(msg);
                break;
            case SET_NAME:
                this.peerModel.name = msg.getBody();
                trackerOutputManager.sendMessage(msg);
                break;
            case UDPPORT:
                this.peerModel.tcpPort = msg.getBody();
                trackerOutputManager.sendMessage(msg);
                break;
            case GET:
                handleGetFileMsg(msg);
                break;
            case KEEP_ALIVE:
                trackerOutputManager.sendMessage(msg);
                break;
        }
    }


    private void handleGetFileMsg(Message msg) {
        String[] args = msg.getBody().split("\\|");
        String port = args[0];
        String filename = args[1];
        // Find which peer has such port and filename
        List<PeerModel> seeder = stateManager.getPeers();
        for (PeerModel se : seeder) {
            if (se.tcpPort.equals(port)) {
                String body = this.peerModel.tcpPort + "|" + filename;
                System.out.println("FOUND PEER " + se);
                se.trackerStreamManager.trackerOutputManager
                        .sendMessage(new Message(MessageType.REQUEST, RequestType.GET, body));
                break;
            }
        }
    }

    private void handleListFilesMsg() {
        List<PeerModel> peers = stateManager.getPeers();
        String[] list = peers.stream()
                .map(peer -> peer.name
                        + " - "
                        + peer.ip
                        + ":"
                        + peer.port + " With P2P TCP Port: " + peer.tcpPort
                        + " - Seeds: \\n -- "
                        + String.join("\\n -- ", peer.files))
                .toArray(String[]::new);

        String body = String.join(" \\n", list);

        Message newMessage = new Message(MessageType.RESPONSE, RequestType.LIST_FILES, body);
        trackerOutputManager.sendMessage(newMessage);
    }

    private void handleGetPeersMsg() {
        List<PeerModel> peers = stateManager.getPeers();
        String[] list = peers.stream().map(peer -> peer.name
                        + " - "
                        + peer.ip
                        + ":"
                        + peer.port)
                .toArray(String[]::new);

        String body = String.join(" \\n", list);

        Message newMessage = new Message(MessageType.RESPONSE, RequestType.GET_PEERS, body);
        trackerOutputManager.sendMessage(newMessage);
    }
}

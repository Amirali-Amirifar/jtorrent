package com.jtorrnet.tracker.net;

import com.jtorrnet.lib.messaging.Message;
import com.jtorrnet.lib.messaging.typing.MessageType;
import com.jtorrnet.lib.messaging.typing.RequestType;
import com.jtorrnet.lib.models.PeerModel;
import com.jtorrnet.tracker.net.peer_manager.TrackerStreamManager;
import com.jtorrnet.tracker.state.StateManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class TrackerServerSocket {

    private final StateManager stateManager;
    private final int BUFFER_SIZE = 1024;
    private final int PORT;
    private final DatagramSocket socket;
    private final boolean running;
    Thread serverThread;
    Thread keepAliveThread;

    public TrackerServerSocket(int port, StateManager stateManager) throws IOException {
        this.PORT = port;
        this.stateManager = stateManager;
        this.serverThread = new Thread(this::listen);
        this.keepAliveThread = new Thread(this::keepAlive);

        this.running = true;

        this.socket = new DatagramSocket(port);

    }

    public void startThreadAndListen() {
        this.serverThread.start();
        this.keepAliveThread.start();
    }

    private void listen() {
        System.out.println("Started Listening for UDP connections over port " + this.PORT);

        while (running) {
            try {
                DatagramPacket packet = getPackets();

                new TrackerStreamManager(packet, socket, stateManager);

            } catch (IOException e) {
                // noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }

    private void keepAlive() {
        final int MIN_REQUEST_INTERVAL = 18_000;
        final int TIMEOUT = 20_000;
        final int THRESHOLD = TIMEOUT + MIN_REQUEST_INTERVAL + 2_000;

        System.out.println("Started Keep Alive Thread");

        while (true) {
            synchronized (stateManager.getPeers()) {
                for (PeerModel peer : stateManager.getPeers()) {
                    if (-peer.lastInteraction + System.currentTimeMillis() > MIN_REQUEST_INTERVAL) {
                        peer.trackerStreamManager
                                .trackerOutputManager
                                .sendMessage(new Message(MessageType.REQUEST, RequestType.KEEP_ALIVE, "")
                                        .getMessage());
                    }
                }
            }
            try {
                Thread.sleep(TIMEOUT);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // remove unavailable peers
            synchronized (stateManager.getPeers()) {
                List<PeerModel> remove = new ArrayList<>();
                for (PeerModel peer : stateManager.getPeers()) {
                    if (System.currentTimeMillis() - peer.lastInteraction > THRESHOLD) {
                        remove.add(peer);
                    }
                }

                for (PeerModel peer : remove) {
                    stateManager.removePeer(peer);
                    System.out.println("Removed peer " + peer.name + " port " + peer.port + " becase last interaction in "
                            + (System.currentTimeMillis() - peer.lastInteraction) + "ms");
                }
            }
        }
    }

    public DatagramPacket getPackets() {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

}

package com.jtorrnet.peer.net;

import com.jtorrnet.lib.messaging.Message;
import com.jtorrnet.lib.messaging.typing.MessageType;
import com.jtorrnet.lib.messaging.typing.RequestType;
import com.jtorrnet.peer.net.tracker_manager.PeerStreamManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class PeerTrackerSocket {

    private final int BUFFER_SIZE = 1024;
    private final int PORT;
    private final DatagramSocket socket;
    private final boolean running;
    private final int DestPort = 3000;
    PeerStreamManager peerStreamManager;
    Thread serverThread;

    public PeerTrackerSocket(String host, int port) throws IOException {
        System.out.println("Connected to " + host + ":" + DestPort);
        this.PORT = port;
        this.serverThread = new Thread(this::listen);
        this.running = true;
        this.socket = new DatagramSocket(port, InetAddress.getByName("localhost"));

        serverThread.start();

        //        peerStreamManager = new PeerStreamManager(this);
    }

    public void sendMessage(String message) throws IOException {
        DatagramPacket gotoTracker = new DatagramPacket(message.getBytes(), message.getBytes().length,
                InetAddress.getByName("localhost"),
                3000);

        socket.send(gotoTracker);
    }

    public void startThreadAndListen() {
        this.serverThread.start();
    }

    private void listen() {
        while (running) {

            DatagramPacket packet = getPackets();

            handleResponse(packet);

        }
    }

    private String cleanMessage(Message msg) {
        if (msg.getType().equals(MessageType.REQUEST))
            return msg.getMessage();


        if (msg.getRequestType().equals(RequestType.GET_PEERS) || msg.getRequestType().equals(RequestType.LIST_FILES))
            return "Tracker said: \n" + msg.getBody().replace("\\n", "\n");

        if (msg.getRequestType().equals(RequestType.GET)) {
            return msg.getBody();
        }
        if (msg.getRequestType().equals(RequestType.KEEP_ALIVE))
            return "KEEP-ALIVE REQUESTED";


        return "OOPS " + msg.getMessage();
    }


    private void handleResponse(DatagramPacket packet) {
        byte[] buffer = packet.getData();
        String _msg = new String(buffer, StandardCharsets.UTF_8);
        _msg = _msg.substring(0, _msg.indexOf("EOF"));
        Message msg = new Message(_msg);

        _msg = cleanMessage(msg);
        _msg = _msg.replace("$", " ");
        System.out.println((_msg));

        if (msg.getType().equals(MessageType.REQUEST) && msg.getRequestType().equals(RequestType.KEEP_ALIVE)) {
            try {
                sendMessage(new Message(MessageType.RESPONSE, RequestType.KEEP_ALIVE, "Hello world").getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
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


//    private int getPort() {
//        try {
//            ServerSocket ss = new ServerSocket(0);
//            int port = ss.getLocalPort();
//            ss.close();
//            return port;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}

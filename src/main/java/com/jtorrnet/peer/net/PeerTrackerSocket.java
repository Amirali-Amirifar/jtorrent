package com.jtorrnet.peer.net;

import com.jtorrnet.lib.messaging.Message;
import com.jtorrnet.lib.messaging.typing.MessageType;
import com.jtorrnet.lib.messaging.typing.RequestType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class PeerTrackerSocket {

    private final int BUFFER_SIZE = 1024;
    private final DatagramSocket socket;
    private final boolean running;
    private final int DestPort = 3000;
    Thread serverThread;

    public PeerTrackerSocket(String host, int port) throws IOException {
        System.out.println("Connected to " + host + ":" + DestPort);
        this.serverThread = new Thread(this::listen);
        this.running = true;
        this.socket = new DatagramSocket(port, InetAddress.getByName("localhost"));

        Message udpPortMessg = new Message(MessageType.REQUEST, RequestType.KEEP_ALIVE, "initialize connection");
        sendMessage(udpPortMessg.getMessage());


        serverThread.start();
    }

    public void sendMessage(String message) throws IOException {
        DatagramPacket gotoTracker = new DatagramPacket(message.getBytes(), message.getBytes().length,
                InetAddress.getByName("localhost"),
                DestPort);

        socket.send(gotoTracker);
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


}

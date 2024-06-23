package com.jtorrnet.tracker.net.peer_manager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class TrackerOutputManager {
    DatagramSocket socket;
    DatagramPacket packet;

    public TrackerOutputManager(DatagramPacket packet, DatagramSocket socket) {
        this.packet = packet;
        this.socket = socket;
    }


    public void sendMessage(String message) {
        System.out.println("Sending response");
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet1 = new DatagramPacket(bytes, bytes.length, packet.getAddress(), packet.getPort());
        try {
            socket.send(packet1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("sent ");
    }
}

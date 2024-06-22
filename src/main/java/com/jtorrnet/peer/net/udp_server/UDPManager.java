package com.jtorrnet.peer.net.udp_server;

import java.io.IOException;
import java.net.*;

public class UDPManager {
    DatagramSocket socket;
    private static final int BUFFER_SIZE = 256;
    public final int port = getPort();

    public UDPManager() throws SocketException {
        socket = new DatagramSocket(port);
        System.out.println("Made UDPManager available on port " + port);
    }

    public void sendPackets(byte[] bytes, InetAddress address, int port) throws IOException {
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
        socket.send(packet);
    }

    public void sendPackets(byte[] bytes, int port) throws IOException {
        sendPackets(bytes, InetAddress.getByName("localhost"), port);
    }

    public void getPackets() {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, BUFFER_SIZE);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }
    private int getPort() {
        try {
            ServerSocket ss = new ServerSocket(0);
            int port = ss.getLocalPort();
            ss.close();
            return port;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

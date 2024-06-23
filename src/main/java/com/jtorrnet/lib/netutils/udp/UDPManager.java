package com.jtorrnet.lib.netutils.udp;

import java.io.IOException;
import java.net.*;

public abstract class UDPManager {
    private final int PORT;
    private final int BUFFER_SIZE = 1024;
    private DatagramSocket socket;
    Thread listener;
    public UDPManager(int port) throws SocketException {
        this.PORT = port;
        this.socket = new DatagramSocket(port);
        listener = new Thread(this::listen);
        listener.start();
    }

    public void listen() {
        while (true) {
            byte[] buff = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buff, buff.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendPackets(byte[] bytes, InetAddress address, int port) throws IOException {
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
        socket.send(packet);
    }

    public void sendPackets(byte[] bytes, int port) throws IOException {
        sendPackets(bytes, InetAddress.getByName("localhost"), port);
    }


    class UDPmessage {
        DatagramPacket packet;
        String message;
        InetAddress address;
        int port;
    }

}

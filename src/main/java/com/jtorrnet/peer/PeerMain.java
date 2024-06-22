package com.jtorrnet.peer;

import com.jtorrnet.lib.messaging.Message;
import com.jtorrnet.lib.messaging.typing.MessageType;
import com.jtorrnet.lib.messaging.typing.RequestType;
import com.jtorrnet.peer.net.PeerTrackerSocket;
import com.jtorrnet.peer.net.udp_server.UDPManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class PeerMain {
    public static void main(String[] argsv) throws IOException {
        // P2P server
//        runUDPServer();
//        runInputServer();

        // Peer-Tracker handshake.
        // Start Peer client.
        System.out.println("Attemtpting to connect to the peer");
        PeerTrackerSocket socket = new PeerTrackerSocket("127.0.0.1", 3000);

        UDPManager udpManager = new UDPManager();

        Message udpPortMessg = new Message(MessageType.REQUEST, RequestType.UDPPORT, String.valueOf(udpManager.port));
        socket.sendMessage(udpPortMessg.getMessage());


        String command = "";
        Scanner scanner = new Scanner(System.in);

        while (!command.equals("exit")) {
            command = scanner.nextLine();
            command = command.toLowerCase().strip();
            if (command.equals("get_peers")) {
                Message message = new Message(MessageType.REQUEST, RequestType.GET_PEERS, "");
                socket.sendMessage(message.getMessage());

            } else if (command.equals("list_files")) {
                Message message = new Message(MessageType.REQUEST, RequestType.LIST_FILES, "");
                socket.sendMessage(message.getMessage());

            } else if (command.startsWith("share")) {
                Message message = new Message(MessageType.REQUEST, RequestType.SHARE, command.substring(5));
                socket.sendMessage(message.getMessage());

            } else if (command.startsWith("set_name")) {
                Message message = new Message(MessageType.REQUEST, RequestType.SET_NAME, command.split(" ")[1]);
                socket.sendMessage(message.getMessage());
            } else if (command.startsWith("echo")) {
                // echo PORT msg
                String[] args = command.split(" ");
                int port = Integer.parseInt(args[1]);
                String msg = Arrays.toString(Arrays.copyOfRange(args, 2, args.length));
                udpManager.sendPackets(msg.getBytes(StandardCharsets.UTF_8), port);
            } else if (command.startsWith("get")) {
                // get filename.fmt port
                String[] args = command.split(" ");
                String filename = args[1];
                int port = Integer.parseInt(args[2]);


                Message message = new Message(MessageType.REQUEST, RequestType.GET, port + "|"+filename);
                socket.sendMessage(message.getMessage());
            } else {
                System.out.println("Invalid command");
                continue;
            }
            System.out.println("COMMAND: " + command);

        }
    }

//    static void runUDPServer() {
//        udpServer = new UDPServer();
//        try {
//            udpServer.sentPacket("salam23612345".getBytes(StandardCharsets.UTF_8));
//            Thread.sleep(4000);
//            udpServer.sentPacket("salam564".getBytes(StandardCharsets.UTF_8));
//            udpServer.sentPacket("salam2345".getBytes(StandardCharsets.UTF_8));
//            udpServer.sentPacket("salam2345".getBytes(StandardCharsets.UTF_8));
//            System.out.println("Sent packets");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    static void runInputServer() {
//        UDPInputStreamManager udpInputStreamManager = new UDPInputStreamManager(udpServer.socket);
//        udpInputStreamManager.start();
//    }
}

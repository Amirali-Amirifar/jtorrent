package com.jtorrnet.peer;

import com.jtorrnet.lib.messaging.Message;
import com.jtorrnet.lib.messaging.typing.MessageType;
import com.jtorrnet.lib.messaging.typing.RequestType;
import com.jtorrnet.peer.net.PeerTrackerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class PeerMain {
    static int getRandomPort() throws IOException {
        try {
            ServerSocket ss = new ServerSocket(0);
            int port = ss.getLocalPort();
            ss.close();
            System.out.println("Available port is " + port);
            return port;
        } catch (IOException e) {
            throw new IOException("Couldn't find a local port.");
        }
    }

    public static void main(String[] argsv) throws IOException {


        // Peer-Tracker handshake.
        // Start Peer client.

        System.out.println("Attemtpting to connect to the peer");
        PeerTrackerSocket socket = new PeerTrackerSocket("127.0.0.1", getRandomPort());

        // TODO MAKE LOCAL TCP SERVER AND CLIENT.
        //        Message udpPortMessg = new Message(MessageType.REQUEST, RequestType.UDPPORT, String.valueOf(udpManager.port));
        //        socket.sendMessage(udpPortMessg.getMessage());


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
            } else if (command.startsWith("get")) {
                // get filename.fmt port
                String[] args = command.split(" ");
                String filename = args[1];
                int port = Integer.parseInt(args[2]);


                Message message = new Message(MessageType.REQUEST, RequestType.GET, port + "|" + filename);
                socket.sendMessage(message.getMessage());
            } else {
                System.out.println("Invalid command");
            }

        }
    }

}

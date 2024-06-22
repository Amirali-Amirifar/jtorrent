package com.jtorrnet.peer;

import com.jtorrnet.lib.messaging.Message;
import com.jtorrnet.lib.messaging.typing.MessageType;
import com.jtorrnet.lib.messaging.typing.RequestType;
import com.jtorrnet.peer.net.PeerTrackerSocket;

import java.io.IOException;
import java.util.Scanner;

public class PeerMain {
    public static void main(String[] argsv) throws IOException {
        // Peer-Tracker handshake.
        // Start Peer client.
        System.out.println("Attemtpting to connect to the peer");
        PeerTrackerSocket socket = new PeerTrackerSocket("127.0.0.1", 3000);


        String command = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("SALAM");
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

            } else {
                System.out.println("Invalid command");
                continue;
            }
            System.out.println("COMMAND: " + command);

        }
    }
}

package com.jtorrnet.controller;

import com.jtorrnet.Client;
import com.jtorrnet.controller.validation.PeerValidation;
import com.jtorrnet.peer.Peer;

import javax.xml.validation.Validator;
import java.util.Scanner;
import java.util.UUID;

public class CommandLine {
    // Handles the user interactions with the CLI.

    private final Client client;
    private final Scanner scanner = new Scanner(System.in);

    public CommandLine(Client client) {
        this.client = client;
    }

    public void run() {

        String command = "";

        boolean runLoop = true;
        while (runLoop) {
            System.out.print(" > ");
            command = scanner.nextLine();


            switch (command) {
                case "whoami" -> Commands.runWhoami();
                case "run-normal" -> Commands.runNormal(client);
                case "run-tracker" -> Commands.runTracker(client);
                case "help" -> {
                    System.out.println("Available commands:");
                    System.out.println("  whoami \t \t get information about yourself on the network as a peer.");
                    System.out.println("  run-normal \t \t connect to an available tracker peer. ");
                    System.out.println("  run-tracker \t \t run client with tracker enabled.");
                    System.out.println("  help");
                    System.out.println("  exit");
                }
                case "exit" -> runLoop = false;

            }
        }

        System.exit(0);
    }


    private static class Commands {
        private final static Scanner scanner = new Scanner(System.in);
        public static void runTracker(Client client) {
            System.out.println("initializing peer with tracker enabled");


            System.out.println("Enter port number to be used ");
            int portNumber = scanner.nextInt();

            scanner.nextLine();
            System.out.println("Enter ip address to be used ");
            String ipAddress = scanner.nextLine();


            Peer peer = new Peer(UUID.randomUUID(), portNumber, ipAddress, "default");

            if (PeerValidation.validate(peer)) {
                Client.peer = peer;
                client.startTracker(portNumber); //implement
            } else {

                System.out.println("We are sorry but something went wrong.");
            }

        }

        public static void runWhoami() {
            Peer peer = Client.peer;
            if (peer == null) {
                System.out.println("You are not connected to the network.");
            } else {
                System.out.println(peer);
            }
        }

        public static void runNormal(Client client) {
            System.out.println("Enter the tracker address <IP>:<PORT>");
            String address = new Scanner(System.in).nextLine();
            String ip = address.split(":")[0];
            String port = address.split(":")[1];


            client.startPeer(ip, port);


            System.out.println("Initializing the connection. ");
        }
    }
}

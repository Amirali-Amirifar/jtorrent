package com.jtorrnet.controller;

import com.jtorrnet.Client;
import com.jtorrnet.controller.validation.PeerValidation;
import com.jtorrnet.peer.Peer;

import java.util.Scanner;
import java.util.UUID;

public class CommandLine {
    // Handles the user interactions with the CLI.
    public void run() {

        Scanner scanner = new Scanner(System.in);
        String command = "";

        boolean runLoop = true;
        while (runLoop) {
            System.out.print(" > ");
            command = scanner.nextLine();


            switch (command) {
                case "whoami" -> {
                    Commands.runWhoami();
                }

                case "run-tracker" -> {
                    Commands.runTracker();
                }
                case "help" -> {
                    System.out.println("Available commands:");
                    System.out.println("  whoami \t \t get information about yourself on the network as a peer.");
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
        public static void runTracker() {
            System.out.println("initializing peer with tracker enabled");

            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter port number to be used ");
            int portNumber = scanner.nextInt();

            scanner.nextLine();
            System.out.println("Enter ip address to be used ");
            String ipAddress = scanner.nextLine();


            Peer peer = new Peer(UUID.randomUUID(), portNumber, ipAddress, "default");

            if (PeerValidation.validate(peer)) {
                Client.peer = peer;
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
    }


}

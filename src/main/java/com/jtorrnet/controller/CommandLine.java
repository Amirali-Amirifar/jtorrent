package com.jtorrnet.controller;

import java.util.Scanner;

public class CommandLine {
    // Handles the user interactions with the CLI.
    public void run() {

        Scanner scanner = new Scanner(System.in);
        String command;

        loop:
        while (true) {
            System.out.print(" > ");
            command = scanner.nextLine();


            switch (command) {
                case "help" -> {
                    System.out.println("Available commands:");
                    System.out.println("  help");
                    System.out.println("  exit");
                }
                case "exit" -> {
                    break loop;
                }
            }
        }

        System.exit(0);
    }

}

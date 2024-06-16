package com.jtorrnet;

import com.jtorrnet.controller.CommandLine;
import com.jtorrnet.peer.Peer;
import com.jtorrnet.peer.Server;
import com.jtorrnet.tracker.Tracker;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * This class handles the state management of the client;
 */
public class Client {

    public static Peer peer;
    public static Tracker tracker = null;


    public Client() {
        CommandLine commandLine = new CommandLine(this);
        commandLine.run();
    }

    public void startPeer(String ip, String port) {
        try {
            Socket socket = new Socket(ip, Integer.parseInt(port));
//            PrintWriter pw = new PrintWriter(s.getOutputStream());
//            pw.println("I am connected to " + ip + ":" + port);
            OutputStream out = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(out);

            printWriter.println(socket);
            printWriter.flush();
            printWriter.close();

            socket.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("Connection established");
        }
        // Start listen
    }

    public void startTracker(final int PORT) {
        // Start tracking server.
        try {
            Server s =new Server(PORT);
            Thread t = new Thread(s);
            t.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

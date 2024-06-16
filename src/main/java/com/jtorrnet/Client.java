package com.jtorrnet;

import com.jtorrnet.controller.CommandLine;
import com.jtorrnet.models.message.Message;
import com.jtorrnet.models.message.MessageTypes;
import com.jtorrnet.peer.Peer;
import com.jtorrnet.peer.Server;
import com.jtorrnet.tracker.Tracker;

import java.io.*;
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
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);

            Message msg = new Message(MessageTypes.LIST_FILES, "Hello I am from local port " + socket.getLocalAddress());

            objectOutputStream.writeObject(msg);
            objectOutputStream.flush();


            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object obj = objectInputStream.readObject();
            msg = (Message) obj;
            System.out.println(msg.getText());
            socket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("message sent.");
        }
        // Start listen
    }

    public void startTracker(final int PORT) {
        // Start tracking server.
        tracker = new Tracker();

        try {
            Server s =new Server(PORT);
            s.setTracker(this.tracker);
            Thread t = new Thread(s);
            t.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

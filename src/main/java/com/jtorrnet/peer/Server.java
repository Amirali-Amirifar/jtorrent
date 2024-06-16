package com.jtorrnet.peer;

import com.jtorrnet.models.message.Message;
import com.jtorrnet.models.message.MessageTypes;
import com.jtorrnet.tracker.Tracker;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {


    ServerSocket serverSocket;
    Tracker tracker;
    boolean running = true;

    public Server(final int PORT) throws IOException {
        serverSocket = new ServerSocket(PORT);
    }

    @Override
    public void run() {
        System.out.println("Starting the server ...");
        while (running) {
            try {
                Socket socket = serverSocket.accept();


                InputStream in = socket.getInputStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                ObjectInputStream objectInputStream = new ObjectInputStream(in);
                Object obj = objectInputStream.readObject();
                Message msg = (Message) obj;

                System.out.println("Recieved MessageType " + msg.getType());
                if (msg.getType().equals(MessageTypes.ECHO)) {
                    System.out.println(msg.getText());
                }


                if (msg.getType().equals(MessageTypes.LIST_FILES)) {
                    String text = tracker.getFileNames();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(new Message(MessageTypes.LIST_FILES, text));

                }
//                String text = reader.readLine();
//                System.out.println("PEER MSG: "+text);


                in.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Server out of run");
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }
}

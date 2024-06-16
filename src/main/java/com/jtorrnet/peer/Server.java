package com.jtorrnet.peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{


    ServerSocket serverSocket;
    boolean running = true;
    public Server(final int PORT) throws IOException {
        serverSocket = new ServerSocket(PORT);
    }

    @Override
    public void run() {
        System.out.println("Starting the server ...");
        while(running) {
            try {
                Socket socket = serverSocket.accept();


                InputStream in = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String text = reader.readLine();
                System.out.println("PEER MSG: "+text);
                in.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Server out of run");
    }
}

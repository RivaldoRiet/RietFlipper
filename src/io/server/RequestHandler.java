package io.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class RequestHandler implements Runnable {

    private boolean running;
    private ServerSocket socket;

    public RequestHandler(ServerSocket socket) {
        this.socket = socket;
        this.running = true;
    }

    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        System.out.println("Server is listening to connections!");
        while(this.running) {
            try {
                Socket incoming = this.socket.accept();
                onNewConnection(incoming);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void onNewConnection(Socket socket);
}

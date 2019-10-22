package io;

import io.protocol.listener.impl.server.EndConnectionListener;
import io.server.IOServer;


public class ServerSimulator {

    public static void main(String... args) {
        try {
            IOServer server = new IOServer(43594)
                    .addListeners(new EndConnectionListener())
                    .setDebug(false)
                    .startListening("hey");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

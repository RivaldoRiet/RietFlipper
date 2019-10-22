package io;

import io.client.IOClient;
import io.protocol.listener.impl.client.ShutdownListener;

import java.io.IOException;

public class ClientSimulator {

    public static void main(String... args) throws IOException {
        IOClient client = new IOClient("UID-id",43594)
                .addListeners(new ShutdownListener())
                .startYelling("khb");
    }
}

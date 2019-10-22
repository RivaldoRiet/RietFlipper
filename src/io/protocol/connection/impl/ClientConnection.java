package io.protocol.connection.impl;

import io.protocol.Packet;
import io.protocol.connection.AbstractConnection;
import io.protocol.listener.AbstractInputListener;
import io.protocol.stream.impl.ClientStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ClientConnection extends AbstractConnection<ClientStream>  {

    private List<AbstractInputListener<ClientConnection>> listeners;

    @Override
    public void addListener(AbstractInputListener l) {
        this.listeners.add(l);
    }

    public ClientConnection(ClientStream stream) {
        super(stream);
        listeners = new ArrayList<>();
        onAccept(stream);

    }

    @Override
    public void run() {
        System.out.println("Connected with the server");
        while(super.getStream().isOpen()) {
            try {
                System.out.println("Reading");
                Packet in = getStream().readJSON();
                System.out.println("received: " + in.toString());
                Optional<AbstractInputListener<ClientConnection>> listener = listeners.stream()
                        .filter(l -> l.getId() == in.getId())
                        .findFirst();
                if (listener.isPresent()) {
                    listener.get().onReceived(in, this);
                } else {
                    System.out.println("No listener found for packet #" + in.getId());
                }
            } catch (IOException e) {
                isConnected = false;
            }
        }
    }
}

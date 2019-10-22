package io.protocol.listener;

import io.protocol.Packet;
import io.protocol.connection.AbstractConnection;

public abstract class AbstractInputListener<E extends AbstractConnection> {

    private int id;

    public AbstractInputListener(int id) {
        this.id = id;

    }

    public int getId() {
        return id;
    }

    public abstract void onReceived(Packet in, E connection);

}

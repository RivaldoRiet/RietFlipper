package io.protocol.connection;

import io.protocol.listener.AbstractInputListener;
import io.protocol.stream.AbstractStream;

public abstract class AbstractConnection<T extends AbstractStream> implements Runnable {

    private T stream;
    protected boolean isConnected;

    public AbstractConnection(T stream) {
        this.stream = stream;
    }

    public abstract void addListener(AbstractInputListener l);

    public T getStream() {
        return stream;
    }

    public boolean isConnected() {
        return getStream().isOpen();
    }

    protected abstract void onAccept(T stream);


}

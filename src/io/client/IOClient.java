package io.client;

import io.protocol.connection.impl.ClientConnection;
import io.protocol.listener.AbstractInputListener;
import io.protocol.stream.impl.ClientStream;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class IOClient {

    private String address;
    private String uid;
    private int port;
    private Socket socket;
    private Executor executor;
    private boolean debug = true;

    private ClientConnection connection;

    private List<AbstractInputListener<ClientConnection>> listeners;

    public IOClient(String uid, int port) {
        this("localhost",port,uid);
    }

    public IOClient(String address, int port, String uid) {
        this.address = address;
        this.port= port;
        this.uid = uid;
        this.executor = Executors.newSingleThreadExecutor();
        this.listeners = new ArrayList<>();
    }

    public ClientConnection getConnection() {
        return connection;
    }

    public IOClient addListeners(AbstractInputListener<ClientConnection>... listeners) {
        Collections.addAll(this.listeners,listeners);
        return this;
    }

    public IOClient startYelling(String password) {
        try {
            connection = new ClientConnection(new ClientStream(socket = new Socket(this.address, this.port))) {
                @Override
                public void onAccept(ClientStream stream) {
                        listeners.forEach(this::addListener);
                        isConnected = true;
                        stream.setDebug(debug);
                }
            };
            executor.execute(connection);
            connection.getStream().authenticate(password,this.uid);
        } catch (IOException e) {
           System.out.println("Error connecting to server");
        }
        return this;
    }

    public IOClient setDebug(boolean on) {
        this.debug = on;
        return this;
    }
}

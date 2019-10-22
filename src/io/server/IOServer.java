package io.server;

import io.protocol.connection.impl.ServerConnection;
import io.protocol.listener.AbstractInputListener;
import io.protocol.stream.impl.ServerStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public  class IOServer {

    private int port;

    private ServerSocket serverSocket;
    private Set<AbstractInputListener<ServerConnection>> listeners;

    private ExecutorService executor;
    private RequestHandler requestHandler;

    private boolean debug = true;

    private HashMap<String,ServerConnection> connections;

    public IOServer(int port) {
        this.port = port;
        this.executor = Executors.newCachedThreadPool();
        this.connections = new HashMap<>();
        this.listeners = new HashSet<>();
    }

    public void stop() {
        requestHandler.stop();
        getOpenConnections().forEach(c -> c.getStream().close());
        connections.clear();
    }

    public IOServer addListeners(AbstractInputListener<ServerConnection>... listeners) {
        Collections.addAll(this.listeners,listeners);
        return this;
    }

    public IOServer setDebug(boolean on) {
        this.debug = on;
        return this;
    }

    public IOServer startListening(String passphrase) throws IOException {
        executor.submit(requestHandler = new RequestHandler(serverSocket = new ServerSocket(port)) {
            @Override
            public void onNewConnection(Socket socket) {
                try {
                    new ServerConnection(passphrase, new ServerStream(socket)) {
                        @Override
                        public void onValidation(ServerConnection connection, String uid) {
                            connections.put(uid,connection);
                            for (AbstractInputListener listener : listeners) {
                                connection.addListener(listener);
                            }
                            connection.getStream().setDebug(debug);
                            executor.submit(connection);
                            System.out.println("Client connected: " + connection.toString());
                        }
                    };
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return this;
    }

    public int getListeningPort() {
        return port;
    }

    public List<ServerConnection> getOpenConnections() {
        return this.connections.values().stream().filter(ServerConnection::isConnected).collect(Collectors.toList());
    }

    public Optional<ServerConnection> getConnection(String uid) {
        return getOpenConnections().stream().filter(c -> c.equals(connections.get(uid)) && c.isConnected()).findFirst();
    }

    public Set<String> getOpenUIDS() {
        return connections.keySet();
    }

}

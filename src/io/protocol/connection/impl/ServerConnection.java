package io.protocol.connection.impl;

import io.protocol.Packet;
import io.protocol.connection.AbstractConnection;
import io.protocol.json.JsonObject;
import io.protocol.listener.AbstractInputListener;
import io.protocol.stream.impl.ServerStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class ServerConnection extends AbstractConnection<ServerStream>  {

    private String password;
    private String uid;

    private List<AbstractInputListener<ServerConnection>> listeners;

    @Override
    public void addListener(AbstractInputListener l) {
        listeners.add(l);
    }

    public ServerConnection(String password, ServerStream stream) {
        super(stream);
        this.password = password;
        this.listeners = new ArrayList<>();
        onAccept(stream);
    }

    protected abstract void onValidation(ServerConnection connection, String uid);

    @Override
    protected void onAccept(ServerStream stream) {
        try {
           Packet inPass = stream.readJSON();
            JsonObject payload = inPass.getPayload();
            String pw = payload.get("pass").asString();
            if (pw != null && Objects.equals(pw,this.password)) {
                super.isConnected = true;
                this.uid = payload.get("uid").asString();
                onValidation(this, this.uid);
            }
            else {
                getStream().sendShutdown("Wrong login pass");
                System.out.println("denied connection from: " + stream.getIP());
                this.getStream().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isConnected) {
            try {
                Packet in = getStream().readJSON();
                Optional<AbstractInputListener<ServerConnection>> listener = listeners.stream()
                        .filter(l -> l.getId() == in.getId())
                        .findFirst();
                if (listener.isPresent()) {
                    listener.get().onReceived(in, this);
                } else  System.out.println("No listener found for packet #" + in.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("We got removed!");
    }

    @Override
    public String toString() {
        return "|" + uid + "|" + getStream().getIP() + "|";
    }

    public String getUID()  {
        return uid;
    }
}

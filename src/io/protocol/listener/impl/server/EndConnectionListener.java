package io.protocol.listener.impl.server;

import io.protocol.Packet;
import io.protocol.connection.impl.ServerConnection;
import io.protocol.json.JsonObject;
import io.protocol.listener.AbstractInputListener;

public class EndConnectionListener extends AbstractInputListener<ServerConnection> {

    public EndConnectionListener() {
        super(-1);
    }

    @Override
    public void onReceived(Packet in, ServerConnection connection) {
        JsonObject payload = in.getPayload();
        String reason = payload.get("message").asString();
        System.out.println("Client #"+connection.getUID()+" disconnected: " + reason);
        connection.getStream().close();
    }
}

package io.protocol.listener.impl.client;

import io.protocol.Packet;
import io.protocol.connection.impl.ClientConnection;
import io.protocol.json.JsonObject;
import io.protocol.listener.AbstractInputListener;

public class ShutdownListener extends AbstractInputListener<ClientConnection> {

    public ShutdownListener() {
        super(-1);
    }

    @Override
    public void onReceived(Packet in, ClientConnection connection) {
        JsonObject payload = in.getPayload();
        String reason = payload.get("message").asString();
        System.out.println("Connection closed! Reason: " + reason);
        connection.getStream().close();
    }
}

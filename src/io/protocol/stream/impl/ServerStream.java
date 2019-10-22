package io.protocol.stream.impl;

import io.protocol.Packet;
import io.protocol.json.JsonObject;
import io.protocol.stream.AbstractStream;

import java.io.IOException;
import java.net.Socket;

public class ServerStream extends AbstractStream {

    public ServerStream(Socket socket) throws IOException {
        super(socket);
    }

    /**
     * Write your custom methods here
     */

    public void sendShutdown(String reason) throws IOException{
        JsonObject object = new JsonObject();
        object.add("message", reason);
        writeJSON(new Packet(1,object));
    }

}

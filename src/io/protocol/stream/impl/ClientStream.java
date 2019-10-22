package io.protocol.stream.impl;

import io.protocol.Packet;
import io.protocol.json.JsonObject;
import io.protocol.stream.AbstractStream;

import java.io.IOException;
import java.net.Socket;

public class ClientStream extends AbstractStream {

    public ClientStream(Socket socket) throws IOException {
        super(socket);
    }

    /**
     * Write your custom methods here
     */
    public void authenticate(String pass,String uid) throws IOException {
        JsonObject object = new JsonObject();
        object.add("pass",pass);
        object.add("uid",uid);
        writeJSON(new Packet(-1,object));
    }
}

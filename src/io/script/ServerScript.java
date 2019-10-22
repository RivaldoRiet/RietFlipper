package io.script;

import io.server.IOServer;
import org.dreambot.api.script.AbstractScript;

public abstract class ServerScript extends AbstractScript{


    private IOServer server;

    @Override
    public void onStart() {
        onStart(server);
    }

    public abstract void onStart(IOServer server);


    public IOServer getIOServer() {
        return server;
    }
}

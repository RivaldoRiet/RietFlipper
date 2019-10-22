package io.script;

import io.client.IOClient;
import io.server.IOServer;
import org.dreambot.api.script.AbstractScript;

public abstract class DualScript extends AbstractScript {

    private IOClient client;
    private IOServer server;

    @Override
    public void onStart() {
        onStart(client,server);
    }

    public abstract void onStart(IOClient client, IOServer server);

    public IOClient getIOClient() {
        return client;
    }

    public IOServer getIOServer() {
        return server;
    }
}

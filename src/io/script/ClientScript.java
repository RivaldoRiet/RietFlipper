package io.script;

import io.client.IOClient;
import org.dreambot.api.script.AbstractScript;

public abstract class ClientScript extends AbstractScript {

    private IOClient client;

    @Override
    public void onStart() {
        onStart(client);
    }

    public abstract void onStart(IOClient client);

    public IOClient getIOClient() {
        return client;
    }
}

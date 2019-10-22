package io.protocol.json;

import org.dreambot.api.methods.MethodContext;

public abstract class ContextualJsonSupplier {

    private MethodContext db;

    public ContextualJsonSupplier(MethodContext context) {
        this.db = context;
    }

    public abstract JsonObject create(JsonObject json);

    public JsonObject getObject() {
        return create(new JsonObject()).asObject();
    }

}

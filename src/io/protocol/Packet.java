package io.protocol;

import io.protocol.json.Json;
import io.protocol.json.JsonObject;
import io.protocol.json.WriterConfig;

public class Packet {

    private int id;
    private JsonObject payload;

    public Packet(int id, JsonObject payload) {
        this.id = id;
        this.payload = payload;
        this.payload.add("id", id);
    }

    public int getId() {
        return id;
    }

    public JsonObject getPayload() {
        return payload;
    }

    public String getRawPayload() {
        return Json.parse(payload.toString()).toString(WriterConfig.MINIMAL);//payload.asString();
    }

    public static Packet of(String payload) {
        JsonObject object = Json.parse(payload).asObject();
        return new Packet(object.get("id").asInt(), object);
    }

    @Override
    public String toString() {
        return "[#"+id+" ("+payload.toString()+")]";
    }
}

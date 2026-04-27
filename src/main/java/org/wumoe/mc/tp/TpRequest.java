package org.wumoe.mc.tp;

import java.util.UUID;

public class TpRequest {
    public enum Type {
        TPA,
        TPAHERE
    }

    public final UUID from;
    public final UUID to;
    public final Type type;
    public final long time;

    public TpRequest(UUID from, UUID to, Type type) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.time = System.currentTimeMillis();
    }
}

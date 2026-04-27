package org.wumoe.mc.player;

import org.bukkit.entity.Player;
import org.wumoe.mc.utils.PropertiesUtil;

import java.util.Objects;
import java.util.UUID;

public class PlayerManager {
    private final PropertiesUtil config;

    public PlayerManager(PropertiesUtil config) {
        this.config = config;
    }

    public void update(Player player) {
        UUID uuid = player.getUniqueId();
        String name = player.getName().toLowerCase();
        this.config.set("uuid." + name.toLowerCase(), uuid.toString());
        this.config.set("name." + uuid, player.getName());
        long now = System.currentTimeMillis();
        if (!config.contains("player." + uuid + ".first_join"))
            config.set("player." + uuid + ".first_join", String.valueOf(now));
        this.config.set("player." + uuid + ".last_join", String.valueOf(now));
        this.config.set("player." + uuid + ".last_ip", Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress());
        this.config.save();
    }

    public void onQuit(Player player) {
        UUID uuid = player.getUniqueId();
        this.config.set("player." + uuid + ".last_quit", String.valueOf(System.currentTimeMillis()));
        this.config.save();
    }

    public long getFirstJoin(UUID uuid) {
        return getLong("player." + uuid + ".first_join");
    }

    public long getLastJoin(UUID uuid) {
        return getLong("player." + uuid + ".last_join");
    }

    public long getLastQuit(UUID uuid) {
        return getLong("player." + uuid + ".last_quit");
    }

    private long getLong(String key) {
        String v = this.config.get(key);
        return v == null ? -1 : Long.parseLong(v);
    }

    public UUID getUUID(String name) {
        String value = this.config.get("uuid." + name.toLowerCase());
        if (value == null) return null;
        try {
            return UUID.fromString(value);
        } catch (Exception e) {
            return null;
        }
    }

    public String getName(UUID uuid) {
        return this.config.get("name." + uuid);
    }

    public String getIP(UUID uuid) {
        return this.config.get("player." + uuid + ".last_ip");
    }
}

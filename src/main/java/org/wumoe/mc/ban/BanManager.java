package org.wumoe.mc.ban;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.wumoe.mc.player.PlayerManager;
import org.wumoe.mc.utils.PropertiesUtil;

import java.util.UUID;

public class BanManager {
    public final PlayerManager player;
    private final PropertiesUtil config;

    public BanManager(PropertiesUtil config, PlayerManager player) {
        this.config = config;
        this.player = player;
    }

    public void ban(UUID uuid, String reason) {
        this.config.set("ban." + uuid, "true");
        this.config.set("ban." + uuid + ".reason", reason);
        this.config.set("ban." + uuid + ".time", String.valueOf(System.currentTimeMillis()));
        this.config.save();
    }

    public void unban(UUID uuid) {
        this.config.remove("ban." + uuid);
        this.config.remove("ban." + uuid + ".reason");
        this.config.remove("ban." + uuid + ".time");
        this.config.save();
    }

    public boolean isBanned(UUID uuid) {
        return this.config.contains("ban." + uuid);
    }

    public String getReason(UUID uuid) {
        return this.config.get("ban." + uuid + ".reason", "Banned");
    }

    public long getBanTime(UUID uuid) {
        String v = this.config.get("ban." + uuid + ".time");
        return v == null ? -1 : Long.parseLong(v);
    }

    public void kickIfBanned(Player player) {
        UUID uuid = player.getUniqueId();
        if (isBanned(uuid))
            player.kick(Component.text("§c你已被封禁！\n§7原因: " + getReason(uuid)));
    }
}

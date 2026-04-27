package org.wumoe.mc.ban.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.wumoe.mc.ban.BanManager;

public class BanListener implements Listener {
    private final BanManager manager;

    public BanListener(BanManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.manager.kickIfBanned(event.getPlayer());
    }
}

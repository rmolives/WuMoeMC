package org.wumoe.mc.player.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.wumoe.mc.player.PlayerManager;

public class PlayerListener implements Listener {
    private final PlayerManager manager;

    public PlayerListener(PlayerManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.manager.update(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.manager.onQuit(event.getPlayer());
    }
}

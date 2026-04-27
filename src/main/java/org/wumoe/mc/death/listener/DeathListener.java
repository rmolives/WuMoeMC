package org.wumoe.mc.death.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.wumoe.mc.death.DeathManager;

public class DeathListener implements Listener {
    private final DeathManager manager;

    public DeathListener(DeathManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        this.manager.recordDeath(player);
        player.sendMessage("§e你死亡了，输入 §a/back §e返回死亡地点");
    }
}

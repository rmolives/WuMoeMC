package org.wumoe.mc.death.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        player.sendMessage(
                Component.text("你死亡了，输入 ", NamedTextColor.YELLOW)
                        .append(Component.text("/back", NamedTextColor.GREEN))
                        .append(Component.text(" 返回死亡地点", NamedTextColor.YELLOW)));
    }
}

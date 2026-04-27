package org.wumoe.mc.death.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.death.DeathManager;

public class DeathCommand implements CommandExecutor {
    private final DeathManager manager;

    public DeathCommand(DeathManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        Location loc = this.manager.getDeathLocation(player);
        if (loc == null) {
            sender.sendMessage(Component.text("没有死亡记录", NamedTextColor.RED));
            return true;
        }
        player.teleport(loc);
        player.sendMessage(Component.text("已返回死亡地点", NamedTextColor.GREEN));
        return true;
    }
}

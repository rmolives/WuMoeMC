package org.wumoe.mc.death.command;

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
            sender.sendMessage("只有玩家可以使用");
            return true;
        }
        Location loc = this.manager.getDeathLocation(player);
        if (loc == null) {
            player.sendMessage("§c没有死亡记录");
            return true;
        }
        player.teleport(loc);
        player.sendMessage("§a已返回死亡地点");
        return true;
    }
}

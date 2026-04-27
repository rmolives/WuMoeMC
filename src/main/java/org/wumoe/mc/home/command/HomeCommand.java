package org.wumoe.mc.home.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.home.HomeManager;

public class HomeCommand implements CommandExecutor {
    private final HomeManager manager;

    public HomeCommand(HomeManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("只有玩家可以使用");
            return true;
        }
        if (!this.manager.hasHome(player)) {
            player.sendMessage("§c你还没有设置家！");
            return true;
        }
        Location home = this.manager.getHome(player);
        if (home == null) {
            player.sendMessage("§cHome 数据损坏！");
            return true;
        }
        player.teleport(home);
        player.sendMessage("§a已传送回家！");
        return true;
    }
}

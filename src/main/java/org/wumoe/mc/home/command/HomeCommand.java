package org.wumoe.mc.home.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        if (!this.manager.hasHome(player)) {
            player.sendMessage(Component.text("你还没有设置家！", NamedTextColor.RED));
            return true;
        }
        Location home = this.manager.getHome(player);
        if (home == null) {
            player.sendMessage(Component.text("Home数据损坏！", NamedTextColor.RED));
            return true;
        }
        player.teleport(home);
        player.sendMessage(Component.text("已传送回家！", NamedTextColor.GREEN));
        return true;
    }
}

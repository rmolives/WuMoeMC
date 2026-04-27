package org.wumoe.mc.home.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.home.HomeManager;

public class DelHomeCommand implements CommandExecutor {
    private final HomeManager manager;

    public DelHomeCommand(HomeManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        boolean success = this.manager.deleteHome(player);
        if (!success) {
            player.sendMessage(Component.text("你还没有设置家！", NamedTextColor.RED));
            return true;
        }
        player.sendMessage(Component.text("Home已删除！", NamedTextColor.GREEN));
        return true;
    }
}

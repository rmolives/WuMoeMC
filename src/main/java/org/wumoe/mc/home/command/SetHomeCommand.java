package org.wumoe.mc.home.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.home.HomeManager;

public class SetHomeCommand implements CommandExecutor {
    private final HomeManager manager;

    public SetHomeCommand(HomeManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("只有玩家可以使用");
            return true;
        }
        this.manager.setHome(player);
        player.sendMessage("§aHome 设置成功！");
        return true;
    }
}

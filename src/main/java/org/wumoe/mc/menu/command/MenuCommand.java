package org.wumoe.mc.menu.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.menu.MenuManager;

public class MenuCommand implements CommandExecutor {
    private final MenuManager manager;

    public MenuCommand(MenuManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c只有玩家可以使用");
            return true;
        }
        this.manager.open(player, "main");
        return true;
    }
}

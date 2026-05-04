package org.wumoe.mc.menu.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        if (args.length == 1) {
            if ("reload".equals(args[0])) {
                this.manager.reload();
                sender.sendMessage(Component.text("已刷新菜单", NamedTextColor.GREEN));
                return true;
            }
            return false;
        }
        if (args.length != 0)
            return false;
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        this.manager.open(player, "main");
        return true;
    }
}

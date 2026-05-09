package org.wumoe.mc.wrap.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.wrap.WarpManager;

public class DelWarpCommand implements CommandExecutor {
    private final WarpManager manager;

    public DelWarpCommand(WarpManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        if (args.length != 1) return false;
        if (!args[0].matches("[A-Za-z0-9]+")) {
            player.sendMessage(Component.text("仅支持纯英文和数字", NamedTextColor.RED));
            return true;
        }
        if (this.manager.deleteWarp(player, args[0]))
            player.sendMessage(Component.text("删除成功", NamedTextColor.GREEN));
        else
            player.sendMessage(Component.text("传送点不存在", NamedTextColor.RED));
        return true;
    }
}

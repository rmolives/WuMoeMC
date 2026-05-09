package org.wumoe.mc.wrap.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.wrap.WarpManager;

public class SetWarpCommand implements CommandExecutor {
    private final WarpManager manager;

    public SetWarpCommand(WarpManager manager) {
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
        this.manager.setWarp(player, args[0], player.getLocation());
        player.sendMessage(Component.text("设置成功", NamedTextColor.GREEN));
        return true;
    }
}

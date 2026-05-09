package org.wumoe.mc.wrap.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.wrap.WarpManager;

import java.util.List;

public class WarpsCommand implements CommandExecutor {
    private final WarpManager manager;

    public WarpsCommand(WarpManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        List<String> warps = this.manager.getWarps(player);
        if (warps.isEmpty()) {
            player.sendMessage(Component.text("未设置任何传送点", NamedTextColor.RED));
            return true;
        }
        player.sendMessage(Component.text("传送点：", NamedTextColor.YELLOW));
        for (String w : warps)
            player.sendMessage(Component.text("- " + w, NamedTextColor.GREEN));
        return true;
    }
}

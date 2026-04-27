package org.wumoe.mc.chunk.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Chunk;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class ChunkLoadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        if (args.length == 0) return false;
        Chunk chunk = player.getLocation().getChunk();
        switch (args[0].toLowerCase()) {
            case "on" -> {
                chunk.setForceLoaded(true);
                player.sendMessage(Component.text("已锁定区块加载", NamedTextColor.GREEN));
            }
            case "off" -> {
                chunk.setForceLoaded(false);
                player.sendMessage(Component.text("已取消区块加载", NamedTextColor.YELLOW));
            }
            case "check" -> {
                boolean force = chunk.isForceLoaded();
                player.sendMessage(Component.text("区块状态:", NamedTextColor.YELLOW));
                player.sendMessage(
                        Component.text("坐标: ", NamedTextColor.GRAY)
                                .append(Component.text(chunk.getX() + ", " + chunk.getZ(), NamedTextColor.WHITE)));
                player.sendMessage(
                        Component.text("强加载: ", NamedTextColor.GRAY)
                                .append(Component.text(force ? "是" : "否",
                                        force ? NamedTextColor.GREEN : NamedTextColor.RED)));
            }
            default -> {
                return false;
            }
        }
        return true;
    }
}

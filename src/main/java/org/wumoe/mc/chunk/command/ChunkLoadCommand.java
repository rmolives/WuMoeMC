package org.wumoe.mc.chunk.command;

import org.bukkit.Chunk;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.chunk.ChunkLoadManager;

public class ChunkLoadCommand implements CommandExecutor {
    private final ChunkLoadManager manager;

    public ChunkLoadCommand(ChunkLoadManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c只有玩家可以使用");
            return true;
        }
        if (args.length == 0) return false;
        Chunk chunk = player.getLocation().getChunk();
        switch (args[0].toLowerCase()) {
            case "on" -> {
                manager.add(chunk);
                player.sendMessage("§a已锁定区块加载");
            }
            case "off" -> {
                manager.remove(chunk);
                player.sendMessage("§e已取消区块加载");
            }
            case "check" -> {
                boolean force = chunk.isForceLoaded();
                player.sendMessage("§e区块状态:");
                player.sendMessage("§7坐标: " + chunk.getX() + ", " + chunk.getZ());
                player.sendMessage("§7强加载: " + (force ? "§a是" : "§c否"));
            }
            default -> {
                player.sendMessage("§c用法: /chunkload on|off");
                return false;
            }
        }
        return true;
    }
}

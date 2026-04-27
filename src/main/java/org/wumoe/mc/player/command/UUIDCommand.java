package org.wumoe.mc.player.command;

import org.bukkit.command.*;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.player.PlayerManager;

import java.util.UUID;

public class UUIDCommand implements CommandExecutor {
    private final PlayerManager manager;

    public UUIDCommand(PlayerManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {
        if (args.length != 1) return false;
        UUID uuid = this.manager.getUUID(args[0]);
        if (uuid == null) {
            sender.sendMessage("§c未找到该玩家记录！");
            return true;
        }
        sender.sendMessage("§e玩家信息:");
        sender.sendMessage("§aName: §f" + args[0]);
        sender.sendMessage("§aUUID: §f" + uuid);
        return true;
    }
}

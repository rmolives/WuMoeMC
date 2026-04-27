package org.wumoe.mc.player.command;

import org.bukkit.command.*;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.player.PlayerManager;

import java.util.UUID;

public class NameCommand implements CommandExecutor {

    private final PlayerManager manager;

    public NameCommand(PlayerManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {
        if (args.length != 1) return false;
        try {
            UUID uuid = UUID.fromString(args[0]);
            String name = this.manager.getName(uuid);
            if (name == null) {
                sender.sendMessage("§c未找到该 UUID 对应玩家！");
                return true;
            }
            sender.sendMessage("§e玩家信息:");
            sender.sendMessage("§aName: §f" + name);
            sender.sendMessage("§aUUID: §f" + args[0]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cUUID格式错误！");
        }
        return true;
    }
}

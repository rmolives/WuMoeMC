package org.wumoe.mc.player.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            sender.sendMessage(Component.text("未找到该 Name 对应玩家！", NamedTextColor.RED));
            return true;
        }
        sender.sendMessage(Component.text("玩家信息:", NamedTextColor.YELLOW));
        sender.sendMessage(
                Component.text("Name: ", NamedTextColor.GREEN)
                        .append(Component.text(args[0], NamedTextColor.WHITE)));
        sender.sendMessage(
                Component.text("UUID: ", NamedTextColor.GREEN)
                        .append(Component.text(uuid.toString(), NamedTextColor.WHITE)));
        return true;
    }
}

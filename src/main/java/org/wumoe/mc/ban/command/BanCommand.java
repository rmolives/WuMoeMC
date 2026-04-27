package org.wumoe.mc.ban.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.ban.BanManager;
import org.wumoe.mc.utils.PlayerUtil;

import java.util.UUID;

public class BanCommand implements CommandExecutor {
    private final BanManager manager;

    public BanCommand(BanManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {
        if (args.length < 1) return false;
        if (sender instanceof Player player)
            if (!PlayerUtil.isHavePermission(player))
                return true;
        String name = args[0];
        UUID uuid = this.manager.player.getUUID(name);
        String reason = args.length >= 2
                ? String.join(" ", args).substring(name.length()).trim()
                : "Banned";
        this.manager.ban(uuid, reason);
        Player online = Bukkit.getPlayer(uuid);
        if (online != null)
            online.kick(
                    Component.text("你已被封禁！", NamedTextColor.RED)
                            .append(Component.newline())
                            .append(Component.text("原因: " + reason, NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("已封禁玩家 " + name, NamedTextColor.GREEN));
        return true;
    }
}

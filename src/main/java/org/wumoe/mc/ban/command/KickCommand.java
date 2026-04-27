package org.wumoe.mc.ban.command;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.ban.BanManager;
import org.wumoe.mc.utils.PlayerUtil;

import java.util.UUID;

public class KickCommand implements CommandExecutor {
    private final BanManager manager;

    public KickCommand(BanManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {
        if (args.length < 1) return false;
        if (sender instanceof Player player)
            if (!PlayerUtil.isHavePermission(player))
                return true;
        UUID uuid = this.manager.player.getUUID(args[0]);
        Player target = Bukkit.getPlayer(uuid);
        if (target == null) {
            sender.sendMessage("§c玩家不存在！");
            return true;
        }
        String reason = args.length >= 2 ? String.join(" ", args).substring(args[0].length()).trim() : "Kicked";
        target.kick(Component.text("§c你被踢出服务器！\n§7原因: " + reason));
        sender.sendMessage("§a已踢出玩家 " + target.getName());
        return true;
    }
}

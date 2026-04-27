package org.wumoe.mc.ban.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.ban.BanManager;
import org.wumoe.mc.utils.PlayerUtil;

import java.util.UUID;

public class UnbanCommand implements CommandExecutor {
    private final BanManager manager;

    public UnbanCommand(BanManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {
        if (args.length != 1) return false;
        if (sender instanceof Player player)
            if (!PlayerUtil.isHavePermission(player))
                return true;
        String name = args[0];
        UUID uuid = this.manager.player.getUUID(name);
        if (uuid == null) {
            sender.sendMessage("§c该玩家从未加入过服务器！");
            return true;
        }
        if (!this.manager.isBanned(uuid)) {
            sender.sendMessage("§c该玩家没有被封禁！");
            return true;
        }
        this.manager.unban(uuid);
        sender.sendMessage("§a已解封玩家 " + name);
        return true;
    }
}

package org.wumoe.mc.ban.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            sender.sendMessage(Component.text("该玩家从未加入过服务器！", NamedTextColor.RED));
            return true;
        }
        if (!this.manager.isBanned(uuid)) {
            sender.sendMessage(Component.text("该玩家没有被封禁！", NamedTextColor.RED));
            return true;
        }
        this.manager.unban(uuid);
        sender.sendMessage(Component.text("已解封玩家 " + name, NamedTextColor.GREEN));
        return true;
    }
}

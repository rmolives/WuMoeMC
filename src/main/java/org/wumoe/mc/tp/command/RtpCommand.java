package org.wumoe.mc.tp.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.tp.RtpManager;
import org.wumoe.mc.utils.TpUtil;

public class RtpCommand implements CommandExecutor {
    private final RtpManager manager;

    public RtpCommand(RtpManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        player.sendMessage(Component.text("正在寻找安全位置...", NamedTextColor.YELLOW));
        Location loc = manager.findSafeLocation(player.getWorld());
        if (loc == null) {
            player.sendMessage(Component.text("未找到安全位置", NamedTextColor.RED));
            return true;
        }
        TpUtil.tp(this.manager.plugin, player, loc);
        player.sendMessage(Component.text("已随机传送到安全位置", NamedTextColor.GREEN));
        return true;
    }
}

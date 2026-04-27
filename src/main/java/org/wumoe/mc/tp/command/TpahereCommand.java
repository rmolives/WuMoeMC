package org.wumoe.mc.tp.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.tp.TpManager;
import org.wumoe.mc.tp.TpRequest;

import java.util.UUID;

public class TpahereCommand implements CommandExecutor {
    private final TpManager manager;

    public TpahereCommand(TpManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        if (args.length != 1) return false;
        UUID uuid = this.manager.player.getUUID(args[0]);
        if (uuid == null) {
            player.sendMessage("§c该玩家不在线");
            return true;
        }
        Player target = Bukkit.getPlayer(uuid);
        if (target != null && target.equals(player)) {
            player.sendMessage(Component.text("不能对自己发送请求", NamedTextColor.RED));
            return true;
        }
        if (target != null) {
            this.manager.sendRequest(player, target, TpRequest.Type.TPAHERE);
            player.sendMessage(Component.text("已请求 " + target.getName() + " 传送到你这里", NamedTextColor.GREEN));
        }
        return true;
    }
}

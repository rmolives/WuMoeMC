package org.wumoe.mc.tpa.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.tpa.TpaManager;
import org.wumoe.mc.tpa.TpaRequest;

import java.util.UUID;

public class TpaCommand implements CommandExecutor {
    private final TpaManager manager;

    public TpaCommand(TpaManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        if (args.length != 1) return false;
        UUID uuid = this.manager.player.getUUID(args[0]);
        if (uuid == null) {
            player.sendMessage(Component.text("该玩家不在线", NamedTextColor.RED));
            return true;
        }
        Player target = Bukkit.getPlayer(uuid);
        if (target != null && target.equals(player)) {
            player.sendMessage(Component.text("不能对自己发送请求", NamedTextColor.RED));
            return true;
        }
        if (target != null) {
            this.manager.sendRequest(player, target, TpaRequest.Type.TPA);
            player.sendMessage(Component.text("已请求传送到 " + target.getName() + " 那里", NamedTextColor.GREEN));
        }
        return true;
    }
}

package org.wumoe.mc.tpa.command;

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
            sender.sendMessage("只有玩家可以使用");
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
            player.sendMessage("§c不能对自己发送请求");
            return true;
        }
        if (target != null) {
            this.manager.sendRequest(player, target, TpaRequest.Type.TPA);
            player.sendMessage("§a已请求传送到 " + target.getName() + " 那里");
        }
        return true;
    }
}

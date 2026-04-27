package org.wumoe.mc.tpa;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.wumoe.mc.player.PlayerManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.util.*;

public class TpaManager {
    private final Map<UUID, TpaRequest> requests = new HashMap<>();
    private final JavaPlugin plugin;
    public final PlayerManager player;

    private final long expireTime = 30 * 1000L;

    public TpaManager(PlayerManager player, JavaPlugin plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    public void sendRequest(Player from, Player to, TpaRequest.Type type) {
        UUID toId = to.getUniqueId();
        requests.put(toId, new TpaRequest(from.getUniqueId(), toId, type));
        to.sendMessage(Component.text(from.getName(), NamedTextColor.GREEN)
                .append(Component.text(" 请求", NamedTextColor.GREEN))
                .append(Component.text(
                        type == TpaRequest.Type.TPA ? "传送到你这里" : "让你传送过去",
                        NamedTextColor.WHITE)));
        to.sendMessage(Component.text("输入 /tpaccept 或 /tpdeny", NamedTextColor.YELLOW));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            TpaRequest req = requests.get(toId);
            if (req != null && System.currentTimeMillis() - req.time >= expireTime) {
                requests.remove(toId);
                Player target = Bukkit.getPlayer(toId);
                if (target != null)
                    target.sendMessage(Component.text("TPA请求已过期", NamedTextColor.RED));
            }
        }, 20 * 30);
    }

    public void accept(Player target) {
        TpaRequest req = requests.remove(target.getUniqueId());
        if (req == null) {
            target.sendMessage(Component.text("没有待处理请求", NamedTextColor.RED));
            return;
        }
        Player from = Bukkit.getPlayer(req.from);
        if (from == null) {
            target.sendMessage(Component.text("请求玩家已离线", NamedTextColor.RED));
            return;
        }
        if (req.type == TpaRequest.Type.TPA)
            from.teleport(target.getLocation());
        else
            target.teleport(from.getLocation());
        from.sendMessage(Component.text("TP成功", NamedTextColor.GREEN));
        target.sendMessage(Component.text("已接受请求", NamedTextColor.GREEN));
    }

    public void deny(Player target) {
        TpaRequest req = requests.remove(target.getUniqueId());
        if (req == null) {
            target.sendMessage(Component.text("没有待处理请求", NamedTextColor.RED));
            return;
        }
        Player from = Bukkit.getPlayer(req.from);
        if (from != null)
            from.sendMessage(Component.text("对方拒绝了你的请求", NamedTextColor.RED));
        target.sendMessage(Component.text("已拒绝请求", NamedTextColor.YELLOW));
    }
}

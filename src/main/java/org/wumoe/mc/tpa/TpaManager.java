package org.wumoe.mc.tpa;

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
        to.sendMessage("§a" + from.getName() + " 请求" +
                (type == TpaRequest.Type.TPA ? "传送到你这里" : "让你传送过去"));
        to.sendMessage("§e输入 /tpaccept 或 /tpdeny");
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            TpaRequest req = requests.get(toId);
            if (req != null && System.currentTimeMillis() - req.time >= expireTime) {
                requests.remove(toId);
                Player target = Bukkit.getPlayer(toId);
                if (target != null)
                    target.sendMessage("§cTPA请求已过期");
            }
        }, 20 * 30);
    }

    public void accept(Player target) {
        TpaRequest req = requests.remove(target.getUniqueId());
        if (req == null) {
            target.sendMessage("§c没有待处理请求");
            return;
        }
        Player from = Bukkit.getPlayer(req.from);
        if (from == null) {
            target.sendMessage("§c请求玩家已离线");
            return;
        }
        if (req.type == TpaRequest.Type.TPA)
            from.teleport(target.getLocation());
        else
            target.teleport(from.getLocation());
        from.sendMessage("§aTPA成功");
        target.sendMessage("§a已接受请求");
    }

    public void deny(Player target) {
        TpaRequest req = requests.remove(target.getUniqueId());
        if (req == null) {
            target.sendMessage("§c没有待处理请求");
            return;
        }
        Player from = Bukkit.getPlayer(req.from);
        if (from != null)
            from.sendMessage("§c对方拒绝了你的请求");
        target.sendMessage("§e已拒绝请求");
    }
}

package org.wumoe.mc.player.command;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.auth.AuthManager;
import org.wumoe.mc.ban.BanManager;
import org.wumoe.mc.player.PlayerManager;
import org.wumoe.mc.utils.PlayerUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class LookupCommand implements CommandExecutor {
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final PlayerManager playerManager;
    private final BanManager banManager;
    private final AuthManager authManager;

    public LookupCommand(PlayerManager nameCache, BanManager banManager, AuthManager authManager) {
        this.playerManager = nameCache;
        this.banManager = banManager;
        this.authManager = authManager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {
        if (args.length != 1) return false;
        if (sender instanceof Player player)
            if (!PlayerUtil.isHavePermission(player))
                return true;
        String name = args[0];
        UUID uuid = this.playerManager.getUUID(name);
        if (uuid == null) {
            sender.sendMessage("§c未找到该玩家记录！");
            return true;
        }
        Player online = Bukkit.getPlayer(uuid);
        boolean banned = this.banManager.isBanned(uuid);
        boolean loggedIn = this.authManager.isLoggedIn(uuid);
        boolean isOnline = online != null;
        sender.sendMessage("§e===== 玩家信息 =====");
        sender.sendMessage("§aName: §f" + name);
        sender.sendMessage("§aUUID: §f" + uuid);
        sender.sendMessage("§aIP: §f" + this.playerManager.getIP(uuid));
        sender.sendMessage("§aOP权限: " + (Bukkit.getOfflinePlayer(uuid).isOp() ? "§c是OP" : "§7不是OP"));
        sender.sendMessage("§a在线状态: " + (isOnline ? "§a在线" : "§7离线"));
        sender.sendMessage("§a登录状态: " + (loggedIn ? "§a已登录" : "§c未登录"));
        sender.sendMessage("§a首次加入: §f" + time(this.playerManager.getFirstJoin(uuid)));
        sender.sendMessage("§a注册时间: §f" + time(this.authManager.getRegisterTime(uuid)));
        sender.sendMessage("§a最后加入: §f" + time(this.playerManager.getLastJoin(uuid)));
        sender.sendMessage("§a最后离线: §f" + time(this.playerManager.getLastQuit(uuid)));
        if (banned) {
            sender.sendMessage("§c封禁状态: 已封禁");
            sender.sendMessage("§c封禁原因: §f" + this.banManager.getReason(uuid));
            sender.sendMessage("§c封禁时间: §f" + time(this.banManager.getBanTime(uuid)));
        } else
            sender.sendMessage("§a封禁状态: 正常");
        return true;
    }

    private String time(long t) {
        return t <= 0 ? "未知" : format.format(new Date(t));
    }
}

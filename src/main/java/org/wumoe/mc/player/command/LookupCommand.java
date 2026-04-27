package org.wumoe.mc.player.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            sender.sendMessage(Component.text("未找到该玩家记录！", NamedTextColor.RED));
            return true;
        }
        Player online = Bukkit.getPlayer(uuid);
        boolean banned = this.banManager.isBanned(uuid);
        boolean loggedIn = this.authManager.isLoggedIn(uuid);
        boolean isOnline = online != null;
        sender.sendMessage(Component.text("===== 玩家信息 =====", NamedTextColor.YELLOW));
        sender.sendMessage(
                Component.text("Name: ", NamedTextColor.GREEN)
                        .append(Component.text(name, NamedTextColor.WHITE)));
        sender.sendMessage(
                Component.text("UUID: ", NamedTextColor.GREEN)
                        .append(Component.text(uuid.toString(), NamedTextColor.WHITE)));
        sender.sendMessage(
                Component.text("IP: ", NamedTextColor.GREEN)
                        .append(Component.text(this.playerManager.getIP(uuid), NamedTextColor.WHITE)));
        sender.sendMessage(
                Component.text("OP权限: ", NamedTextColor.GREEN)
                        .append(Component.text(
                                Bukkit.getOfflinePlayer(uuid).isOp() ? "是OP" : "不是OP",
                                Bukkit.getOfflinePlayer(uuid).isOp() ? NamedTextColor.RED : NamedTextColor.GRAY)));
        sender.sendMessage(
                Component.text("在线状态: ", NamedTextColor.GREEN)
                        .append(Component.text(isOnline ? "在线" : "离线",
                                isOnline ? NamedTextColor.GREEN : NamedTextColor.GRAY)));
        sender.sendMessage(
                Component.text("登录状态: ", NamedTextColor.GREEN)
                        .append(Component.text(loggedIn ? "已登录" : "未登录",
                                loggedIn ? NamedTextColor.GREEN : NamedTextColor.RED)));
        sender.sendMessage(
                Component.text("首次加入: ", NamedTextColor.GREEN)
                        .append(Component.text(time(this.playerManager.getFirstJoin(uuid)), NamedTextColor.WHITE)));
        sender.sendMessage(
                Component.text("注册时间: ", NamedTextColor.GREEN)
                        .append(Component.text(time(this.authManager.getRegisterTime(uuid)), NamedTextColor.WHITE)));
        sender.sendMessage(
                Component.text("最后加入: ", NamedTextColor.GREEN)
                        .append(Component.text(time(this.playerManager.getLastJoin(uuid)), NamedTextColor.WHITE)));
        sender.sendMessage(
                Component.text("最后离线: ", NamedTextColor.GREEN)
                        .append(Component.text(time(this.playerManager.getLastQuit(uuid)), NamedTextColor.WHITE)));
        if (banned) {
            sender.sendMessage(Component.text("封禁状态: 已封禁", NamedTextColor.RED));
            sender.sendMessage(
                    Component.text("封禁原因: ", NamedTextColor.RED)
                            .append(Component.text(this.banManager.getReason(uuid), NamedTextColor.WHITE)));
            sender.sendMessage(
                    Component.text("封禁时间: ", NamedTextColor.RED)
                            .append(Component.text(time(this.banManager.getBanTime(uuid)), NamedTextColor.WHITE)));
        } else
            sender.sendMessage(
                    Component.text("封禁状态: 正常", NamedTextColor.GREEN));
        return true;
    }

    private String time(long t) {
        return t <= 0 ? "未知" : format.format(new Date(t));
    }
}

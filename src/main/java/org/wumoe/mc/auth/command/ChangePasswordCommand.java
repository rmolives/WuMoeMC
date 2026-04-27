package org.wumoe.mc.auth.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.auth.AuthManager;

public class ChangePasswordCommand implements CommandExecutor {
    private final AuthManager manager;

    public ChangePasswordCommand(AuthManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("只有玩家可以使用");
            return true;
        }
        if (args.length != 3) return false;
        if (!this.manager.isRegistered(player)) {
            player.sendMessage("§c你还没有注册！");
            return true;
        }
        if (!this.manager.isLoggedIn(player)) {
            player.sendMessage("§c请先登录！");
            return true;
        }
        if (!args[1].equals(args[2])) {
            player.sendMessage("§c两次输入的密码不一样！");
            return true;
        }
        if (args[1].length() < 6) {
            player.sendMessage("§c密码至少6位！");
            return true;
        }
        boolean success = this.manager.changePassword(player, args[0], args[1]);
        if (!success) {
            player.sendMessage("§c旧密码错误！");
            return true;
        }
        player.sendMessage("§a密码修改成功！");
        this.manager.wait(player);
        player.sendMessage("§e请重新登录！");
        return true;
    }
}

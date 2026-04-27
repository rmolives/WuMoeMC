package org.wumoe.mc.auth.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.auth.AuthManager;

public class LoginCommand implements CommandExecutor {
    private final AuthManager manager;

    public LoginCommand(AuthManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("只有玩家可以使用");
            return true;
        }
        if (args.length != 1) return false;
        if (!this.manager.isRegistered(player)) {
            player.sendMessage("§c你还没有注册！");
            return true;
        }
        if (this.manager.isLoggedIn(player)) {
            player.sendMessage("§e你已经登录了！");
            return true;
        }
        if (this.manager.login(player, args[0]))
            player.sendMessage("§a登录成功！");
        else
            player.sendMessage("§c密码错误！");
        return true;
    }
}

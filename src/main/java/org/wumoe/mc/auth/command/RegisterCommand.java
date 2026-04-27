package org.wumoe.mc.auth.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.auth.AuthManager;

public class RegisterCommand implements CommandExecutor {
    private final AuthManager manager;

    public RegisterCommand(AuthManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("只有玩家可以使用");
            return true;
        }
        if (args.length != 2) return false;
        if (this.manager.isRegistered(player)) {
            player.sendMessage("§c你已经注册过了！");
            return true;
        }
        if (!args[0].equals(args[1])) {
            player.sendMessage("§c两次输入的密码不一样！");
            return true;
        }
        if (args[0].length() < 6) {
            player.sendMessage("§c密码至少6位！");
            return true;
        }
        this.manager.register(player, args[0]);
        player.sendMessage("§a注册成功");
        this.manager.login(player, args[0]);
        return true;
    }
}

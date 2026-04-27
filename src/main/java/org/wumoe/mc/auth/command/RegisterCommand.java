package org.wumoe.mc.auth.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        if (args.length != 2) return false;
        if (this.manager.isRegistered(player)) {
            player.sendMessage(Component.text("你已经注册过了！", NamedTextColor.RED));
            return true;
        }
        if (!args[0].equals(args[1])) {
            player.sendMessage(Component.text("两次输入的密码不一样！", NamedTextColor.RED));
            return true;
        }
        if (args[0].length() < 6) {
            player.sendMessage(Component.text("密码至少6位！", NamedTextColor.RED));
            return true;
        }
        this.manager.register(player, args[0]);
        player.sendMessage(Component.text("注册成功", NamedTextColor.GREEN));
        this.manager.login(player, args[0]);
        return true;
    }
}

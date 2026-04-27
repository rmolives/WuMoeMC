package org.wumoe.mc.auth.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        if (args.length != 1) return false;
        if (!this.manager.isRegistered(player)) {
            player.sendMessage(Component.text("你还没有注册！", NamedTextColor.RED));
            return true;
        }
        if (this.manager.isLoggedIn(player)) {
            player.sendMessage(Component.text("你已经登录了！", NamedTextColor.YELLOW));
            return true;
        }
        if (this.manager.login(player, args[0]))
            player.sendMessage(Component.text("登录成功！", NamedTextColor.GREEN));
        else
            player.sendMessage(Component.text("密码错误！", NamedTextColor.RED));
        return true;
    }
}

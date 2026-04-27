package org.wumoe.mc.auth.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        if (args.length != 3) return false;
        if (!this.manager.isRegistered(player)) {
            player.sendMessage(Component.text("你还没有注册！", NamedTextColor.RED));
            return true;
        }
        if (!this.manager.isLoggedIn(player)) {
            player.sendMessage(Component.text("请先登录！", NamedTextColor.RED));
            return true;
        }
        if (!args[1].equals(args[2])) {
            player.sendMessage(Component.text("两次输入的密码不一样！", NamedTextColor.RED));
            return true;
        }
        if (args[1].length() < 6) {
            player.sendMessage(Component.text("密码至少6位！", NamedTextColor.RED));
            return true;
        }
        boolean success = this.manager.changePassword(player, args[0], args[1]);
        if (!success) {
            player.sendMessage(Component.text("旧密码错误！", NamedTextColor.RED));
            return true;
        }
        player.sendMessage(Component.text("密码修改成功！", NamedTextColor.GREEN));
        this.manager.wait(player);
        player.sendMessage(Component.text("请重新登录！", NamedTextColor.YELLOW));
        return true;
    }
}

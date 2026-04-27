package org.wumoe.mc.tp.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.tp.TpaManager;

public class TpDenyCommand implements CommandExecutor {
    private final TpaManager manager;

    public TpDenyCommand(TpaManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        this.manager.deny(player);
        return true;
    }
}

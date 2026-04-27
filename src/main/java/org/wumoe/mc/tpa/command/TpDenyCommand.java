package org.wumoe.mc.tpa.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.tpa.TpaManager;

public class TpDenyCommand implements CommandExecutor {
    private final TpaManager manager;

    public TpDenyCommand(TpaManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("只有玩家可以使用");
            return true;
        }
        this.manager.deny(player);
        return true;
    }
}

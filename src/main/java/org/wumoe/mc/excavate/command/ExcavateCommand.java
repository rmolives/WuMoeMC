package org.wumoe.mc.excavate.command;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.wumoe.mc.excavate.ExcavateManager;

public class ExcavateCommand implements CommandExecutor {
    private final ExcavateManager manager;

    public ExcavateCommand(ExcavateManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c只有玩家可以使用");
            return true;
        }
        if (args.length == 0) return false;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            player.sendMessage("§c手上没有物品");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "on" -> {
                this.manager.set(item, true);
                player.sendMessage("§a已开启批量挖掘");
            }
            case "off" -> {
                this.manager.set(item, false);
                player.sendMessage("§c已关闭批量挖掘");
            }
            default -> {
                return false;
            }
        }
        return true;
    }
}

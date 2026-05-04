package org.wumoe.mc.excavate.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            sender.sendMessage(Component.text("只有玩家可以使用", NamedTextColor.RED));
            return true;
        }
        if (args.length != 1) return false;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) {
            player.sendMessage(Component.text("手上没有物品", NamedTextColor.RED));
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "on" -> {
                this.manager.set(item, true);
                player.sendMessage(Component.text("已开启批量挖掘", NamedTextColor.GREEN));
            }
            case "off" -> {
                this.manager.set(item, false);
                player.sendMessage(Component.text("已关闭批量挖掘", NamedTextColor.RED));
            }
            default -> {
                return false;
            }
        }
        return true;
    }
}

package org.wumoe.mc.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class PlayerUtil {
    public static boolean isHavePermission(Player player) {
        if (!player.isOp()) {
            player.sendMessage(Component.text("你没有权限！", NamedTextColor.RED));
            return false;
        }
        return true;
    }
}

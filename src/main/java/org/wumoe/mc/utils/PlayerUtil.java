package org.wumoe.mc.utils;

import org.bukkit.entity.Player;

public class PlayerUtil {
    public static boolean isHavePermission(Player player) {
        if (!player.isOp()) {
            player.sendMessage("§c你没有权限！");
            return false;
        }
        return true;
    }
}

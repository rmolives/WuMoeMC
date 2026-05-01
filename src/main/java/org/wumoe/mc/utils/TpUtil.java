package org.wumoe.mc.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class TpUtil {
    public static void tp(JavaPlugin plugin, Player player, Location target) {
        World world = player.getWorld();
        List<LivingEntity> leashed = new ArrayList<>();
        for (Entity entity : world.getNearbyEntities(player.getLocation(), 10, 10, 10)) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (!living.isLeashed()) continue;
            if (living.getLeashHolder() instanceof Player holder
                    && holder.getUniqueId().equals(player.getUniqueId()))
                leashed.add(living);
        }
        for (LivingEntity living : leashed)
            living.setLeashHolder(null);
        for (LivingEntity living : leashed)
            living.teleport(target.clone());
        player.teleport(target);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (LivingEntity living : leashed) {
                if (!living.isValid()) continue;
                try {
                    living.setLeashHolder(player);
                } catch (Exception ignored) {}
            }
        }, 1L);
    }
}

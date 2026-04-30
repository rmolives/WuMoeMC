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
        for (Entity entity : world.getNearbyEntities(player.getLocation(), 16, 16, 16)) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (!living.isLeashed()) continue;
            if (living.getLeashHolder() instanceof Player holder
                    && holder.getUniqueId().equals(player.getUniqueId()))
                leashed.add(living);
        }
        for (LivingEntity living : leashed)
            living.setLeashHolder(null);
        int i = 0;
        for (LivingEntity living : leashed) {
            Location loc = target.clone().add((i % 3) - 1, 0, ((double) i / 3) - 1);
            living.teleport(loc);
            ++i;
        }
        player.teleport(target);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (LivingEntity living : leashed) {
                if (!living.isValid()) continue;
                try {
                    living.setLeashHolder(player);
                } catch (Exception ignored) {
                }
            }
        }, 1L);
    }
}

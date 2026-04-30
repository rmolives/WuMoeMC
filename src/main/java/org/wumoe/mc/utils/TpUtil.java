package org.wumoe.mc.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TpUtil {
    public static boolean isSafe(World world, int x, int y, int z) {
        Block ground = world.getBlockAt(x, y, z);
        Block feet = world.getBlockAt(x, y + 1, z);
        Block head = world.getBlockAt(x, y + 2, z);
        Material g = ground.getType();
        if (g == Material.LAVA || g == Material.WATER) return false;
        if (g.name().contains("MAGMA")) return false;
        if (g.name().contains("CACTUS")) return false;
        if (g.name().contains("FIRE")) return false;
        if (g.name().contains("LEAVES")) return false;
        if (!g.isSolid()) return false;
        if (!feet.isPassable() || !head.isPassable()) return false;
        return !world.getBlockAt(x, y - 1, z).isEmpty();
    }

    public static Location findSafeSpot(World world, Location center, int radius) {
        int baseX = center.getBlockX();
        int baseZ = center.getBlockZ();
        for (int r = 0; r <= radius; ++r) {
            for (int dx = -r; dx <= r; ++dx) {
                for (int dz = -r; dz <= r; ++dz) {
                    if (Math.abs(dx) != r && Math.abs(dz) != r) continue;
                    int x = baseX + dx;
                    int z = baseZ + dz;
                    int y = world.getHighestBlockYAt(x, z);
                    if (isSafe(world, x, y, z))
                        return new Location(world, x + 0.5, y + 1, z + 0.5);
                }
            }
        }
        return null;
    }

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
        int i = 0;
        for (LivingEntity living : leashed) {
            Location base = target.clone().add((i % 3) - 1, 0, (i / 3) - 1);
            Location safe = findSafeSpot(target.getWorld(), base, 6);
            living.teleport(Objects.requireNonNullElseGet(safe, () -> target.clone().add(0, 1, 0)));
            ++i;
        }
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

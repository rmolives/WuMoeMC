package org.wumoe.mc.tp;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class RtpManager {
    private final Random random = new Random();
    public final JavaPlugin plugin;

    public RtpManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    private final int radius = 5000;
    private final int maxAttempts = 50;

    public Location findSafeLocation(World world) {
        for (int i = 0; i < this.maxAttempts; ++i) {
            int x = this.random.nextInt(this.radius * 2) - this.radius;
            int z = this.random.nextInt(this.radius * 2) - this.radius;
            int y = world.getHighestBlockYAt(x, z);
            Material block = world.getBlockAt(x, y, z).getType();
            Material above = world.getBlockAt(x, y + 1, z).getType();
            Material above2 = world.getBlockAt(x, y + 2, z).getType();
            if (block == Material.WATER || block == Material.LAVA) continue;
            Biome biome = world.getBiome(x, y, z);
            if (biome.toString().contains("OCEAN")) continue;
            if (block.isSolid() && above.isAir() && above2.isAir())
                return new Location(world, x + 0.5, y + 1, z + 0.5);
        }
        return null;
    }
}

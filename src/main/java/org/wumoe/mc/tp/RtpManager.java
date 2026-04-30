package org.wumoe.mc.tp;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.wumoe.mc.utils.TpUtil;

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
            Location base = new Location(world, x, y, z);
            Location safe = TpUtil.findSafeSpot(world, base, 6);
            if (safe != null)
                return safe;
        }
        return null;
    }
}

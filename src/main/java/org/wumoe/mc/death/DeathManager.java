package org.wumoe.mc.death;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.wumoe.mc.utils.PropertiesUtil;

import java.util.UUID;

public class DeathManager {
    private final PropertiesUtil config;

    public DeathManager(PropertiesUtil config) {
        this.config = config;
    }

    public void recordDeath(Player player) {
        Location loc = player.getLocation();

        String value = loc.getWorld().getName() + "," +
                loc.getX() + "," +
                loc.getY() + "," +
                loc.getZ() + "," +
                loc.getYaw() + "," +
                loc.getPitch();

        config.set("death." + player.getUniqueId(), value);
        config.save();
    }

    public Location getDeathLocation(Player player) {
        return getDeathLocation(player.getUniqueId());
    }

    public Location getDeathLocation(UUID uuid) {
        String value = config.get("death." + uuid);
        if (value == null) return null;
        try {
            String[] split = value.split(",");
            World world = Bukkit.getWorld(split[0]);
            if (world == null) return null;
            double x = Double.parseDouble(split[1]);
            double y = Double.parseDouble(split[2]);
            double z = Double.parseDouble(split[3]);
            float yaw = Float.parseFloat(split[4]);
            float pitch = Float.parseFloat(split[5]);
            return new Location(world, x, y, z, yaw, pitch);
        } catch (Exception e) {
            return null;
        }
    }
}

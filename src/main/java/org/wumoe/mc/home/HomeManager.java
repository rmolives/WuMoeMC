package org.wumoe.mc.home;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.wumoe.mc.utils.PropertiesUtil;

import java.util.UUID;

public class HomeManager {
    private final PropertiesUtil config;

    public HomeManager(PropertiesUtil config) {
        this.config = config;
    }

    public void setHome(Player player) {
        Location loc = player.getLocation();
        String value = String.format("%s,%f,%f,%f,%f,%f",
                loc.getWorld().getName(),
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                loc.getYaw(),
                loc.getPitch()
        );
        this.config.set("home." + player.getUniqueId(), value);
        this.config.save();
    }

    public boolean deleteHome(Player player) {
        if (!hasHome(player)) return false;
        this.config.remove("home." + player.getUniqueId());
        this.config.save();
        return true;
    }

    public Location getHome(Player player) {
        String value = this.config.get("home." + player.getUniqueId());
        if (value == null) return null;
        String[] split = value.split(",");
        if (split.length != 6) return null;
        World world = Bukkit.getWorld(split[0]);
        if (world == null) return null;
        return new Location(
                world,
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Float.parseFloat(split[4]),
                Float.parseFloat(split[5])
        );
    }

    public boolean hasHome(Player player) {
        return this.config.contains("home." + player.getUniqueId());
    }
}

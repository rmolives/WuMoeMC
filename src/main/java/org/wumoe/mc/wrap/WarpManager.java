package org.wumoe.mc.wrap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.wumoe.mc.utils.PropertiesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WarpManager {
    private final PropertiesUtil properties;

    public WarpManager(PropertiesUtil properties) {
        this.properties = properties;
    }

    public void setWarp(Player player, String name, Location loc) {
        String value =
                loc.getWorld().getName() + "," +
                        loc.getX() + "," +
                        loc.getY() + "," +
                        loc.getZ() + "," +
                        loc.getYaw() + "," +
                        loc.getPitch();
        this.properties.set("warp." + player.getUniqueId() + "." + name.toLowerCase(), value);
        this.properties.save();
    }

    public Location getWarp(Player player, String name) {
        String value = this.properties.get("warp." + player.getUniqueId() + "." + name.toLowerCase());
        if (value == null)
            return null;
        String[] split = value.split(",");
        if (split.length != 6)
            return null;
        World world = Bukkit.getWorld(split[0]);
        if (world == null)
            return null;
        return new Location(
                world,
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Float.parseFloat(split[4]),
                Float.parseFloat(split[5])
        );
    }

    public boolean deleteWarp(Player player, String name) {
        String key = "warp." + player.getUniqueId() + "." + name.toLowerCase();
        if (!this.properties.contains(key))
            return false;
        this.properties.remove(key);
        this.properties.save();
        return true;
    }

    public List<String> getWarps(Player player) {
        String prefix = "warp." + player.getUniqueId() + ".";
        Set<String> keys = this.properties.getKeys(prefix);
        List<String> result = new ArrayList<>();
        for (String key : keys)
            result.add(key.substring(prefix.length()));
        return result;
    }
}

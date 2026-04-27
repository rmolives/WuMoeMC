package org.wumoe.mc.chunk;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.wumoe.mc.utils.PropertiesUtil;

public class ChunkLoadManager {
    private final JavaPlugin plugin;
    private final PropertiesUtil config;

    public ChunkLoadManager(JavaPlugin plugin, PropertiesUtil config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void add(Chunk chunk) {
        String k = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
        chunk.addPluginChunkTicket(plugin);
        config.set("chunkload." + k, "true");
        config.save();
    }

    public void remove(Chunk chunk) {
        String k = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
        chunk.removePluginChunkTicket(plugin);
        config.remove("chunkload." + k);
        config.save();
    }

    public void loadAll() {
        if (config.getKeys("chunkload.") == null) return;
        for (String fullKey : config.getKeys("chunkload.")) {
            String raw = fullKey.replace("chunkload.", "");
            String[] split = raw.split(",");
            if (split.length != 3) continue;
            World world = Bukkit.getWorld(split[0]);
            if (world == null) continue;
            int x = Integer.parseInt(split[1]);
            int z = Integer.parseInt(split[2]);
            Chunk chunk = world.getChunkAt(x, z);
            chunk.addPluginChunkTicket(plugin);
        }
    }
}

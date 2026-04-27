package org.wumoe.mc;

import org.bukkit.plugin.java.JavaPlugin;
import org.wumoe.mc.auth.AuthManager;
import org.wumoe.mc.ban.BanManager;
import org.wumoe.mc.chunk.ChunkLoadManager;
import org.wumoe.mc.player.PlayerManager;
import org.wumoe.mc.death.DeathManager;
import org.wumoe.mc.home.HomeManager;
import org.wumoe.mc.tpa.TpaManager;
import org.wumoe.mc.utils.PropertiesUtil;

public class WuMoeManager {
    public JavaPlugin plugin;
    public TpaManager tpaManager;
    public DeathManager deathManager;
    public HomeManager homeManager;
    public AuthManager authManager;
    public BanManager banManager;
    public PlayerManager playerManager;
    public ChunkLoadManager chunkLoadManager;

    public WuMoeManager(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        this.deathManager = new DeathManager(new PropertiesUtil(plugin.getDataFolder() + "/death.properties"));
        this.homeManager = new HomeManager(new PropertiesUtil(plugin.getDataFolder() + "/homes.properties"));
        this.authManager = new AuthManager(new PropertiesUtil(plugin.getDataFolder() + "/auth.properties"), plugin);
        this.playerManager = new PlayerManager(new PropertiesUtil(plugin.getDataFolder() + "/player.properties"));
        this.tpaManager = new TpaManager(this.playerManager, plugin);
        this.banManager = new BanManager(new PropertiesUtil(plugin.getDataFolder() + "/bans.properties"), this.playerManager);
        this.chunkLoadManager = new ChunkLoadManager(this.plugin, new PropertiesUtil(plugin.getDataFolder() + "/chunk_load.properties"));
    }
}

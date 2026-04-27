package org.wumoe.mc;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.wumoe.mc.auth.command.ChangePasswordCommand;
import org.wumoe.mc.auth.command.LoginCommand;
import org.wumoe.mc.auth.command.RegisterCommand;
import org.wumoe.mc.auth.listener.AuthListener;
import org.wumoe.mc.ban.command.BanCommand;
import org.wumoe.mc.ban.command.KickCommand;
import org.wumoe.mc.ban.command.UnbanCommand;
import org.wumoe.mc.ban.listener.BanListener;
import org.wumoe.mc.chain.listener.ChainListener;
import org.wumoe.mc.chunk.command.ChunkLoadCommand;
import org.wumoe.mc.menu.command.MenuCommand;
import org.wumoe.mc.menu.listener.MenuListener;
import org.wumoe.mc.player.command.NameCommand;
import org.wumoe.mc.player.command.UUIDCommand;
import org.wumoe.mc.player.listener.PlayerListener;
import org.wumoe.mc.death.command.BackCommand;
import org.wumoe.mc.death.listener.DeathListener;
import org.wumoe.mc.home.command.DelHomeCommand;
import org.wumoe.mc.home.command.HomeCommand;
import org.wumoe.mc.home.command.SetHomeCommand;
import org.wumoe.mc.player.command.LookupCommand;
import org.wumoe.mc.tpa.command.TpAcceptCommand;
import org.wumoe.mc.tpa.command.TpaCommand;
import org.wumoe.mc.tpa.command.TpDenyCommand;
import org.wumoe.mc.tpa.command.TpahereCommand;

import java.util.Objects;

public final class WuMoePlugin extends JavaPlugin {
    public WuMoeManager manager;

    @Override
    public void onEnable() {
        this.manager = new WuMoeManager(this);
        /*-----------------------------------------------*/
        register("menu", new MenuCommand(this.manager.menuManager));
        getServer().getPluginManager().registerEvents(new MenuListener(this.manager.menuManager), this);
        /*-----------------------------------------------*/
        register("chunkload", new ChunkLoadCommand());
        /*-----------------------------------------------*/
        register("uuid", new UUIDCommand(this.manager.playerManager));
        register("name", new NameCommand(this.manager.playerManager));
        register("lookup", new LookupCommand(this.manager.playerManager, this.manager.banManager, this.manager.authManager));
        getServer().getPluginManager().registerEvents(new PlayerListener(this.manager.playerManager), this);
        /*-----------------------------------------------*/
        register("tpa", new TpaCommand(this.manager.tpaManager));
        register("tpahere", new TpahereCommand(this.manager.tpaManager));
        register("tpaccept", new TpAcceptCommand(this.manager.tpaManager));
        register("tpdeny", new TpDenyCommand(this.manager.tpaManager));
        /*-----------------------------------------------*/
        register("back", new BackCommand(this.manager.deathManager));
        getServer().getPluginManager().registerEvents(new DeathListener(this.manager.deathManager), this);
        /*-----------------------------------------------*/
        register("home", new HomeCommand(this.manager.homeManager));
        register("sethome", new SetHomeCommand(this.manager.homeManager));
        register("delhome", new DelHomeCommand(this.manager.homeManager));
        /*-----------------------------------------------*/
        register("login", new LoginCommand(this.manager.authManager));
        register("register", new RegisterCommand(this.manager.authManager));
        register("changepassword", new ChangePasswordCommand(this.manager.authManager));
        getServer().getPluginManager().registerEvents(new AuthListener(this.manager.authManager), this);
        /*-----------------------------------------------*/
        register("ban", new BanCommand(this.manager.banManager));
        register("unban", new UnbanCommand(this.manager.banManager));
        register("kick", new KickCommand(this.manager.banManager));
        getServer().getPluginManager().registerEvents(new BanListener(this.manager.banManager), this);
        /*-----------------------------------------------*/
        getServer().getPluginManager().registerEvents(new ChainListener(), this);
        /*-----------------------------------------------*/
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> this.manager.playerManager.onQuit(player));
    }

    private void register(String name, CommandExecutor executor) {
        Objects.requireNonNull(getCommand(name), "命令未注册: " + name)
                .setExecutor(executor);
    }
}

package org.wumoe.mc.auth.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.entity.Player;
import org.wumoe.mc.auth.AuthManager;

public class AuthListener implements Listener {
    private final AuthManager manager;

    public AuthListener(AuthManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.manager.wait(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.manager.logout(event.getPlayer());
    }

    private void msg(Player p) {
        p.sendMessage(Component.text("§c请先登录！"));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (this.manager.isLoggedIn(p)) return;
        if (event.getFrom().getX() != event.getTo().getX()
                || event.getFrom().getY() != event.getTo().getY()
                || event.getFrom().getZ() != event.getTo().getZ()) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player p = event.getPlayer();
        if (this.manager.isLoggedIn(p)) return;
        event.setCancelled(true);
        msg(p);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        if (this.manager.isLoggedIn(p)) return;
        String msg = event.getMessage().toLowerCase();
        if (msg.startsWith("/login") || msg.startsWith("/register")) return;
        event.setCancelled(true);
        msg(p);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player p)) return;
        if (this.manager.isLoggedIn(p)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player p)) return;
        if (this.manager.isLoggedIn(p)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        if (this.manager.isLoggedIn(p)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (this.manager.isLoggedIn(p)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        if (this.manager.isLoggedIn(p)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        if (this.manager.isLoggedIn(p)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player p)) return;
        if (this.manager.isLoggedIn(p)) return;
        event.setCancelled(true);
    }
}

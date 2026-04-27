package org.wumoe.mc.menu.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.wumoe.mc.menu.MenuManager;
import org.wumoe.mc.menu.holder.MenuHolder;

public class MenuListener implements Listener {
    private final MenuManager manager;

    public MenuListener(MenuManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!isMenu(e)) return;
        e.setCancelled(true);
        if (e.isShiftClick()) return;
        if (e.getClick() == ClickType.NUMBER_KEY) return;
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory() != e.getView().getTopInventory()) return;
        if (!(e.getWhoClicked() instanceof Player player)) return;
        int slot = e.getRawSlot();
        MenuHolder holder = (MenuHolder) e.getInventory().getHolder();
        if (holder == null) return;
        String menuName = holder.getMenuName();
        String cmd = manager.getCommand(menuName, slot);
        String run = manager.getRunType(menuName, slot);
        if (cmd == null || cmd.isBlank()) return;
        cmd = cmd.trim();
        if (cmd.equalsIgnoreCase("close")) {
            player.closeInventory();
            return;
        }
        if (cmd.equalsIgnoreCase("refresh")) {
            player.closeInventory();
            manager.open(player, menuName);
            return;
        }
        if (cmd.toLowerCase().startsWith("menu:")) {
            String target = cmd.substring(5).trim();
            if (target.isEmpty()) {
                player.sendMessage("§c菜单不存在");
                return;
            }
            player.closeInventory();
            manager.open(player, target);
            return;
        }
        player.closeInventory();
        if (cmd.startsWith("/"))
            cmd = cmd.substring(1);
        cmd = cmd
                .replace("%player%", player.getName())
                .replace("%uuid%", player.getUniqueId().toString());
        String mode = (run == null || run.isBlank()) ? "player" : run.toLowerCase();
        switch (mode) {
            case "console" -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            case "op" -> {
                boolean wasOp = player.isOp();
                try {
                    if (!wasOp) player.setOp(true);
                    player.performCommand(cmd);
                } finally {
                    if (!wasOp) player.setOp(false);
                }
            }
            default -> player.performCommand(cmd);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (!isMenu(e)) return;
        for (int slot : e.getRawSlots()) {
            if (slot < e.getView().getTopInventory().getSize()) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (!isMenu(e.getPlayer().getOpenInventory())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!isMenu(e)) return;
        e.getPlayer().setItemOnCursor(null);
    }

    private boolean isMenu(InventoryEvent e) {
        return e.getInventory().getHolder() instanceof MenuHolder;
    }

    private boolean isMenu(org.bukkit.inventory.InventoryView view) {
        return view.getTopInventory().getHolder() instanceof MenuHolder;
    }
}
package org.wumoe.mc.menu.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
    public void onClick(InventoryClickEvent event) {
        if (!isMenu(event)) return;
        event.setCancelled(true);
        if (event.isShiftClick()) return;
        if (event.getClick() == ClickType.NUMBER_KEY) return;
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory() != event.getView().getTopInventory()) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;
        int slot = event.getRawSlot();
        MenuHolder holder = (MenuHolder) event.getInventory().getHolder();
        if (holder == null) return;
        String menuName = holder.getMenuName();
        String cmd = this.manager.getCommand(menuName, slot);
        String run = this.manager.getRunType(menuName, slot);
        if (cmd == null || cmd.isBlank()) return;
        cmd = cmd.trim();
        if (cmd.toLowerCase().startsWith("menu:")) {
            String target = cmd.substring(5).trim();
            if (target.isEmpty()) {
                player.sendMessage(Component.text("菜单不存在", NamedTextColor.RED));
                return;
            }
            player.closeInventory();
            this.manager.open(player, target);
            return;
        }
        player.closeInventory();
        if (cmd.startsWith("/"))
            cmd = cmd.substring(1);
        cmd = cmd.replace("%player%", player.getName())
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
    public void onDrag(InventoryDragEvent event) {
        if (!isMenu(event)) return;
        for (int slot : event.getRawSlots()) {
            if (slot < event.getView().getTopInventory().getSize()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!isMenu(event.getPlayer().getOpenInventory())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!isMenu(event)) return;
        event.getPlayer().setItemOnCursor(null);
    }

    private boolean isMenu(InventoryEvent event) {
        return event.getInventory().getHolder() instanceof MenuHolder;
    }

    private boolean isMenu(org.bukkit.inventory.InventoryView view) {
        return view.getTopInventory().getHolder() instanceof MenuHolder;
    }
}

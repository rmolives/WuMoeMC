package org.wumoe.mc.excavate.listener;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.wumoe.mc.excavate.ExcavateManager;

import java.util.HashSet;
import java.util.Set;

public class ExcavateListener implements Listener {
    private final ExcavateManager manager;

    public ExcavateListener(ExcavateManager manager) {
        this.manager = manager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (!this.manager.has(tool)) return;
        Block origin = e.getBlock();
        Vector dir = player.getEyeLocation().getDirection().normalize();
        int dx = Math.abs(dir.getX()) > Math.abs(dir.getZ()) ? (dir.getX() > 0 ? 1 : -1) : 0;
        int dz = dx == 0 ? (dir.getZ() > 0 ? 1 : -1) : 0;
        int dy = (Math.abs(dir.getY()) > Math.abs(dir.getX()) && Math.abs(dir.getY()) > Math.abs(dir.getZ()))
                ? (dir.getY() > 0 ? 1 : -1) : 0;
        Set<Block> visited = new HashSet<>();
        visited.add(origin);
        for (int depth = 0; depth < 3; ++depth) {
            Block center = origin.getRelative(dx * depth, dy * depth, dz * depth);
            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    Block target;
                    if (dx != 0)
                        target = center.getRelative(0, x, y);
                    else if (dz != 0)
                        target = center.getRelative(x, y, 0);
                    else
                        target = center.getRelative(x, 0, y);
                    if (visited.contains(target)) continue;
                    visited.add(target);
                    target.breakNaturally(tool);
                }
            }
        }
    }
}

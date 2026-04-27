package org.wumoe.mc.chain.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ChainListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        chainMine(event.getBlock(), event.getBlock().getType(), event.getPlayer());
    }

    private boolean isMineableMaterial(Material material) {
        return material.name().endsWith("_ORE") || material == Material.ANCIENT_DEBRIS;
    }

    private void chainMine(Block block, Material blockType, Player player) {
        if (block == null || !isMineableMaterial(block.getType()) || block.getType() != blockType)
            return;
        block.breakNaturally(player.getInventory().getItemInMainHand());
        for (int x = -1; x <= 1; ++x)
            for (int y = -1; y <= 1; ++y)
                for (int z = -1; z <= 1; ++z)
                    chainMine(block.getRelative(x, y, z), blockType, player);
    }
}

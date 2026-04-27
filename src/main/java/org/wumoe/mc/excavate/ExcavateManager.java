package org.wumoe.mc.excavate;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ExcavateManager {
    private final NamespacedKey key;

    public ExcavateManager(JavaPlugin plugin) {
        this.key = new NamespacedKey(plugin, "batch_mining");
    }

    public boolean has(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        return pdc.has(this.key, PersistentDataType.BYTE);
    }

    public void set(ItemStack item, boolean enable) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        List<Component> lore = meta.lore();
        if (lore == null) lore = new ArrayList<>();
        lore.removeIf(line ->
                line instanceof TextComponent tc &&
                        tc.content().contains("批量挖掘"));
        if (enable) {
            meta.getPersistentDataContainer().set(this.key, PersistentDataType.BYTE, (byte) 1);
            lore.add(Component.text("批量挖掘", NamedTextColor.GREEN));
        } else
            meta.getPersistentDataContainer().remove(this.key);
        meta.lore(lore.isEmpty() ? null : lore);
        item.setItemMeta(meta);
    }
}

package org.wumoe.mc.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.wumoe.mc.menu.holder.MenuHolder;
import org.wumoe.mc.utils.YamlUtil;

import java.util.*;

public class MenuManager {
    private final YamlUtil yaml;
    private final MiniMessage mm = MiniMessage.miniMessage();

    private final Map<String, Map<Integer, String>> commands = new HashMap<>();
    private final Map<String, Map<Integer, String>> runTypes = new HashMap<>();

    public MenuManager(YamlUtil yaml) {
        this.yaml = yaml;
    }

    public Inventory createMenu(String menuName) {
        var cfg = yaml.get();
        String path = "menus." + menuName;
        String title = cfg.getString(path + ".title");
        if (title == null)
            title = "<green>菜单";
        int size = cfg.getInt(path + ".size");
        MenuHolder holder = new MenuHolder(menuName);
        Inventory inv = Bukkit.createInventory(
                holder,
                size,
                mm.deserialize(title)
        );
        holder.setInventory(inv);
        commands.put(menuName, new HashMap<>());
        runTypes.put(menuName, new HashMap<>());
        var section = cfg.getConfigurationSection(path + ".items");
        if (section == null) return inv;
        for (String key : section.getKeys(false)) {
            int slot = Integer.parseInt(key);
            String base = path + ".items." + key;
            Material mat = Material.valueOf(cfg.getString(base + ".material"));
            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(mm.deserialize(Objects.requireNonNull(cfg.getString(base + ".name"))));
                List<String> loreStr = cfg.getStringList(base + ".lore");
                if (!loreStr.isEmpty()) {
                    List<Component> lore = new ArrayList<>();
                    for (String line : loreStr)
                        lore.add(mm.deserialize(line));
                    meta.lore(lore);
                }
                item.setItemMeta(meta);
            }
            inv.setItem(slot, item);
            String cmd = cfg.getString(base + ".command");
            String run = cfg.getString(base + ".run", "player");
            if (cmd != null) {
                commands.get(menuName).put(slot, cmd);
                runTypes.get(menuName).put(slot, run);
            }
        }
        return inv;
    }

    public void open(Player player, String menuName) {
        player.openInventory(createMenu(menuName));
    }

    public String getCommand(String menuName, int slot) {
        return commands.getOrDefault(menuName, Collections.emptyMap()).get(slot);
    }

    public String getRunType(String menuName, int slot) {
        return runTypes.getOrDefault(menuName, Collections.emptyMap()).get(slot);
    }
}
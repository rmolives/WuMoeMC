package org.wumoe.mc.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.wumoe.mc.menu.holder.MenuHolder;

import java.util.*;

public class MenuManager {
    private final YamlConfiguration yaml;
    private final MiniMessage mm = MiniMessage.miniMessage();

    private final Map<String, Map<Integer, String>> commands = new HashMap<>();
    private final Map<String, Map<Integer, String>> runTypes = new HashMap<>();

    public MenuManager(YamlConfiguration yaml) {
        this.yaml = yaml;
    }

    public Inventory createMenu(String menuName) {
        String path = "menus." + menuName;
        String title = yaml.getString(path + ".title");
        if (title == null)
            title = "<green>菜单";
        int size = yaml.getInt(path + ".size");
        MenuHolder holder = new MenuHolder(menuName);
        Inventory inv = Bukkit.createInventory(
                holder,
                size,
                mm.deserialize(title)
        );
        holder.setInventory(inv);
        commands.put(menuName, new HashMap<>());
        runTypes.put(menuName, new HashMap<>());
        var section = yaml.getConfigurationSection(path + ".items");
        if (section == null) return inv;
        for (String key : section.getKeys(false)) {
            int slot = Integer.parseInt(key);
            String base = path + ".items." + key;
            Material mat = Material.valueOf(yaml.getString(base + ".material"));
            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(mm.deserialize(Objects.requireNonNull(yaml.getString(base + ".name"))));
                List<String> loreStr = yaml.getStringList(base + ".lore");
                if (!loreStr.isEmpty()) {
                    List<Component> lore = new ArrayList<>();
                    for (String line : loreStr)
                        lore.add(mm.deserialize(line));
                    meta.lore(lore);
                }
                item.setItemMeta(meta);
            }
            inv.setItem(slot, item);
            String cmd = yaml.getString(base + ".command");
            String run = yaml.getString(base + ".run", "player");
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
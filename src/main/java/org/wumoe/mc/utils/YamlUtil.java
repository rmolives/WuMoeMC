package org.wumoe.mc.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class YamlUtil {
    private final File file;
    private YamlConfiguration config;

    public YamlUtil(File file) {
        this.file = file;
        reload();
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration get() {
        return this.config;
    }
}

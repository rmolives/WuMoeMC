package org.wumoe.mc.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlUtil {
    private final File file;
    private YamlConfiguration config;

    public YamlUtil(String filePath) {
        this.file = new File(filePath);
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration get() {
        return config;
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save yaml file", e);
        }
    }
}

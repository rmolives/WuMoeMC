package org.wumoe.mc.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class PropertiesUtil {
    private final Properties properties = new Properties();
    private final File file;

    public PropertiesUtil(String filePath) {
        this.file = new File(filePath);
        load();
    }

    public Set<String> getKeys(String prefix) {
        Set<String> result = new HashSet<>();
        for (Object keyObj : properties.keySet()) {
            String key = String.valueOf(keyObj);
            if (key.startsWith(prefix))
                result.add(key);
        }
        return result;
    }

    private synchronized void load() {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            try (InputStreamReader in = new InputStreamReader(
                    new FileInputStream(file),
                    StandardCharsets.UTF_8
            )) {
                properties.load(in);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file", e);
        }
    }

    public synchronized void save() {
        try (OutputStreamWriter out = new OutputStreamWriter(
                new FileOutputStream(file),
                StandardCharsets.UTF_8
        )) {
            properties.store(out, "WuMoe Minecraft Plugin");
        } catch (IOException e) {
            throw new RuntimeException("Failed to save properties file", e);
        }
    }

    public synchronized String get(String key) {
        return properties.getProperty(key);
    }

    public synchronized String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public synchronized void set(String key, String value) {
        properties.setProperty(key, value);
    }

    public synchronized void remove(String key) {
        properties.remove(key);
    }

    public synchronized boolean contains(String key) {
        return properties.containsKey(key);
    }
}

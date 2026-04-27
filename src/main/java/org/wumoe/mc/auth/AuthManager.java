package org.wumoe.mc.auth;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.wumoe.mc.utils.PropertiesUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.*;

public class AuthManager {
    private final PropertiesUtil config;
    private final JavaPlugin plugin;
    private final Set<UUID> loggedIn = new HashSet<>();
    private final Map<UUID, BukkitTask> kickTasks = new HashMap<>();
    private final Map<UUID, BukkitTask> titleTasks = new HashMap<>();

    public AuthManager(PropertiesUtil config, JavaPlugin plugin) {
        this.config = config;
        this.plugin = plugin;
    }

    public void startLoginTimeout(Player player) {
        UUID uuid = player.getUniqueId();
        int timeoutSeconds = 25;
        BukkitTask titleTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            int timeLeft = timeoutSeconds;
            @Override
            public void run() {
                if (isLoggedIn(uuid)) {
                    cancel(uuid);
                    return;
                }
                if (timeLeft <= 0) return;
                player.showTitle(
                        Title.title(
                                Component.text("§c请登录"),
                                Component.text("§e剩余 " + timeLeft + " 秒"),
                                Title.Times.times(
                                        Duration.ZERO,
                                        Duration.ofSeconds(1),
                                        Duration.ZERO
                                )
                        )
                );
                --timeLeft;
            }
        }, 0L, 20L);
        this.titleTasks.put(uuid, titleTask);
        BukkitTask kickTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!isLoggedIn(uuid))
                player.kick(Component.text("§c登录超时！"));
        }, timeoutSeconds * 20L);
        this.kickTasks.put(uuid, kickTask);
    }

    private void cancel(UUID uuid) {
        BukkitTask t1 = this.titleTasks.remove(uuid);
        if (t1 != null) t1.cancel();
        BukkitTask t2 = this.kickTasks.remove(uuid);
        if (t2 != null) t2.cancel();
    }

    public boolean isRegistered(Player player) {
        return this.config.contains("auth." + player.getUniqueId());
    }

    public boolean isLoggedIn(UUID uuid) {
        return this.loggedIn.contains(uuid);
    }

    public boolean isLoggedIn(Player player) {
        return isLoggedIn(player.getUniqueId());
    }

    public boolean register(Player player, String password) {
        if (isRegistered(player)) return false;
        this.config.set("auth." + player.getUniqueId(), hash(password));
        this.config.set("auth." + player.getUniqueId() + ".register_time",
                String.valueOf(System.currentTimeMillis()));
        this.config.save();
        return true;
    }

    public boolean changePassword(Player player, String oldPassword, String newPassword) {
        String stored = this.config.get("auth." + player.getUniqueId());
        if (stored == null) return false;
        if (!stored.equals(hash(oldPassword))) return false;
        this.config.set("auth." + player.getUniqueId(), hash(newPassword));
        this.config.save();
        return true;
    }

    public boolean login(Player player, String password) {
        String stored = this.config.get("auth." + player.getUniqueId());
        if (stored == null) return false;
        if (stored.equals(hash(password))) {
            UUID uuid = player.getUniqueId();
            this.loggedIn.add(uuid);
            cancel(uuid);
            return true;
        }
        return false;
    }

    public void wait(Player player) {
        logout(player);
        if (!isRegistered(player))
            player.sendMessage("§e请使用 /register <密码> <重复密码> 注册");
        else
            player.sendMessage("§e请使用 /login <密码> 登录");
        startLoginTimeout(player);
    }

    public void logout(Player player) {
        UUID uuid = player.getUniqueId();
        this.loggedIn.remove(uuid);
        player.setInvulnerable(false);
        cancel(uuid);
    }

    public long getRegisterTime(UUID uuid) {
        String v = config.get("auth." + uuid + ".register_time");
        return v == null ? -1 : Long.parseLong(v);
    }

    private String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String s = Integer.toHexString(0xff & b);
                if (s.length() == 1) hex.append('0');
                hex.append(s);
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

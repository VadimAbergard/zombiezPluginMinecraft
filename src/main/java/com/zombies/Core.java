package com.zombies;

import com.zombies.command.CommandStart;
import com.zombies.trigger.Trigger;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Core extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("start").setExecutor(new CommandStart());
        Game.init(this);
        Trigger.init(this);

        getServer().getPluginManager().registerEvents(new ServerHandler(this), this);
        getServer().getScheduler().runTaskTimer(this, () -> {
            for (int i = 0; i < 3; i++) {
                spawnParticle(Particle.EXPLOSION_NORMAL, getLocation(5, 5 + i, 5), 5);
            }
        },0L, 2);
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void sendMessgeSomebody(String message) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }
    public static void teleportSomebody(Location loc) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(loc);
        }
    }

    public static Location getLocation(float x, float y, float z) {
        return new Location(Bukkit.getWorld("world"), x, y, z);
    }

    public static void stopMusic() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopsound @a music");
    }

    public static void playMusic(String musicName) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound " + musicName + " music @a 0 0 0 1000000");
    }

    public static void sendActionBar(String message) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        }
    }

    public static void sendSound(Sound sound) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, 20, 1);
        }
    }

    public static void spawnParticle(Particle particle, Location location, int count) {
        Bukkit.getWorld("world").spawnParticle(particle, location, count, 0, 0, 0, 0.1f);
    }

    public static void giveItem(String namePlayer, String nameItem, int count) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "give " + namePlayer + " " + nameItem + " " + count);
    }

    public static void setBlock(Material material, Location... location) {
        for(int i = 0;i < location.length;i++) {
            Bukkit.getWorld("world").getBlockAt(location[i]).setType(material);
        }
    }

    public static void givePotion(Player player, PotionEffectType potionEffect, int duration, int amplifer) {
        player.addPotionEffect(new PotionEffect(potionEffect, duration, amplifer, true, false, false));
    }
}

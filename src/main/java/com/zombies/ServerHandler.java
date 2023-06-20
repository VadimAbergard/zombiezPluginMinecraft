package com.zombies;

import com.zombies.trigger.Trigger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffectType;

public class ServerHandler implements Listener {

    private Core core;

    public ServerHandler(Core plugin) {
        core = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);

        Player player = e.getPlayer();
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        player.removePotionEffect(PotionEffectType.SATURATION);
        Core.givePotion(player, PotionEffectType.NIGHT_VISION, 999999999, 1);
        Core.givePotion(player, PotionEffectType.DAMAGE_RESISTANCE, 999999999, 1);
        Core.givePotion(player, PotionEffectType.SATURATION, 999999999, 10);

        if(Game.isPlaying()) {
            for(Player _player : Bukkit.getOnlinePlayers()) {
                player.teleport(_player);
                break;
            }
        } else {
            player.teleport(Game.getLobbyLocation());
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();

        if(Game.isPlayerLay(player.getName())) {
            e.setCancelled(true);
        } else if(player.getHealth() - e.getDamage() <= 0) {
            player.setHealth(1);
            e.setCancelled(true);
            Game.setPlayerLays(player.getName(), true);
            Core.sendMessgeSomebody(Core.color("&e&Рация: &e" + player.getName() + " &cупал! поднемите его!"));
        }
    }


    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        e.getDrops().clear();
        e.setDroppedExp(0);
        Core.giveItem(e.getEntity().getKiller().getName(), "potato", 10);
    }


    @EventHandler
    public void onPlayerPickUpItem(PlayerPickupItemEvent e) {
        Trigger.handlePickUp(e.getItem().getItemStack().getType());
    }


    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        e.setCancelled(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE));
    }


    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        e.setCancelled(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        Trigger.handleMove(player.getLocation());

        if(Game.isPlayerLay(player.getName())) {
            Location loc = player.getLocation();
            loc.setPitch(-180);
            player.teleport(loc);

            player.sendTitle("", Core.color("&cнажмите &eF &cчтобы позвать на помощь!"), 0, 100, 0);
        } else {
            Game.updateSpawnPoints();
        }
    }

    @EventHandler
    public void onInputF(PlayerSwapHandItemsEvent e) {
        e.setCancelled(true);

        Player player = e.getPlayer();

        if(Game.isPlayerLay(player.getName())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/stopsound @a ambient minecraft:voice_help");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:voice_help ambient @a 0 0 0 1000000");
        } else {
            for(Player _player : Bukkit.getOnlinePlayers()) {
                if(Game.isPlayerLay(player.getName())) {
                    if(player.getLocation().distance(_player.getLocation()) < 3)
                        Game.setPlayerLays(_player.getName(), false);
                }
            }
        }

        Trigger.handleF(player);
    }

}

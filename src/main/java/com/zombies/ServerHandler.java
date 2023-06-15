package com.zombies;

import com.zombies.map.TypeMap;
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
import org.bukkit.inventory.ItemStack;
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

        //Game.updatePurpose("что-то");
        //Game.start(TypeMap.DEPOT);
        //Trigger.nextStage("стадиия 1", Material.DIAMOND_SWORD);
        //Trigger.nextStageF("стадиия 1", 4, 4, 4, "stone");

        /*core.getServer().getScheduler().runTaskLater(core, () -> {
            Game.updatePurpose("что-то 2");
        },40L);*/
        /*BossBar bar = Bukkit.createBossBar(Core.color("цель: &eнет"), BarColor.WHITE, BarStyle.SOLID);
        bar.setProgress(0);
        bar.addPlayer(player);

        for(int i = 1;i <= 20;i++) {
            int finalI = i;
            core.getServer().getScheduler().runTaskLater(core, () -> {
                bar.setProgress(finalI / 20f);
            }, i);
        }*/

        /*core.getServer().getScheduler().runTaskTimer(core, () -> {
            //Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "playsound minecraft:code_silver music @a 0 0 0 1000000");
        },0L, 20L*//*5351*//*);*/
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
    public void onPlayerRBM(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        //ItemStack itemInHand = player.getItemInHand();
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

package com.zombies.trigger;

import com.zombies.Core;
import com.zombies.Game;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Trigger {

    private static Core core;
    private static TypeTrigger trigger;
    private static Location triggerLocation;
    private static Location triggerLocationBlock;
    private static String nameRewardItem;
    private static Material triggerItem;
    private static int stage = -1;

    public static void init(Core plugin) {
        core = plugin;
    }

    public static void handleMove(Location playerLocation) {
        if(!trigger.equals(TypeTrigger.MOVE)) return;

        switch (Game.getTypeMap()) {
            case HOSPITAL:
                switch (stage) {
                    case 0:
                        if(playerLocation.distance(triggerLocation) < 3) {
                            nextStage("проберитесь на крышу", 143, 117, 107);
                            Game.startAction();
                        }
                        break;
                    case 1:
                        if(playerLocation.distance(triggerLocation) < 3) {
                            Location[] locs = {Core.getLocation(145, 116, 102), Core.getLocation(142, 115, 94),
                                    Core.getLocation(147, 115, 88), Core.getLocation(113, 115, 85),
                                    Core.getLocation(139, 115, 86), Core.getLocation(128, 115, 83),
                                    Core.getLocation(122, 115, 95), Core.getLocation(123, 115, 89),
                                    Core.getLocation(135, 116, 100), Core.getLocation(125, 115, 99),
                                    Core.getLocation(136, 115, 107), Core.getLocation(120, 115, 110),
                                    Core.getLocation(132, 115, 115), Core.getLocation(137, 115, 109),
                                    Core.getLocation(126, 115, 125), Core.getLocation(118, 115, 123),
                                    Core.getLocation(124, 115, 123), Core.getLocation(108, 115, 120),
                                    Core.getLocation(110, 115, 114), Core.getLocation(111, 115, 107),
                                    Core.getLocation(111, 115, 101)};
                            int index = (int)(Math.random() * locs.length);
                            System.out.println(locs[index].getBlockX() + " " + locs[index].getBlockY() + " " + locs[index].getBlockZ());
                            nextStageF("найдите термит", locs[index].getBlockX(), locs[index].getBlockY(), locs[index].getBlockZ(), "gunpowder");
                        }
                        break;
                    case 3:
                        if(playerLocation.distance(triggerLocation) < 3) {
                            Game.updatePurpose("подождите термит");
                            BukkitTask taskParticle = core.getServer().getScheduler().runTaskTimer(core, () -> {
                                for(int i = 0;i < 12;i++) {
                                    Core.spawnParticle(Particle.EXPLOSION_NORMAL , Core.getLocation(148, 118 + (0.25f * i), 112), 2);
                                }
                            }, 0, 10L);
                            core.getServer().getScheduler().runTaskLater(core, () -> {
                                nextStage("выбирайтесь", 154, 140, 107);
                                taskParticle.cancel();
                                Core.setBlock(Material.AIR, Core.getLocation(148, 118, 111), Core.getLocation(148, 119, 111),
                                        Core.getLocation(148, 120, 111), Core.getLocation(148, 118, 112),
                                        Core.getLocation(148, 119, 112), Core.getLocation(148, 120, 112));
                            }, 20L * 40);
                            triggerLocation = Core.getLocation(0, 0, 0);
                        }
                        break;
                    case 4:
                        if(playerLocation.distance(triggerLocation) < 3)Game.stop();
                        break;
                }
                break;
            case DEPOT:
                switch (stage) {
                    case 0:
                        if (playerLocation.distance(triggerLocation) < 3) {
                            nextStage("найдите выход в депо", 77, 59, 172);
                            Game.startAction();
                        }
                        break;
                    case 1:
                        if (playerLocation.distance(triggerLocation) < 3) {
                            nextStageF("найдите динамит", 77, 54, 172, "tnt");
                        }
                        break;
                    case 3:
                        if (playerLocation.distance(triggerLocation) < 3) {
                            Core.setBlock(Material.AIR, Core.getLocation(63, 59, 172), Core.getLocation(63, 60, 172),
                                    Core.getLocation(63, 59, 171), Core.getLocation(63, 60, 171),
                                    Core.getLocation(63, 61, 171), Core.getLocation(63, 59, 170),
                                    Core.getLocation(63, 60, 170));
                            Core.sendSound(Sound.ENTITY_TNT_PRIMED);
                            nextStageF("возьмите еду", 63, 66, 159, "tnt");
                        }
                        break;
                    case 6:
                        if (playerLocation.distance(triggerLocation) < 3) {
                            Game.updatePurpose("подождите термит");
                            BukkitTask taskParticle = core.getServer().getScheduler().runTaskTimer(core, () -> {
                                for(int i = 0;i < 12;i++) {
                                    Core.spawnParticle(Particle.EXPLOSION_NORMAL , Core.getLocation(69, 66 + (0.25f * i), 150), 2);
                                }
                            }, 0, 10L);
                            core.getServer().getScheduler().runTaskLater(core, () -> {
                                nextStage("выбирайтесь", 68, 66, 148);
                                taskParticle.cancel();
                                Core.setBlock(Material.AIR, Core.getLocation(69, 66, 150), Core.getLocation(69, 67, 150));
                            }, 20L * 40);
                            triggerLocation = Core.getLocation(0, 0, 0);

                        }
                        break;
                    case 7:
                        if (playerLocation.distance(triggerLocation) < 3) {
                            Game.stop();
                        }
                        break;
                }
                break;
        }
    }

    public static void handlePickUp(Material material) {
        if(!trigger.equals(TypeTrigger.PICK_UP)) return;

        switch (Game.getTypeMap()) {
            case HOSPITAL:
                switch (stage) {}
                break;
        }
    }

    public static void handleF(Player player) {
        if(!trigger.equals(TypeTrigger.F)) return;

        switch (Game.getTypeMap()) {
            case HOSPITAL:
                switch (stage) {
                    case 2:
                        if(isclickblock(player)) {
                            nextStage("положите термит к баррикаде", 147, 119, 111);
                            Core.sendMessgeSomebody(Core.color("&b&l" + player.getName() + "&f - нашёл, нашёл!"));
                        } else player.sendMessage("пусто...");
                        break;
                }
                break;
            case DEPOT:
                switch (stage) {
                    case 2:
                        if(isclickblock(player)) {
                            nextStage("положите к белой стене", 64, 59, 171);
                        }
                        break;
                    case 4:
                        if(isclickblock(player)) {
                            Location[] locs = {Core.getLocation(67, 66, 157), Core.getLocation(73, 66, 158),
                                    Core.getLocation(67, 66, 164), Core.getLocation(67, 67, 164)};
                            int index = (int)(Math.random() * locs.length);
                            nextStageF("найдите термит в бочках", locs[index].getBlockX(), locs[index].getBlockY(), locs[index].getBlockZ(), "");
                        }
                        break;
                    case 5:
                        if(isclickblock(player)) {
                            nextStage("разложите термит", 69, 66, 151);
                        }
                        break;
                }
                break;
        }
    }

    private static boolean isclickblock(Player player) {
        return player.getTargetBlock(null, 3).getLocation().getBlockX() == triggerLocationBlock.getBlockX() &&
                player.getTargetBlock(null, 3).getLocation().getBlockY() == triggerLocationBlock.getBlockY() &&
                player.getTargetBlock(null, 3).getLocation().getBlockZ() == triggerLocationBlock.getBlockZ();
    }

    private static void giveReward(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "give " + player.getName() + " " + nameRewardItem);
    }

    public static void nextStage(String purpose, float x, float y, float z) {
        trigger = TypeTrigger.MOVE;
        triggerLocation = new Location(Bukkit.getWorld("world"), x, y, z);
        Game.updatePurpose(purpose);
        stage++;
    }

    public static void nextStageF(String purpose, float x, float y, float z, String nameItem) {
        nameRewardItem = nameItem;
        trigger = TypeTrigger.F;
        triggerLocationBlock = new Location(Bukkit.getWorld("world"), x, y, z);
        Game.updatePurpose(purpose);
        stage++;
    }

    public static void nextStage(String purpose, Material item) {
        trigger = TypeTrigger.PICK_UP;
        triggerItem = item;
        Game.updatePurpose(purpose);
        stage++;
    }

    public static void clearStage() {
        stage = -1;
    }
}

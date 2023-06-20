package com.zombies;

import com.zombies.map.TypeMap;
import com.zombies.music.TypeMusic;
import com.zombies.trigger.Trigger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Game {

    private static Core core;
    private static boolean isPlaying;
    private static ArrayList<Location> spawnPointEnemy;
    private static ArrayList<Player> players;
    private static Map<String, Boolean> playerLays;
    private static BossBar bar;
    private static final Location lobbyLoc = new Location(Bukkit.getWorld("world"), 74, 87, 108);
    private static TypeMap typeMap;
    private static TypeMusic typeMusic;
    private static boolean isAssault;
    private static boolean startAction;
    private static BukkitTask taskMusicControl;
    private static BukkitTask taskAnumatedActionBar;
    private static BukkitTask task;

    public static void init(Core plugin) {
        core = plugin;
        playerLays = new HashMap<>();
        spawnPointEnemy = new ArrayList<>();
        players = new ArrayList<>();
    }

    public static void start(TypeMap map) {
        isPlaying = true;
        typeMap = map;
        int maxSpawnedEnemy = 0;
        spawnPointEnemy.clear();

        killEntity();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give @a cgm:assault_rifle");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give @a cgm:basic_bullet 64");

        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setHealth(20);
        }

        players.addAll(Bukkit.getOnlinePlayers());

        switch (map) {
            case HOSPITAL:
                maxSpawnedEnemy = 20 + ((Bukkit.getOnlinePlayers().size() - 1) * 8);
                if(players.size() == 1) dialog(60, "&b&l" + players.get(0).getName() + "&f - это болльница?",
                        "&b&llРАЦИЯ &f- &fне знаю, но тебя окружили, больница единственный твой путь!",
                        "&b&l" + players.get(0).getName() + " &f- &fпонял, иду.");
                else dialog(20, "&b&l" + players.get(0).getName() + " &f- &fэто болльница?",
                        "&b&l" + players.get(1).getName() + " &f- &fда. Как нас сюда занесло?",
                        "&b&lРАЦИЯ &f- &fребят! нет времени, они вас окружили, больница единственный ваш путь.",
                        "&b&l" + players.get(0).getName() + "&f - &fпонял, идём.");
                Core.setBlock(Material.OBSIDIAN, Core.getLocation(148, 118, 111), Core.getLocation(148, 119, 111),
                        Core.getLocation(148, 120, 111), Core.getLocation(148, 118, 112),
                        Core.getLocation(148, 119, 112), Core.getLocation(148, 120, 112));
                Trigger.nextStage("войдите в здание", 99, 101, 97);
                playControl(TypeMusic.CODE_SILVER_2018);
                Core.teleportSomebody(Core.getLocation(88, 101, 103));
                spawnEnemy(Core.getLocation(102, 101, 102));
                spawnEnemy(Core.getLocation(101, 101, 106));
                spawnEnemy(Core.getLocation(103, 101, 111));
                spawnEnemy(Core.getLocation(110, 105, 109));
                spawnEnemy(Core.getLocation(114, 105, 97));
                spawnEnemy(Core.getLocation(120, 105, 99));
                spawnEnemy(Core.getLocation(120, 105, 107));
                spawnEnemy(Core.getLocation(116, 106, 112));
                spawnEnemy(Core.getLocation(110, 110, 117));
                spawnEnemy(Core.getLocation(114, 109, 105));
                spawnEnemy(Core.getLocation(106, 109, 100));
                spawnEnemy(Core.getLocation(117, 112, 98));
                spawnPointEnemy.add(Core.getLocation(109, 119, 102));
                spawnPointEnemy.add(Core.getLocation(117, 119, 107));
                spawnPointEnemy.add(Core.getLocation(119, 119, 124));
                spawnPointEnemy.add(Core.getLocation(123, 119, 113));
                spawnPointEnemy.add(Core.getLocation(128, 119, 117));
                spawnPointEnemy.add(Core.getLocation(127, 119, 111));
                spawnPointEnemy.add(Core.getLocation(130, 119, 106));
                spawnPointEnemy.add(Core.getLocation(133, 119, 96));
                spawnPointEnemy.add(Core.getLocation(123, 119, 93));
                spawnPointEnemy.add(Core.getLocation(136, 119, 91));
                spawnPointEnemy.add(Core.getLocation(144, 122, 114));
                break;
            case DEPOT:
                maxSpawnedEnemy = 10 + ((Bukkit.getOnlinePlayers().size() - 1) * 8);
                if(players.size() == 1) dialog(60, "&b&l" + players.get(0).getName() + "&f - зачем мы вообще тут?",
                        "&b&llРАЦИЯ &f- &fзапасов еды совсем нету.",
                        "&b&l" + players.get(0).getName() + " &f- &fпочему ты мне сказал об этом именно сечяс?",
                        "&b&llРАЦИЯ &f- &fсечяс!? Я говорил тебе ты как обычно не слушал меня.",
                        "&b&l" + players.get(0).getName() + " &f- &f...");
                else dialog(20, "&b&l" + players.get(0).getName() + " &f- зачем мы вообще тут?",
                        "&b&l" + players.get(1).getName() + " &f- &fой, как обычно ты не слушал...",
                        "&b&l" + players.get(0).getName() + "&f - &fдаладно тебе",
                        "&b&l" + players.get(1).getName() + " &f- &fнам сказали сюда прити за припасами");
                Core.setBlock(Material.POLISHED_DIORITE, Core.getLocation(63, 59, 172), Core.getLocation(63, 60, 172),
                        Core.getLocation(63, 59, 171), Core.getLocation(63, 60, 171),
                        Core.getLocation(63, 61, 171), Core.getLocation(63, 59, 170),
                        Core.getLocation(63, 60, 170));
                Trigger.nextStage("найдите вагоны", 108, 56, 187);
                playControl(TypeMusic.DEAD_EYE);
                Core.setBlock(Material.IRON_BARS, Core.getLocation(69, 66, 150), Core.getLocation(69, 67, 150));
                Core.teleportSomebody(Core.getLocation(104, 64, 213));
                spawnEnemy(Core.getLocation(106, 60, 205));
                spawnEnemy(Core.getLocation(107, 60, 201));
                spawnEnemy(Core.getLocation(107, 57, 195));
                spawnEnemy(Core.getLocation(96, 54, 184));
                spawnEnemy(Core.getLocation(94, 55, 180));
                spawnEnemy(Core.getLocation(83, 53, 183));
                spawnEnemy(Core.getLocation(85, 54, 183));
                spawnEnemy(Core.getLocation(82, 54, 179));
                spawnEnemy(Core.getLocation(78, 55, 182));
                spawnEnemy(Core.getLocation(87, 54, 171));
                spawnEnemy(Core.getLocation(82, 54, 175));
                spawnEnemy(Core.getLocation(77, 54, 175));
                spawnEnemy(Core.getLocation(79, 55, 169));
                spawnEnemy(Core.getLocation(66, 59, 170));
                spawnEnemy(Core.getLocation(67, 59, 175));
                spawnEnemy(Core.getLocation(70, 59, 172));
                spawnEnemy(Core.getLocation(74, 59, 169));
                spawnEnemy(Core.getLocation(82, 59, 175));
                spawnEnemy(Core.getLocation(81, 59, 171));
                spawnEnemy(Core.getLocation(77, 59, 174));
                break;
        }

        int finalMaxSpawnedEnemy = maxSpawnedEnemy;
        task = core.getServer().getScheduler().runTaskTimer(core, () -> {
            if(spawnPointEnemy.isEmpty() || !isAssault) return;

            if(Bukkit.getWorld("world").getEntities().size() >= finalMaxSpawnedEnemy + Bukkit.getOnlinePlayers().size()) return;

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give @a cgm:basic_bullet 64");

            //spawn
            Location location = spawnPointEnemy.get((int)(Math.random() * spawnPointEnemy.size()));
            spawnEnemy(location);
            if(Bukkit.getWorld("world").getEntities().size() < finalMaxSpawnedEnemy / 2) {
                location = spawnPointEnemy.get((int)(Math.random() * spawnPointEnemy.size()));
                spawnEnemy(location);
            }
        },0L, 20L * 9);
    }

    public static void stop() {
        task.cancel();
        spawnPointEnemy.clear();
        taskAnumatedActionBar.cancel();
        players.clear();
        killEntity();
        Core.teleportSomebody(lobbyLoc);
        Core.sendActionBar("");
        Trigger.clearStage();

        for(Player player : Bukkit.getOnlinePlayers()) {
            bar.removePlayer(player);
            player.setHealth(20);
            player.getInventory().clear();
        }

        Core.stopMusic();
        startAction = false;
        isPlaying = false;
    }

    private static void playControl(TypeMusic musicType) {
        if(!isPlaying) return;
        typeMusic = musicType;
        if(taskAnumatedActionBar != null) taskAnumatedActionBar.cancel();


        switch (musicType) {
            case CODE_SILVER_2018:
                long timeControl = 1173L;
                Core.playMusic("code_silver_control");
                 taskMusicControl = core.getServer().getScheduler().runTaskLater(core, () -> {
                    if(!isPlaying) return;
                    if(!startAction) playControl(musicType);
                }, timeControl);
                break;
            case DEAD_EYE:
                long timeControl2 = 281L;
                Core.playMusic("dead_eye_control");
                 taskMusicControl = core.getServer().getScheduler().runTaskLater(core, () -> {
                    if(!isPlaying) return;
                    if(!startAction) playControl(musicType);
                }, timeControl2);
                break;
        }
    }

    private static void playAssault() {
        if(!isPlaying) return;
        taskMusicControl.cancel();
        Core.stopMusic();

        switch (typeMusic) {
            case CODE_SILVER_2018:
                long timeWait = 586L;
                long timeAssault = 5350L;

                setAssault(true);
                Core.playMusic("code_silver_wait");

                core.getServer().getScheduler().runTaskLater(core, () -> {
                    if(!isPlaying) return;

                    Core.playMusic("code_silver_assault");
                    actionBarAnimated();
                    core.getServer().getScheduler().runTaskLater(core, () -> {
                        if(!isPlaying) return;
                        setAssault(false);
                        playControl(typeMusic);
                    },timeAssault);

                }, timeWait);
                break;
            case DEAD_EYE:
                long timeWait2 = 680L;
                long timeAssault2 = 3780L;

                setAssault(true);
                Core.playMusic("dead_eye_wait");

                core.getServer().getScheduler().runTaskLater(core, () -> {
                    if(!isPlaying) return;

                    Core.playMusic("dead_eye_assault");
                    actionBarAnimated();
                    core.getServer().getScheduler().runTaskLater(core, () -> {
                        if(!isPlaying) return;
                        setAssault(false);
                        playControl(typeMusic);
                    },timeAssault2);

                }, timeWait2);
                break;
        }
    }

    private static void setAssault(boolean assault) {
        isAssault = assault;
    }

    public static void startAction() {
        if(!startAction) playAssault();
        startAction = true;
    }

    private static void actionBarAnimated() {
        String text = "осада в процессе";
        char[] words = text.toCharArray();
        AtomicInteger i = new AtomicInteger();
        AtomicReference<String> resultText = new AtomicReference<>("");
        taskAnumatedActionBar = core.getServer().getScheduler().runTaskTimer(core, () -> {
            if(i.get() < words.length) resultText.set(resultText.get() + words[i.getAndIncrement()]);
            Core.sendActionBar(Core.color("&e&l" + resultText.get()));
        }, 0L, 2L);
    }


    public static void updateSpawnPoints() {
        switch (typeMap) {
            case HOSPITAL:
                break;
            case DEPOT:
                if(players.get(0).getLocation().distance(Core.getLocation(108, 54, 183)) < 3) {
                    spawnPointEnemy.clear();
                    spawnPointEnemy.add(Core.getLocation(101, 58, 185));
                    spawnPointEnemy.add(Core.getLocation(94, 55, 177));
                } else if(players.get(0).getLocation().distance(Core.getLocation(92, 54, 181)) < 3) {
                    spawnPointEnemy.clear();
                    spawnPointEnemy.add(Core.getLocation(78, 55, 182));
                    spawnPointEnemy.add(Core.getLocation(86, 55, 179));
                } else if(players.get(0).getLocation().distance(Core.getLocation(79, 55, 176)) < 3) {
                    spawnPointEnemy.clear();
                    spawnPointEnemy.add(Core.getLocation(87, 54, 171));
                    spawnPointEnemy.add(Core.getLocation(78, 55, 169));
                } else if(players.get(0).getLocation().distance(Core.getLocation(84, 59, 172)) < 3) {
                    spawnPointEnemy.clear();
                    spawnPointEnemy.add(Core.getLocation(77, 59, 174));
                    spawnPointEnemy.add(Core.getLocation(70, 61, 177));
                }else if(players.get(0).getLocation().distance(Core.getLocation(61, 66, 162)) < 3) {
                    spawnPointEnemy.clear();
                    spawnPointEnemy.add(Core.getLocation(59, 69, 161));
                    spawnPointEnemy.add(Core.getLocation(70, 70, 158));
                    spawnPointEnemy.add(Core.getLocation(67, 70, 166));
                }
                break;
        }
    }

    private static void setSpawnPointEnemy(Location... location) {
        spawnPointEnemy.clear();
        spawnPointEnemy.addAll(Arrays.asList(location));
    }

    public static boolean isPlaying() {
        return isPlaying;
    }

    public static Location getLobbyLocation() {
        return lobbyLoc;
    }

    public static boolean isPlayerLay(String namePlayer) {
        try {
            return playerLays.get(namePlayer);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static void setPlayerLays(String namePlayer, boolean lay) {
        playerLays.put(namePlayer, lay);
    }

    public static void updatePurpose(String purpose) {
        BossBar newBar = Bukkit.createBossBar(Core.color("цель: &e" + purpose), BarColor.WHITE, BarStyle.SOLID);
        newBar.setProgress(0);
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(bar != null) bar.removePlayer(player);
            newBar.addPlayer(player);
        }
        bar = newBar;

        for(int i = 1;i <= 20;i++) {
            int finalI = i;
            core.getServer().getScheduler().runTaskLater(core, () -> {
                newBar.setProgress(finalI / 20f);
            }, i);
        }
    }

    public static TypeMap getTypeMap() {
        return typeMap;
    }

    private static void spawnEnemy(Location location) {
        String[] zombie = {"zombie_extreme:radioactive_zombie", "zombie_extreme:drawnedzombie"};
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                "summon " + zombie[(int)(Math.random() * zombie.length)] + " " + (location.getBlockX() + 0.5f) + " " + location.getBlockY() + " " +
                        (location.getBlockZ()+ 0.5f));
    }

    public static void dialog(long time, String... message) {
        for (int i = 0; i < message.length; i++) {
            int finalI = i;
            task = core.getServer().getScheduler().runTaskLater(core, () -> {
                Core.sendMessgeSomebody(Core.color(message[finalI]));
            }, time * i);
        }
    }

    public static void killEntity() {
        for(Entity entity : Bukkit.getWorld("world").getEntities()) {
            if(!(entity instanceof Player)) {
                entity.remove();
            }
        }
    }
}

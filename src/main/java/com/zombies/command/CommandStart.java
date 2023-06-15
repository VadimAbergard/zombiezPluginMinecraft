package com.zombies.command;

import com.zombies.Core;
import com.zombies.Game;
import com.zombies.map.TypeMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStart implements CommandExecutor {

    /*private Core plugin;

    public CommandStart(Core plugin) {
        this.plugin = plugin;
    }*/

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if(Game.isPlaying()) {
            player.sendMessage(Core.color("&cигра уже идёт"));
            return true;
        } else if(args.length == 0) {
            player.sendMessage(Core.color("&cведите название карты:"));
            player.sendMessage(Core.color("- &e/start hospital"));
            player.sendMessage(Core.color("- &e/start depot"));
            return true;
        }

        if(args[0].equalsIgnoreCase("hospital")) {
            Game.start(TypeMap.HOSPITAL);
        } else if(args[0].equalsIgnoreCase("depot")) {
            Game.start(TypeMap.DEPOT);
        }

        return true;
    }
}

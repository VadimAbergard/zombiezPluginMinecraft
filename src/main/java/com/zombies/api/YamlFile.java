package com.zombies.api;

import com.zombies.Core;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlFile {

    private final File informationFile;

    private final FileConfiguration infoFile;


    public YamlFile(Core plugin) {
        informationFile = new File(plugin.getDataFolder(), "Data.yml");
        infoFile = YamlConfiguration.loadConfiguration(informationFile);
    }

    public FileConfiguration getConfig() {
        return infoFile;
    }

    public void save() {
        try {
            this.infoFile.save(informationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

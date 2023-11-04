package me.merunko.holocraft.Rewards;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class UnclaimedConfiguration {

    private final FileConfiguration unclaimed;
    private final Logger logger;

    public UnclaimedConfiguration(FileConfiguration unclaimed, Logger logger) {
        this.unclaimed = unclaimed;
        this.logger = logger;
    }

    public void load() {
        try {
            unclaimed.load(new File("plugins/Submitter/unclaimed.yml"));
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            logger.severe("An error occurred while loading unclaimed.yml file.");
        }
    }

    public void save() {
        try {
            unclaimed.save(new File("plugins/Submitter/unclaimed.yml"));
        } catch (IOException e) {
            logger.severe("An error occurred while saving unclaimed.yml file.");
        }
    }

    public void setPlayer(List<String> player) {
        unclaimed.set("PLAYER", player);
    }

    public List<String> getStringList(String path) {
        return unclaimed.getStringList(path);
    }

}

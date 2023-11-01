package me.merunko.holocraft;

import me.merunko.holocraft.Command.Command;
import me.merunko.holocraft.Configuration.Configuration;
import me.merunko.holocraft.Listener.InventoryCloseListener;
import me.merunko.holocraft.Listener.InventoryInteractListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public final class Submitter extends JavaPlugin {

    Configuration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");
        FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(configFile);
        config = new Configuration(fileConfig);
        config.load();

        Logger logger = getLogger();
        File logFile = new File(getDataFolder(), "logs.txt");
        try {
            if (!logFile.exists()) {
                boolean created = logFile.createNewFile();
                if (!created) {
                    getLogger().warning("Failed to create logs.txt");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Objects.requireNonNull(getCommand("submitter")).setExecutor(new Command(config));
        getServer().getPluginManager().registerEvents(new InventoryInteractListener(config, logFile, logger), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
    }

    @Override
    public void onDisable() {
        config.save();
    }
}

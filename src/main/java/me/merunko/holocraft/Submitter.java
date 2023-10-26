package me.merunko.holocraft;

import me.merunko.holocraft.Command.OpenSubmitter;
import me.merunko.holocraft.Listener.InventoryInteractListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public final class Submitter extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

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

        getCommand("submitter").setExecutor(new OpenSubmitter(config));
        getServer().getPluginManager().registerEvents(new InventoryInteractListener(config, logFile, logger), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

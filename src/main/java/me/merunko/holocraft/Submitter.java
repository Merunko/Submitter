package me.merunko.holocraft;

import me.merunko.holocraft.Command.Command;
import me.merunko.holocraft.Configuration.Configuration;
import me.merunko.holocraft.Leaderboard.Leaderboard;
import me.merunko.holocraft.Leaderboard.LeaderboardConfiguration;
import me.merunko.holocraft.Listener.InventoryCloseListener;
import me.merunko.holocraft.Listener.InventoryInteractListener;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;
import java.util.logging.Logger;

public final class Submitter extends JavaPlugin {

    Configuration config;
    LeaderboardConfiguration leaderboardConfig;
    Calendar calendar;
    private static Submitter plugin;

    public static Submitter getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        Logger logger = getLogger();

        calendar = Calendar.getInstance();
        plugin = this;

        logger.info("==============================================================");
        logger.info("==                                                          ==");
        logger.info("==                                                          ==");
        logger.info("==   OOOOOOOO        OOOO        OOOOOOOO        OOOO       ==");
        logger.info("==   OO     OO     OO    OO      OO     OO     OO    OO     ==");
        logger.info("==   OO     OO    OO      OO     OO     OO    OO      OO    ==");
        logger.info("==   OOOOOOOO    OO        OO    OOOOOOOO    OO        OO   ==");
        logger.info("==   OO           OO      OO     OOO          OO      OO    ==");
        logger.info("==   OO            OO    OO      OO OOO        OO    OO     ==");
        logger.info("==   OO              OOOO        OO    OOO       OOOO       ==");
        logger.info("==                                                          ==");
        logger.info("==                                                          ==");
        logger.info("==============================================================");

        File config_yml = new File(getDataFolder(), "config.yml");
        if (!config_yml.exists()) {
            saveResource("config.yml", false);
            logger.warning("Can't find config.yml, generating config.yml");
            FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(config_yml);
            config = new Configuration(fileConfig);
        } else {
            FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(config_yml);
            config = new Configuration(fileConfig);
        }
        logger.info("Loading config.yml");
        config.load();

        File leaderboard_monthly_yml = new File(getDataFolder(), "leaderboard_monthly.yml");
        if (!leaderboard_monthly_yml.exists()) {
            saveResource("leaderboard_monthly.yml", false);
            logger.warning("Can't find leaderboard_monthly.yml, generating leaderboard_monthly.yml");
            FileConfiguration leaderboardFileConfig = YamlConfiguration.loadConfiguration(leaderboard_monthly_yml);
            leaderboardConfig = new LeaderboardConfiguration(leaderboardFileConfig);
            logger.info("Loading leaderboard_monthly.yml");
            leaderboardConfig.load();
            leaderboardConfig.newLeaderboard(logger, calendar);
        } else {
            FileConfiguration leaderboardFileConfig = YamlConfiguration.loadConfiguration(leaderboard_monthly_yml);
            leaderboardConfig = new LeaderboardConfiguration(leaderboardFileConfig);
            logger.info("Loading leaderboard_monthly.yml");
            leaderboardConfig.load();
        }

        File logFile = new File(getDataFolder(), "logs.txt");
        try {
            if (!logFile.exists()) {
                boolean created = logFile.createNewFile();
                if (!created) {
                    logger.warning("Failed to create logs.txt");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File backupDirectory = new File("plugins/Submitter/backups/");
        backupDirectory.mkdirs();

        Objects.requireNonNull(getCommand("submitter")).setExecutor(new Command(config, plugin));
        getServer().getPluginManager().registerEvents(new InventoryInteractListener(config, logFile, logger), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
        Leaderboard leaderboard = new Leaderboard(logger, config, leaderboardConfig, config.getLeaderboardResetDay(), Integer.parseInt(config.getLeaderboardResetTime().substring(0, 2)), Integer.parseInt(config.getLeaderboardResetTime().substring(2)));
        leaderboard.resetCountdown();
    }

    @Override
    public void onDisable() {

        Logger logger = getLogger();

        leaderboardConfig.save();
        logger.info(ChatColor.GOLD + "Saving leaderboard...");

    }
}

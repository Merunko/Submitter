package me.merunko.holocraft;

import me.merunko.holocraft.Command.Command;
import me.merunko.holocraft.Configuration.MainConfiguration;
import me.merunko.holocraft.Leaderboard.Leaderboard;
import me.merunko.holocraft.Leaderboard.LeaderboardConfiguration;
import me.merunko.holocraft.Listener.InventoryCloseListener;
import me.merunko.holocraft.Listener.PlayerJoinListener;
import me.merunko.holocraft.Listener.RewardsInventoryInteractListener;
import me.merunko.holocraft.Listener.SubmitterInventoryInteractListener;

import me.merunko.holocraft.Rewards.RewardsConfiguration;
import me.merunko.holocraft.Rewards.UnclaimedConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;
import java.util.logging.Logger;

public final class Submitter extends JavaPlugin {

    MainConfiguration mainConfig;
    LeaderboardConfiguration leaderboardConfig;
    RewardsConfiguration rewardsConfig;
    UnclaimedConfiguration unclaimedConfig;
    Leaderboard leaderboard;
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


        logger.info("✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿");
        logger.info("✿✿                                                               ✿✿");
        logger.info("✿✿                                                               ✿✿");
        logger.info("✿✿     OOOOOOOO        OOOO        OOOOOOOO         OOOO         ✿✿");
        logger.info("✿✿     OO      OO    OO    OO      OO      OO     OO    OO       ✿✿");
        logger.info("✿✿     OO      OO   OO      OO     OO      OO    OO      OO      ✿✿");
        logger.info("✿✿     OOOOOOOO    OO        OO    OOOOOOOO     OO        OO     ✿✿");
        logger.info("✿✿     OO           OO      OO     OO            OO      OO      ✿✿");
        logger.info("✿✿     OO            OO    OO      OO OO          OO    OO       ✿✿");
        logger.info("✿✿     OO              OOOO        OO    OO         OOOO         ✿✿");
        logger.info("✿✿                                                               ✿✿");
        logger.info("✿✿                                                               ✿✿");
        logger.info("✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿✿");


        File file_configyml = new File(getDataFolder(), "config.yml");
        if (!file_configyml.exists()) {
            saveResource("config.yml", false);
            logger.warning("Can't find config.yml, generating config.yml");
            FileConfiguration mainConfigFile = YamlConfiguration.loadConfiguration(file_configyml);
            mainConfig = new MainConfiguration(mainConfigFile, logger);
        } else {
            FileConfiguration mainConfigFile = YamlConfiguration.loadConfiguration(file_configyml);
            mainConfig = new MainConfiguration(mainConfigFile, logger);
        }
        logger.info("Loading config.yml");
        mainConfig.load();


        File file_leaderboardyml = new File(getDataFolder(), "leaderboard.yml");
        if (!file_leaderboardyml.exists()) {
            saveResource("leaderboard.yml", false);
            logger.warning("Can't find leaderboard.yml, generating leaderboard.yml");
            FileConfiguration leaderboardFileConfig = YamlConfiguration.loadConfiguration(file_leaderboardyml);
            leaderboardConfig = new LeaderboardConfiguration(leaderboardFileConfig, logger);
            logger.info("Loading leaderboard.yml");
            leaderboardConfig.load();
            leaderboard = new Leaderboard(calendar, logger, mainConfig, leaderboardConfig, rewardsConfig);
            leaderboard.newLeaderboard(logger, calendar);
        } else {
            FileConfiguration leaderboardFileConfig = YamlConfiguration.loadConfiguration(file_leaderboardyml);
            leaderboardConfig = new LeaderboardConfiguration(leaderboardFileConfig, logger);
            logger.info("Loading leaderboard.yml");
            leaderboardConfig.load();
        }


        File file_rewardsyml = new File(getDataFolder(), "rewards.yml");
        if (!file_rewardsyml.exists()) {
            saveResource("rewards.yml", false);
            logger.warning("Can't find rewards.yml, generating rewards.yml");
            FileConfiguration rewardsFileConfig = YamlConfiguration.loadConfiguration(file_rewardsyml);
            rewardsConfig = new RewardsConfiguration(mainConfig, unclaimedConfig, rewardsFileConfig, leaderboardConfig, logger, calendar);
        } else {
            FileConfiguration rewardsFileConfig = YamlConfiguration.loadConfiguration(file_rewardsyml);
            rewardsConfig = new RewardsConfiguration(mainConfig, unclaimedConfig, rewardsFileConfig, leaderboardConfig, logger, calendar);
        }
        logger.info("Loading rewards.yml");
        rewardsConfig.load();

        File file_unclaimedyml = new File(getDataFolder(), "unclaimed.yml");
        if (!file_unclaimedyml.exists()) {
            saveResource("unclaimed.yml", false);
            logger.warning("Can't find unclaimed.yml, generating unclaimed.yml");
            FileConfiguration unclaimedFileConfig = YamlConfiguration.loadConfiguration(file_unclaimedyml);
            unclaimedConfig = new UnclaimedConfiguration(unclaimedFileConfig, logger);
        } else {
            FileConfiguration unclaimedFileConfig = YamlConfiguration.loadConfiguration(file_unclaimedyml);
            unclaimedConfig = new UnclaimedConfiguration(unclaimedFileConfig, logger);
        }
        logger.info("Loading unclaimed.yml");
        unclaimedConfig.load();


        File logFile = new File(getDataFolder(), "logs.txt");
        try {
            if (!logFile.exists()) {
                boolean created = logFile.createNewFile();
                if (!created) {
                    logger.warning("Failed to create logs.txt");
                    logger.warning("You can manually create logs.txt in 'plugins/Submitter' directory");
                }
            }
        } catch (IOException e) {
            logger.severe("An error occurred while creating the log file.");
        }


        File topItemFile = new File(getDataFolder(), "topItem.txt");
        try {
            if (!topItemFile.exists()) {
                boolean created = topItemFile.createNewFile();
                if (!created) {
                    logger.warning("Failed to create topItem.txt");
                    logger.warning("You can manually create topItem.txt in 'plugins/Submitter' directory");
                }
            }
        } catch (IOException e) {
            logger.severe("An error occurred while creating the topItem file.");
        }


        File backupDirectory = new File("plugins/Submitter/backups/");
        if (backupDirectory.mkdirs()) {
            logger.info("Backup directory created.");
        } else {
            logger.warning("Failed to create backup directory or already exist.");
            logger.warning("You can manually create 'backup' directory in 'plugins/Submitter' directory.");
        }


        Objects.requireNonNull(getCommand("submitter")).setExecutor(new Command(rewardsConfig, mainConfig, leaderboardConfig, plugin, logger));
        getServer().getPluginManager().registerEvents(new RewardsInventoryInteractListener(rewardsConfig, logger), this);
        getServer().getPluginManager().registerEvents(new SubmitterInventoryInteractListener(mainConfig, logFile, logger), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(unclaimedConfig, rewardsConfig), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
        leaderboard = new Leaderboard(calendar, logger, mainConfig, leaderboardConfig, rewardsConfig);
        leaderboard.startCountdown();
    }

    @Override
    public void onDisable() {


        Logger logger = getLogger();
        logger.info("Saving leaderboard...");
        leaderboardConfig.save();
        logger.info("Leaderboard saved.");

        logger.info("Saving unclaimed rewards...");
        unclaimedConfig.save();
        logger.info("Unclaimed rewards saved.");


    }
}

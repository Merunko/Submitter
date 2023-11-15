package me.merunko.holocraft.Configuration;

import net.Zrips.CMILib.Colors.CMIChatColor;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class MainConfiguration {

    private final FileConfiguration config;
    Logger logger;

    public MainConfiguration(FileConfiguration config, Logger logger) {
        this.config = config;
        this.logger = logger;
    }

    public void load() {
        try {
            config.load(new File("plugins/Submitter/config.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            logger.severe("An error occurred while loading config.yml file.");
        }
    }

    public boolean getCMILib() {
        if (config.isBoolean("enable_cmilib")) {
            return config.getBoolean("enable_cmilib");
        }
        return false;
    }

    public String getTitle() {
        if (config.isString("title")) {
            return CMIChatColor.translate(config.getString("title"));
        } else {
            return "No Title";
        }
    }

    public boolean getDefaultMsg() {
        if (config.isBoolean("enable_default_msg")) {
            return config.getBoolean("enable_default_msg");
        } else {
            return false;
        }
    }

    public String getDefaultMsgText(Player player, int totalPoints) {
        if (config.isString("default_msg")) {
            return Objects.requireNonNull(CMIChatColor.translate(config.getString("default_msg")))
                    .replace("%player%", player.getName())
                    .replace("%point%", String.valueOf(totalPoints))
                    .replace("%point_name%", getPointName().toLowerCase());
        } else {
            return "No Message";
        }
    }

    public String getPointName() {
        if (config.isString("point_name")) {
            return config.getString("point_name");
        } else {
            return "NoName";
        }
    }

    public boolean getSSBHook() {
        if (config.isBoolean("superior_skyblock_hook")) {
            return config.getBoolean("superior_skyblock_hook");
        } else {
            return false;
        }
    }

    public boolean getSSBSharedPoint() {
        if (config.isBoolean("leaderboard_affect_ssb_island_member")) {
            return config.getBoolean("leaderboard_affect_ssb_island_member");
        } else {
            return false;
        }
    }

    public String getSSBSharedPointName() {
        if (config.isString("shared_point_name")) {
            return config.getString("shared_point_name");
        } else {
            return "NoName";
        }
    }

    public boolean getMMOItemsEnabled() {
        if (config.isBoolean("enable_mmoitems")) {
            return config.getBoolean("enable_mmoitems");
        } else {
            return false;
        }
    }

    public int getMMOItemValue(String itemType, String itemID) {
        if (config.isList("mmoitems." + itemType)) {
            List<String> items = config.getStringList("mmoitems." + itemType);
            for (String item : items) {
                String[] parts = item.split(":");
                if (parts.length == 2) {
                    String id = parts[0].trim();
                    int value = Integer.parseInt(parts[1].trim());
                    if (id.equals(itemID)) {
                        return value;
                    }
                }
            }
        }
        return 0;
    }

    public int getMinecraftItemValue(String itemName) {
        if (config.isConfigurationSection("minecraft_items")) {
            String key = "minecraft_items." + itemName;
            if (config.isInt(key)) {
                return config.getInt(key);
            }
        }
        return 0;
    }

    public List<String> getCommands(Player player, int totalPoints) {
        if (config.isList("commands")) {
            List<String> commands = config.getStringList("commands");
            List<String> replacedCommands = new ArrayList<>();

            for (String command : commands) {
                String replacedCommand = command
                        .replace("%player%", player.getName())
                        .replace("%point%", String.valueOf(totalPoints))
                        .replace("%point_name%", getPointName().toLowerCase());

                replacedCommands.add(replacedCommand);
            }

            return replacedCommands;
        }

        return Collections.emptyList();
    }

    public int getLeaderboardResetDay() {
        return config.getInt("leaderboard_reset_date");
    }

    public String getLeaderboardResetTime() {
        return config.getString("leaderboard_reset_time");
    }

}

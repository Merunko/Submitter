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

public class Configuration {

    private final FileConfiguration config;

    public Configuration(FileConfiguration config) {
        this.config = config;
    }

    public void load() {
        try {
            config.load(new File("plugins/Submitter/config.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            config.save("plugins/Submitter/config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getCMILib() {
        if (config.isBoolean("enable_cmilib")) {
            return config.getBoolean("enable_cmilib");
        }
        return false;
    }

    public String getTitleHookCMILib() {
        if (config.isString("title_w_cmilib")) {
            return CMIChatColor.translate(config.getString("title_w_cmilib"));
        } else {
            return "No Title";
        }
    }

    public String getTitleNoCMILib() {
        if (config.isString("title_w/o_cmilib")) {
            return config.getString("title_w/o_cmilib").replace("&", "§");
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

    public String getDefaultMsgHookCMILib(Player player, int totalPoints) {
        if (config.isString("default_msg_w_cmilib")) {
            return CMIChatColor.translate(config.getString("default_msg_w_cmilib"))
                    .replace("%player%", player.getName())
                    .replace("%point%", String.valueOf(totalPoints))
                    .replace("%point_name%", getPointName().toLowerCase());
        } else {
            return "No Message";
        }
    }

    public String getDefaultMsgNoCMILib(Player player, int totalPoints) {
        if (config.isString("default_msg_w/o_cmilib")) {
            return config.getString("default_msg_w/o_cmilib")
                    .replace("%player%", player.getName())
                    .replace("%point%", String.valueOf(totalPoints))
                    .replace("%point_name%", getPointName().toLowerCase())
                    .replace("&", "§");
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

}

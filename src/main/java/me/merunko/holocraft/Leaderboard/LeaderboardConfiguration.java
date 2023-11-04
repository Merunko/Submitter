package me.merunko.holocraft.Leaderboard;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Set;
import java.util.logging.Logger;

public class LeaderboardConfiguration {

    private final FileConfiguration leaderboard;
    Logger logger;

    public LeaderboardConfiguration(FileConfiguration leaderboard, Logger logger) {
        this.leaderboard = leaderboard;
        this.logger = logger;
    }

    public void load() {
        try {
            leaderboard.load(new File("plugins/Submitter/leaderboard.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            logger.severe("An error occurred while loading leaderboard.yml file.");
        }
    }

    public void save() {
        try {
            leaderboard.save(new File("plugins/Submitter/leaderboard.yml"));
        } catch (IOException e) {
            logger.severe("An error occurred while saving leaderboard.yml file.");
        }
    }

    public void setStringSection(String path, String sectionName) {
        leaderboard.set(path, sectionName);
    }

    public void setIntSection(String path, int sectionInt) {
        leaderboard.set(path, sectionInt);
    }

    public @NotNull Set<String> getSectionKey(boolean b) {
        return leaderboard.getKeys(b);
    }

    public String getCurrentMonth(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH);

        String[] monthNames = {
                "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"
        };

        if (month < monthNames.length) {
            return monthNames[month];
        }

        return "UNKNOWN";
    }

    public String getPreviousMonth(Calendar calendar) {
        calendar.add(Calendar.MONTH, -1);
        int month = calendar.get(Calendar.MONTH);
        String[] monthNames = {
                "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"
        };

        if (month < monthNames.length) {
            return monthNames[month];
        }

        return "UNKNOWN";
    }

    public void setTopPlayer(String month, int position, String playerName, int points) {
        String section = month + ".TOP" + position;
        leaderboard.set(section + ".Player", playerName);
        leaderboard.set(section + ".Points", points);
    }

    public String getTopPlayer(String month, int position) {
        String section = month + ".TOP" + position;
        String playerName = leaderboard.getString(section + ".Player");
        int points = leaderboard.getInt(section + ".Points");
        return playerName + " - " + points + " points - ";
    }

}

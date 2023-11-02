package me.merunko.holocraft.Leaderboard;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Logger;

public class LeaderboardConfiguration {

    private final FileConfiguration leaderboard;

    public LeaderboardConfiguration(FileConfiguration leaderboard) {
        this.leaderboard = leaderboard;
    }

    public void load() {
        try {
            leaderboard.load(new File("plugins/Submitter/leaderboard_monthly.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            leaderboard.save(new File("plugins/Submitter/leaderboard_monthly.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newLeaderboard(Logger logger, Calendar calendar) {
        String currentMonth = getCurrentMonth(calendar);

        logger.warning("Detected new leaderboard_monthly.yml file");
        logger.warning("This could be monthly reset or a first-time startup");
        logger.warning("Setting up leaderboard_monthly.yml");

        for (int i = 1; i <= 7; i++) {
            String section = "TOP" + i;
            leaderboard.set(currentMonth + "." + section + ".Player", "null");
            leaderboard.set(currentMonth + "." + section + ".Points", 0);
            leaderboard.set(currentMonth + "." + section + ".Most Submitted Item", "null");
        }
        save();
    }

    public void fixLeaderboard(Logger logger, Calendar calendar) {
        String currentMonth = getCurrentMonth(calendar);

        for (String monthName : leaderboard.getKeys(false)) {
            if (!currentMonth.equals(monthName)) {
                logger.warning("Detected mismatch month in leaderboard_monthly.yml file");
                logger.warning("This could be a monthly reset or just a mismatch");
                logger.warning("Fixing up leaderboard_monthly.yml");

                for (int i = 1; i <= 7; i++) {
                    String section = "TOP" + i;
                    leaderboard.set(monthName + "." + section + ".Player", "null");
                    leaderboard.set(monthName + "." + section + ".Points", 0);
                    leaderboard.set(monthName + "." + section + ".Most Submitted Item", "null");
                }

                newLeaderboard(logger, calendar);
            }
        }
    }

    public String getCurrentMonth(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH);

        String[] monthNames = {
                "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"
        };

        if (month >= 0 && month < monthNames.length) {
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

        if (month >= 0 && month < monthNames.length) {
            return monthNames[month];
        }

        return "UNKNOWN";
    }

    public void setTopPlayer(String month, int position, String playerName, int points, String submittedItem) {
        String section = month + ".TOP" + position;
        leaderboard.set(section + ".Player", playerName);
        leaderboard.set(section + ".Points", points);
        leaderboard.set(section + ".Most Submitted Item", submittedItem);
    }

    public String getTopPlayer(String month, int position) {
        String section = month + ".TOP" + position;
        String playerName = leaderboard.getString(section + ".Player");
        int points = leaderboard.getInt(section + ".Points");
        String submittedItem = leaderboard.getString(section + ".Most Submitted Item");
        return playerName + " - " + points + " points - " + submittedItem;
    }

}

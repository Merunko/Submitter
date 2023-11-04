package me.merunko.holocraft.Leaderboard;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class LeaderboardBackup {


    public void createLeaderboardBackup(Logger logger, LeaderboardConfiguration leaderboard) {
        try {
            File leaderboardFile = new File("plugins/Submitter/leaderboard.yml");
            File backupDirectory = new File("plugins/Submitter/backups/");

            if (leaderboardFile.exists()) {
                Calendar now = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                String currentMonthYear = dateFormat.format(now.getTime());

                String backupFileName = "leaderboard_monthly_backup_" + leaderboard.getPreviousMonth(now) + "_" + currentMonthYear + ".yml";

                File backupFile = new File(backupDirectory, backupFileName);
                org.apache.commons.io.FileUtils.copyFile(leaderboardFile, backupFile);

                logger.info("Created a backup of the monthly leaderboard: " + backupFileName);
            } else {
                logger.warning("Monthly leaderboard file not found. Backup operation aborted.");
            }
        } catch (IOException e) {
            logger.severe("An error occurred while backing up leaderboard.yml file.");
        }
    }
}

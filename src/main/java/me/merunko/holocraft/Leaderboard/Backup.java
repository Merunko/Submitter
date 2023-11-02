package me.merunko.holocraft.Leaderboard;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class Backup {

    public void createLeaderboardBackup(Logger logger, LeaderboardConfiguration leaderboard) {
        try {
            File leaderboardFile = new File("plugins/Submitter/leaderboard_monthly.yml");
            File backupDirectory = new File("plugins/Submitter/backups/");
            backupDirectory.mkdirs();

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
            e.printStackTrace();
        }
    }

    public String getCurrentMonth(Calendar calendar) {
        int currentMonth = calendar.get(Calendar.MONTH);
        currentMonth += 1;

        String[] monthNames = new String[] {
                "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY",
                "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"
        };

        if (currentMonth >= 1 && currentMonth <= 12) {
            return monthNames[currentMonth - 1];
        } else {
            return "INVALID_MONTH";
        }
    }
}

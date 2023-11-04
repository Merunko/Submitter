package me.merunko.holocraft.Logs;

import me.merunko.holocraft.Leaderboard.LeaderboardConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class LogsBackup {

    public void createLogsBackup(Logger logger, LeaderboardConfiguration leaderboard) {
        try {
            File logsFile = new File("plugins/Submitter/logs.txt");
            File backupDirectory = new File("plugins/Submitter/backups/");

            if (logsFile.exists()) {
                Calendar now = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                String currentMonthYear = dateFormat.format(now.getTime());

                String backupFileName = "logs_backup_" + leaderboard.getPreviousMonth(now) + "_" + currentMonthYear + ".txt";

                File backupFile = new File(backupDirectory, backupFileName);
                org.apache.commons.io.FileUtils.copyFile(logsFile, backupFile);

                logger.info("Created a backup of the monthly logs: " + backupFileName);
                resetLogs(logger);
                logger.info("Resetting logs.");
            } else {
                logger.warning("Monthly logs file not found. Backup operation aborted.");
            }
        } catch (IOException e) {
            logger.severe("Failed to create logs backup for previous month.");
        }
    }

    public void resetLogs(Logger logger) {
        String logsFilePath = "plugins/Submitter/logs.txt";

        try {
            FileWriter fileWriter = new FileWriter(logsFilePath);
            fileWriter.close();
        } catch (IOException e) {
            logger.severe("An error occurred while trying to reset logs.txt. Check if the file exist..");
        }
    }
}

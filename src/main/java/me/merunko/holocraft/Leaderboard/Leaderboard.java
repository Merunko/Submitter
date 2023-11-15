package me.merunko.holocraft.Leaderboard;

import me.merunko.holocraft.Configuration.MainConfiguration;
import me.merunko.holocraft.Logs.LogsBackup;
import me.merunko.holocraft.Rewards.RewardsConfiguration;
import me.merunko.holocraft.Submitter;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.logging.Logger;

public class Leaderboard {

    private final Logger logger;
    private final MainConfiguration config;
    private final LeaderboardConfiguration leaderboard;
    private final RewardsConfiguration reward;
    private final LeaderboardBackup leaderboardBackup = new LeaderboardBackup();
    private final LogsBackup logsBackup = new LogsBackup();
    private final LeaderboardUpdater updater;


    public Leaderboard(Logger logger, MainConfiguration config, LeaderboardConfiguration leaderboard, RewardsConfiguration reward, LeaderboardUpdater updater) {
        this.reward = reward;
        this.logger = logger;
        this.config = config;
        this.leaderboard = leaderboard;
        this.updater = updater;
    }

    public void startCountdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Calendar now = Calendar.getInstance();
                int currentDay = now.get(Calendar.DAY_OF_MONTH);
                int currentHour = now.get(Calendar.HOUR_OF_DAY);
                int currentMinute = now.get(Calendar.MINUTE);

                int resetDay = config.getLeaderboardResetDay();
                String resetTime = config.getLeaderboardResetTime();

                if (currentDay == resetDay && currentHour == Integer.parseInt(resetTime.substring(0, 2)) && currentMinute == Integer.parseInt(resetTime.substring(2))) {

                    updater.updateLeaderboard();
                    reward.giveOutRewards();
                    leaderboardBackup.createLeaderboardBackup(logger, leaderboard);
                    fixLeaderboard(logger, now);
                    logsBackup.createLogsBackup(logger, leaderboard);

                    resetLeaderboard(now);
                }
            }
        }.runTaskTimer(Submitter.getPlugin(), 0L, 20L * 60L);
    }


    public void newLeaderboard(Logger logger, Calendar calendar) {
        String currentMonth = leaderboard.getCurrentMonth(calendar);

        logger.warning("Detected new leaderboard.yml file");
        logger.warning("This could be monthly reset or a first-time startup");
        logger.warning("Setting up leaderboard.yml");

        for (int i = 1; i <= 7; i++) {
            String section = "TOP" + i;
            leaderboard.setStringSection(currentMonth + "." + section + ".Player", "null");
            leaderboard.setIntSection(currentMonth + "." + section + ".Points", 0);
        }
        leaderboard.save();
    }


    public void fixLeaderboard(Logger logger, Calendar calendar) {
        String currentMonth = leaderboard.getCurrentMonth(calendar);

        for (String monthName : leaderboard.getSectionKey(false)) {
            if (!currentMonth.equals(monthName)) {
                logger.warning("Detected mismatch month in leaderboard.yml file");
                logger.warning("This could be a monthly reset or just a mismatch");
                logger.warning("Fixing up leaderboard.yml");

                for (int i = 1; i <= 7; i++) {
                    String section = "TOP" + i;
                    leaderboard.setStringSection(monthName + "." + section + ".Player", "null");
                    leaderboard.setIntSection(monthName + "." + section + ".Points", 0);
                }

                newLeaderboard(logger, calendar);
            }
        }
    }


    public void resetLeaderboard(Calendar now) {
        leaderboard.load();

        String currentMonth = leaderboard.getCurrentMonth(now);

        for (int i = 1; i <= 7; i++) {
            leaderboard.setTopPlayer(currentMonth, i, "null", 0);
        }

        leaderboard.save();
    }
}

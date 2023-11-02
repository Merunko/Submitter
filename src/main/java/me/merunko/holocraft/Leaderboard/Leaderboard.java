package me.merunko.holocraft.Leaderboard;

import me.merunko.holocraft.Configuration.Configuration;
import me.merunko.holocraft.Submitter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.logging.Logger;

public class Leaderboard {

    private final Logger logger;
    private final Configuration config;
    private final LeaderboardConfiguration leaderboard;
    private final Backup backup = new Backup();
    private final int resetDay;
    private final int resetHour;
    private final int resetMinute;

    public Leaderboard(Logger logger, Configuration config, LeaderboardConfiguration leaderboard, int resetDay, int resetHour, int resetMinute) {
        this.logger = logger;
        this.config = config;
        this.leaderboard = leaderboard;
        this.resetDay = resetDay;
        this.resetHour = resetHour;
        this.resetMinute = resetMinute;
    }

    public void resetCountdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Calendar now = Calendar.getInstance();
                int currentDay = now.get(Calendar.DAY_OF_MONTH);
                int currentHour = now.get(Calendar.HOUR_OF_DAY);
                int currentMinute = now.get(Calendar.MINUTE);

                int resetDay = config.getLeaderboardResetDay();
                String resetTime = config.getLeaderboardResetTime();

                if (currentDay == resetDay && currentHour == Integer.parseInt(resetTime.substring(0, 2))
                        && currentMinute == Integer.parseInt(resetTime.substring(2))) {
                    backup.createLeaderboardBackup(logger, leaderboard);
                    leaderboard.fixLeaderboard(logger, now);
                    resetLeaderboard(now);
                }
            }
        }.runTaskTimer(Submitter.getPlugin(), 0L, 20L * 60L);
    }

    public void resetLeaderboard(Calendar now) {
        leaderboard.load();

        String currentMonth = leaderboard.getCurrentMonth(now);

        for (int i = 1; i <= 7; i++) {
            leaderboard.setTopPlayer(currentMonth, i, "null", 0, "null");
        }

        leaderboard.save();
    }

}

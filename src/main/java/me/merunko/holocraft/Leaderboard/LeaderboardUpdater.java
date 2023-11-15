package me.merunko.holocraft.Leaderboard;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeaderboardUpdater {

    private final LeaderboardConfiguration leaderboard;
    private final Logger logger;

    public LeaderboardUpdater(LeaderboardConfiguration leaderboard, Logger logger) {
        this.leaderboard = leaderboard;
        this.logger = logger;
    }

    public void updateLeaderboard() {

        Pattern logEntryPattern = Pattern.compile("\\[(\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2})] (.+) \\(Leader: (.+)\\) submitted (\\d+) (.+), and gained (\\d+) point value.");

        Map<String, Integer> playerPoints = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("plugins/Submitter/logs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = logEntryPattern.matcher(line);
                if (matcher.find()) {
                    String playerName = matcher.group(2);
                    String leaderName = matcher.group(3);
                    int pointsEarned = Integer.parseInt(matcher.group(6));

                    playerPoints.put(leaderName, playerPoints.getOrDefault(leaderName, 0) + pointsEarned);
                }
            }
        } catch (IOException e) {
            logger.severe("An error occurred while trying to read leaderboard.yml file.");
        }

        TreeMap<Integer, String> sortedPlayerPoints = new TreeMap<>((a, b) -> b - a);
        playerPoints.forEach((player, points) -> sortedPlayerPoints.put(points, player));

        int position = 1;
        for (Map.Entry<Integer, String> entry : sortedPlayerPoints.entrySet()) {
            if (position > 7) {
                break;
            }

            String playerName = entry.getValue();
            int points = entry.getKey();

            Calendar now = Calendar.getInstance();
            String currentMonth = leaderboard.getCurrentMonth(now);
            leaderboard.setTopPlayer(currentMonth, position, playerName, points);

            position++;
        }

        while (position <= 7) {
            Calendar now = Calendar.getInstance();
            String currentMonth = leaderboard.getCurrentMonth(now);
            leaderboard.setTopPlayer(currentMonth, position, "null", 0);

            position++;
        }

        leaderboard.save();
    }
}

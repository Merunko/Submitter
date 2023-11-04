package me.merunko.holocraft.Leaderboard;

import java.util.Calendar;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class LeaderboardUpdater {

    public void updateLeaderboard(LeaderboardConfiguration leaderboard, Logger logger) {

        Pattern logEntryPattern = Pattern.compile("\\[(\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2})] (.+) submitted (\\d+) (.+), and gained (\\d+) point value.");

        Map<String, Integer> playerPoints = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("plugins/Submitter/logs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = logEntryPattern.matcher(line);
                if (matcher.find()) {
                    String playerName = matcher.group(2);
                    int pointsEarned = Integer.parseInt(matcher.group(5));

                    playerPoints.put(playerName, playerPoints.getOrDefault(playerName, 0) + pointsEarned);
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

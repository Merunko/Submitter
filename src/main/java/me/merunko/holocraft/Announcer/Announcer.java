package me.merunko.holocraft.Announcer;

import me.merunko.holocraft.Configuration.MainConfiguration;
import me.merunko.holocraft.Hook.SuperiorSkyblock.SuperiorSkyBlockHook;
import me.merunko.holocraft.Leaderboard.LeaderboardConfiguration;

import net.Zrips.CMILib.Colors.CMIChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Announcer {

    private final SuperiorSkyBlockHook ssb;
    private final MainConfiguration config;
    private final LeaderboardConfiguration leaderboard;
    private final Calendar calendar;

    public Announcer(SuperiorSkyBlockHook ssb, MainConfiguration config, LeaderboardConfiguration leaderboard, Calendar calendar) {
        this.ssb = ssb;
        this.config = config;
        this.leaderboard = leaderboard;
        this.calendar = calendar;
    }

    public void announceTopPlayers() {
        String currentMonth = leaderboard.getCurrentMonth(calendar);
        List<String> announcerMessage = config.getAnnouncerMessage();

        int numTopPlayers = config.getTotalPlayerInLeaderboard();

        for (String line : announcerMessage) {
            String message = replacePlaceholders(line, currentMonth, numTopPlayers);
            Bukkit.broadcast(message, "default");
        }
    }

    public void announcePersonalOnlinePlayer(Player player, int rank, int points) {
        List<String> announcerMessage = config.getPersonalAnnouncerMessage();

        for (String line : announcerMessage) {
            String message = CMIChatColor.translate(line.replace("%rank%", Integer.toString(rank)).replace("%point%", Integer.toString(points)));
            player.sendMessage(message);
        }
    }

    public void announcePersonalOfflinePlayer(Player player, int rank, int points) {
        List<String> announcerMessage = config.getPersonalAnnouncerMessage();

        for (String line : announcerMessage) {
            String message = CMIChatColor.translate(line.replace("%rank%", Integer.toString(rank)).replace("%point%", Integer.toString(points)));
            player.sendMessage(message);
        }
    }

    public void announceTopPlayersDebug(Player player) {
        String currentMonth = leaderboard.getCurrentMonth(calendar);
        List<String> announcerMessage = config.getAnnouncerMessage();

        int numTopPlayers = config.getTotalPlayerInLeaderboard();

        player.sendMessage(CMIChatColor.translate("&c&lONLY YOU CAN SEE THIS ANNOUNCER"));
        for (String line : announcerMessage) {
            String message = replacePlaceholders(line, currentMonth, numTopPlayers);
            player.sendMessage(message);
        }
    }

    public void announcePersonalDebug(Player player) {
        String currentMonth = leaderboard.getCurrentMonth(calendar);
        List<String> announcerMessage = config.getPersonalAnnouncerMessage();

        for (String line : announcerMessage) {
            String message = replacePlaceholders(line, currentMonth, 1);
            player.sendMessage(message);
        }
    }

    private String replacePlaceholders(String line, String currentMonth, int numTopPlayers) {
        for (int i = 1; i <= numTopPlayers; i++) {
            String playerName = leaderboard.getTopPlayerName(currentMonth, i);
            String islandName = "No Winner";
            int points = 0;

            if (playerName != null && !playerName.equals("null")) {
                islandName = ssb.getIslandName(playerName);
                points = leaderboard.getTopPlayerPoint(currentMonth, i);
            }

            line = line.replace("%island" + i + "%", islandName)
                    .replace("%point" + i + "%", String.valueOf(points)
                            .replace("%top" + i + "%", Objects.requireNonNull(playerName)
                                    .replace("%rank%", String.valueOf(i)
                                            .replace("%point%", String.valueOf(points)))));
        }

        return CMIChatColor.translate(line);
    }
}

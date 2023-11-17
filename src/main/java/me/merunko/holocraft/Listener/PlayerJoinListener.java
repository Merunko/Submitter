package me.merunko.holocraft.Listener;

import me.merunko.holocraft.Announcer.Announcer;
import me.merunko.holocraft.Rewards.RewardsConfiguration;
import me.merunko.holocraft.Rewards.UnclaimedConfiguration;
import me.merunko.holocraft.Submitter;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoinListener implements Listener {

    private final Announcer announcer;
    private final UnclaimedConfiguration unclaimed;
    private final RewardsConfiguration reward;

    public PlayerJoinListener(Announcer announcer, UnclaimedConfiguration unclaimed, RewardsConfiguration reward) {
        this.announcer = announcer;
        this.unclaimed = unclaimed;
        this.reward = reward;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        final String playerName = player.getName();

        Bukkit.getScheduler().runTaskLater(Submitter.getPlugin(), () -> {
            if (player.isOnline()) {
                List<String> playerList = unclaimed.getStringList("PLAYER");
                List<String> entriesToRemove = new ArrayList<>();

                for (String entry : playerList) {
                    if (entry.contains("Player: " + playerName)) {
                        String[] parts = entry.split(", ");
                        if (parts.length == 3) {
                            String positionStr = parts[0].replace("Position: ", "");
                            String pointsStr = parts[2].replace("Points: ", "");
                            int position = Integer.parseInt(positionStr);
                            int points = Integer.parseInt(pointsStr);
                            announcer.announcePersonalOfflinePlayer(player, position, points);
                            List<ItemStack> rewardItems = reward.getRewardItems(position);

                            if (rewardItems != null) {
                                for (ItemStack item : rewardItems) {
                                    String itemName = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ? item.getItemMeta().getDisplayName() : reward.getFormattedDisplayName(item);
                                    player.sendMessage(ChatColor.GOLD + "[Rewards] " + ChatColor.GREEN + "You received: " + ChatColor.GOLD + itemName + ChatColor.GREEN + ".");
                                    player.getInventory().addItem(item);
                                }
                            }

                            List<String> rewardCommands = reward.getRewardCommands(position);
                            if (!rewardCommands.isEmpty()) {
                                for (String command : rewardCommands) {
                                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", playerName));
                                }
                            }

                            entriesToRemove.add(entry);
                        }
                    }
                }
                playerList.removeAll(entriesToRemove);
                unclaimed.setPlayer(playerList);
                unclaimed.save();
            }
        }, 20 * 30);
    }
}

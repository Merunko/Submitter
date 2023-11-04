package me.merunko.holocraft.Listener;

import me.merunko.holocraft.Rewards.RewardsConfiguration;
import me.merunko.holocraft.Rewards.UnclaimedConfiguration;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.ChatColor;

import java.util.List;

public class PlayerJoinListener implements Listener {

    private final UnclaimedConfiguration unclaimed;
    private final RewardsConfiguration reward;

    public PlayerJoinListener(UnclaimedConfiguration unclaimed, RewardsConfiguration reward) {
        this.unclaimed = unclaimed;
        this.reward = reward;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        String playerName = e.getPlayer().getName();
        List<String> playerList = unclaimed.getStringList("PLAYER");

        if (playerList.contains(playerName)) {
            Player player = e.getPlayer();

            for (String entry : playerList) {
                if (entry.contains("Player: " + playerName)) {
                    String[] parts = entry.split(", ");
                    if (parts.length == 2) {

                        String positionStr = parts[0].replace("Position: ", "");
                        int position = Integer.parseInt(positionStr);
                        player.sendMessage(ChatColor.GOLD + "[Monthly Island Points] " + ChatColor.GREEN + "Your ranking:" + ChatColor.GOLD + position);
                        List<ItemStack> rewardItems = reward.getRewardItems(position);

                        if (rewardItems != null) {
                            for (ItemStack item : rewardItems) {
                                player.sendMessage(ChatColor.GOLD + "[Monthly Island Points] " + ChatColor.GREEN + "You received: " + ChatColor.GOLD + reward.getFormattedDisplayName(item) + ChatColor.GREEN + ".");
                                player.getInventory().addItem(item);
                            }
                        }

                        List<String> rewardCommands = reward.getRewardCommands(position);
                        if (!rewardCommands.isEmpty()) {
                            for (String command : rewardCommands) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", playerName));
                            }
                        }

                        playerList.remove(entry);
                        unclaimed.setPlayer(playerList);
                        unclaimed.save();
                    }
                }
            }
        }
    }
}

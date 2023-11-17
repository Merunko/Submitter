package me.merunko.holocraft.Rewards;

import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.merunko.holocraft.Announcer.Announcer;
import me.merunko.holocraft.Hook.SuperiorSkyblock.SuperiorSkyBlockHook;
import me.merunko.holocraft.Leaderboard.LeaderboardConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class RewardsConfiguration {

    private final Announcer announcer;
    private final FileConfiguration rewards;
    private final LeaderboardConfiguration leaderboard;
    private final UnclaimedConfiguration unclaimed;
    private final SuperiorSkyBlockHook ssb;
    private final Calendar calendar;
    private final Logger logger;

    public RewardsConfiguration(Announcer announcer, UnclaimedConfiguration unclaimed, FileConfiguration rewards, LeaderboardConfiguration leaderboard, SuperiorSkyBlockHook ssb, Logger logger, Calendar calendar) {
        this.announcer = announcer;
        this.unclaimed = unclaimed;
        this.rewards = rewards;
        this.leaderboard = leaderboard;
        this.ssb = ssb;
        this.logger = logger;
        this.calendar = calendar;
    }

    public void load() {
        try {
            rewards.load(new File("plugins/Submitter/rewards.yml"));
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            logger.severe("An error occurred while loading rewards.yml file.");
        }
    }

    public void save() {
        try {
            rewards.save(new File("plugins/Submitter/rewards.yml"));
        } catch (IOException e) {
            logger.severe("An error occurred while saving rewards.yml file.");
        }
    }

    public void setRewardItems(int position, List<String> serializedItems) {
        ConfigurationSection rewardSection = rewards.getConfigurationSection("TOP" + position);
        if (rewardSection != null) {
            rewardSection.set("items", serializedItems);
            save();
        }
    }

    public List<ItemStack> getRewardItems(int position) {
        ConfigurationSection rewardSection = rewards.getConfigurationSection("TOP" + position);
        if (rewardSection != null) {
            List<String> serializedItems = rewardSection.getStringList("items");
            List<ItemStack> items = new ArrayList<>();

            for (String serializedItem : serializedItems) {
                String[] parts = serializedItem.split(" - ");
                if (parts.length == 2) {
                    ItemStack itemStack = deserializeItemStack(parts[1]);
                    if (itemStack != null) {
                        items.add(itemStack);
                    }
                }
            }

            return items;
        }
        return null;
    }

    public List<String> getRewardCommands(int position) {
        ConfigurationSection rewardSection = rewards.getConfigurationSection("TOP" + position);
        if (rewardSection != null) {
            return rewardSection.getStringList("commands");
        }
        return Collections.emptyList();
    }

    public ItemStack deserializeItemStack(String serializedItem) {
        try {
            byte[] itemBytes = Base64.getDecoder().decode(serializedItem);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(itemBytes);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack itemStack = (ItemStack) dataInput.readObject();

            dataInput.close();
            return itemStack;
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("Failed to retrieve rewards. This often happens due to data corruption.");
            return null;
        }
    }

    public String getFormattedMaterialName(Material material) {
        String materialName = material.toString().toLowerCase().replace("_", " ");
        return materialName.substring(0, 1).toUpperCase() + materialName.substring(1);
    }

    public String getFormattedDisplayName(ItemStack itemStack) {
        String materialName = getFormattedMaterialName(itemStack.getType());
        int quantity = itemStack.getAmount();
        return materialName + " x" + quantity;
    }

    public void giveOutRewards() {
        for (int i = 1; i < 8; i++) {
            String playerName = leaderboard.getTopPlayerName(leaderboard.getCurrentMonth(calendar), i);
            int points = leaderboard.getTopPlayerPoint(leaderboard.getCurrentMonth(calendar),i);

            List<ItemStack> rewardItems = getRewardItems(i);
            List<String> rewardCommands = getRewardCommands(i);

            Player onlinePlayer = Bukkit.getPlayer(playerName);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);

            if (onlinePlayer != null && !playerName.equals("null")) {

                announcer.announcePersonalOnlinePlayer(onlinePlayer, i, points);
                distributeRewards(onlinePlayer, rewardItems, rewardCommands);

                List<SuperiorPlayer> islandMembers = ssb.getMembersList(playerName);

                for (SuperiorPlayer member : islandMembers) {
                    Player memberPlayer = member.asPlayer();

                    if (memberPlayer != null) {
                        announcer.announcePersonalOnlinePlayer(memberPlayer, i, points);
                        distributeRewards(memberPlayer, rewardItems, rewardCommands);
                    } else {
                        List<String> unclaimedPlayers = unclaimed.getStringList("PLAYER");
                        String unclaimedEntry = "Position: " + i + ", Player: " + member.getName() + ", Points: " + points;
                        unclaimedPlayers.add(unclaimedEntry);
                        unclaimed.setPlayer(unclaimedPlayers);
                    }
                }

            } else if (onlinePlayer == null && !playerName.equals("null")) {
                List<String> unclaimedPlayers = unclaimed.getStringList("PLAYER");
                String unclaimedEntry = "Position: " + i + ", Player: " + offlinePlayer.getName() + ", Points: " + points;
                unclaimedPlayers.add(unclaimedEntry);
                unclaimed.setPlayer(unclaimedPlayers);

                List<SuperiorPlayer> islandMembers = ssb.getMembersList(playerName);

                for (SuperiorPlayer member : islandMembers) {
                    Player memberPlayer = member.asPlayer();

                    if (memberPlayer != null) {
                        announcer.announcePersonalOnlinePlayer(memberPlayer, i, points);
                        distributeRewards(memberPlayer, rewardItems, rewardCommands);
                    } else {
                        unclaimedPlayers = unclaimed.getStringList("PLAYER");
                        unclaimedEntry = "Position: " + i + ", Player: " + member.getName() + ", Points: " + points;
                        unclaimedPlayers.add(unclaimedEntry);
                        unclaimed.setPlayer(unclaimedPlayers);
                    }
                }
            }
        }

        unclaimed.save();
    }

    private void distributeRewards(Player player, List<ItemStack> rewardItems, List<String> rewardCommands) {
        if (!rewardItems.isEmpty()) {
            for (ItemStack item : rewardItems) {
                String itemName = (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) ? item.getItemMeta().getDisplayName() : getFormattedDisplayName(item);
                player.sendMessage(ChatColor.GOLD + "[Monthly Island Points] " + ChatColor.GREEN + "You received: " + ChatColor.GOLD + itemName + ChatColor.GREEN + ".");
                player.getInventory().addItem(item);
            }
        }

        if (!rewardCommands.isEmpty()) {
            for (String command : rewardCommands) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
            }
        }
    }

}

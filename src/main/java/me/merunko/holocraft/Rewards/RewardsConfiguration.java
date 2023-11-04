package me.merunko.holocraft.Rewards;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class RewardsConfiguration {

    private final FileConfiguration rewards;
    private final Logger logger;

    public RewardsConfiguration(FileConfiguration rewards, Logger logger) {
        this.rewards = rewards;
        this.logger = logger;
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

}

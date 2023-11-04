package me.merunko.holocraft.Listener;

import me.merunko.holocraft.Holder.RewardsInventoryGuiHolder;
import me.merunko.holocraft.Rewards.RewardsConfiguration;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class RewardsInventoryInteractListener implements Listener {

    RewardsConfiguration reward;
    private final Logger logger;

    public RewardsInventoryInteractListener(RewardsConfiguration reward, Logger logger) {
        this.reward = reward;
        this.logger = logger;
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent e) {
        Inventory clickedInventory = e.getClickedInventory();
        InventoryCloseListener invCloseL = new InventoryCloseListener();
        int clickedSlot = e.getRawSlot();

        if (clickedInventory != null && clickedInventory.getHolder() instanceof RewardsInventoryGuiHolder) {

            if (!((clickedSlot >= 10 && clickedSlot <= 16) || (clickedSlot >= 19 && clickedSlot <= 25) || (clickedSlot >= 28 && clickedSlot <= 34) || (clickedSlot >= 37 && clickedSlot <= 43))) {
                e.setCancelled(true);
            }

            if (clickedSlot == 50) {
                e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();
                invCloseL.returnItems(e.getClickedInventory(), player.getInventory());
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            }

            if (clickedSlot == 48) {
                e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();

                List<String> serializedItems = new ArrayList<>();

                for (int slot = 10; slot <= 16; slot++) {
                    ItemStack itemStack = e.getInventory().getItem(slot);
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        String serializedItem = serializeItemStack(itemStack);
                        String itemDisplayName = itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : reward.getFormattedDisplayName(itemStack);
                        serializedItems.add(itemDisplayName + " - " + serializedItem);
                    }
                }

                for (int slot = 19; slot <= 25; slot++) {
                    ItemStack itemStack = e.getInventory().getItem(slot);
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        String serializedItem = serializeItemStack(itemStack);
                        String itemDisplayName = itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : reward.getFormattedDisplayName(itemStack);
                        serializedItems.add(itemDisplayName + " - " + serializedItem);
                    }
                }

                for (int slot = 28; slot <= 34; slot++) {
                    ItemStack itemStack = e.getInventory().getItem(slot);
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        String serializedItem = serializeItemStack(itemStack);
                        String itemDisplayName = itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : reward.getFormattedDisplayName(itemStack);
                        serializedItems.add(itemDisplayName + " - " + serializedItem);
                    }
                }

                for (int slot = 37; slot <= 43; slot++) {
                    ItemStack itemStack = e.getInventory().getItem(slot);
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        String serializedItem = serializeItemStack(itemStack);
                        String itemDisplayName = itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().getDisplayName() : reward.getFormattedDisplayName(itemStack);
                        serializedItems.add(itemDisplayName + " - " + serializedItem);
                    }
                }

                int position = Integer.parseInt(Objects.requireNonNull(e.getInventory().getItem(49)).getItemMeta().getDisplayName());
                reward.setRewardItems(position, serializedItems);
                player.closeInventory();
                player.sendMessage(ChatColor.GOLD + "[Submitter] " + ChatColor.GREEN + "Successfully set rewards for top " + ChatColor.GOLD + position + ChatColor.GREEN + ".");
            }
        }
    }

    private String serializeItemStack(ItemStack itemStack) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(itemStack);
            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            logger.severe("An error occurred while saving rewards.");
            return null;
        }
    }

}

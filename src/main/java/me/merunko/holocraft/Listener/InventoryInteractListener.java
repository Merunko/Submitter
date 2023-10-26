package me.merunko.holocraft.Listener;

import me.merunko.holocraft.Holder.InventoryGuiHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

public class InventoryInteractListener implements Listener {

    private final FileConfiguration config;
    private final Logger logger;
    private final File logFile;

    public InventoryInteractListener(FileConfiguration config, File logFile, Logger logger) {
        this.config = config;
        this.logger = logger;
        this.logFile = logFile;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory clickedInventory = e.getClickedInventory();
        int clickedSlot = e.getRawSlot();

        if (clickedInventory != null && clickedInventory.getHolder() instanceof InventoryGuiHolder) {

            if (!((clickedSlot >= 10 && clickedSlot <= 16) || (clickedSlot >= 19 && clickedSlot <= 25) || (clickedSlot >= 28 && clickedSlot <= 34) || (clickedSlot >= 37 && clickedSlot <= 43))) {
                e.setCancelled(true);
            }

            if (clickedSlot == 50) {
                e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();
                player.closeInventory();
            }

            if (clickedSlot == 48) {
                e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();

                int totalPoints = 0;
                Inventory inventory = e.getClickedInventory();
                StringBuilder submittedItems = new StringBuilder();

                for (int slot = 10; slot <= 16; slot++) {
                    totalPoints += calculatePoints(inventory.getItem(slot), player.getInventory(), submittedItems);
                }
                for (int slot = 19; slot <= 25; slot++) {
                    totalPoints += calculatePoints(inventory.getItem(slot), player.getInventory(), submittedItems);
                }
                for (int slot = 28; slot <= 34; slot++) {
                    totalPoints += calculatePoints(inventory.getItem(slot), player.getInventory(), submittedItems);
                }
                for (int slot = 37; slot <= 43; slot++) {
                    totalPoints += calculatePoints(inventory.getItem(slot), player.getInventory(), submittedItems);
                }

                String playerName = player.getName();
                logSubmission(playerName, submittedItems.toString(), totalPoints);

                if (totalPoints > 0) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "is admin addbonus " + playerName + " worth " + totalPoints);
                    player.sendMessage(ChatColor.GREEN + "Your island gained " + ChatColor.GOLD + totalPoints + ChatColor.GREEN + " worth value.");
                } else {
                    player.sendMessage(ChatColor.RED + "All the items submitted have no value.");
                }

                player.closeInventory();
            }
        }
    }

    private int calculatePoints(ItemStack item, Inventory playerInventory, StringBuilder submittedItems) {
        if (item != null) {
            Material itemType = item.getType();
            if (config.isConfigurationSection("items") && config.contains("items." + itemType)) {
                int points = config.getInt("items." + itemType);
                playerInventory.removeItem(item);
                submittedItems.append(item.getAmount()).append(" ").append(itemType).append(", ");
                return points * item.getAmount();
            } else {
                playerInventory.addItem(item);
            }
        }
        return 0;
    }

    private void logSubmission(String playerName, String items, int totalPoints) {
        if (totalPoints > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
            String timestamp = dateFormat.format(new Date());
            String logMessage = "[" + timestamp + "] " + playerName + " submitted " + items + " and gained " + totalPoints + " worth value.";

            logger.info(logMessage);

            try {
                FileWriter writer = new FileWriter(logFile, true);
                writer.write(logMessage + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

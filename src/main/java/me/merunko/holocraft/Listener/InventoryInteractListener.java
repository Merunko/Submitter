package me.merunko.holocraft.Listener;

import me.merunko.holocraft.Holder.InventoryGuiHolder;
import net.Indyuce.mmoitems.MMOItems;
import net.Zrips.CMILib.Colors.CMIChatColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
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

            updateDynamicWorthValue(clickedInventory);

            if (!((clickedSlot >= 10 && clickedSlot <= 16) || (clickedSlot >= 19 && clickedSlot <= 25) || (clickedSlot >= 28 && clickedSlot <= 34) || (clickedSlot >= 37 && clickedSlot <= 43))) {
                e.setCancelled(true);
            }

            if (clickedSlot == 50) {
                e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();
                returnItems(e.getClickedInventory(), player.getInventory());
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            }

            if (clickedSlot == 48) {
                e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();

                int totalPoints = 0;
                Inventory inventory = e.getClickedInventory();
                StringBuilder submittedItems = new StringBuilder();

                for (int slot = 10; slot <= 16; slot++) {
                    if (inventory != null) {
                        totalPoints += calculatePoint(inventory.getItem(slot), player.getInventory(), submittedItems);
                    }
                }
                for (int slot = 19; slot <= 25; slot++) {
                    if (inventory != null) {
                        totalPoints += calculatePoint(inventory.getItem(slot), player.getInventory(), submittedItems);
                    }
                }
                for (int slot = 28; slot <= 34; slot++) {
                    if (inventory != null) {
                        totalPoints += calculatePoint(inventory.getItem(slot), player.getInventory(), submittedItems);
                    }
                }
                for (int slot = 37; slot <= 43; slot++) {
                    if (inventory != null) {
                        totalPoints += calculatePoint(inventory.getItem(slot), player.getInventory(), submittedItems);
                    }
                }

                String playerName = player.getName();
                logSubmission(playerName, submittedItems.toString(), totalPoints);

                if (totalPoints > 0) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "is admin addbonus " + player.getName() + " worth " + totalPoints);
                    player.sendMessage(ChatColor.GREEN + "Your island gained " + ChatColor.GOLD + totalPoints + ChatColor.GREEN + " " + pointName() +  " value.");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1.0F, 1.0F);
                } else {
                    player.sendMessage(ChatColor.RED + "All the items submitted have no value.");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                }

                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Inventory closedInventory = e.getInventory();
        Player player = (Player) e.getPlayer();
        if (!closedInventory.isEmpty() && closedInventory.getHolder() instanceof InventoryGuiHolder && e.getReason() != InventoryCloseEvent.Reason.PLUGIN ) {
            returnItems(closedInventory, player.getInventory());
        }
    }

    public void returnItems(Inventory inv, Inventory playerInv) {
        for (int slot = 10; slot <= 16; slot++) {
            ItemStack item = inv.getItem(slot);
            if(item != null) {
                playerInv.addItem(item);
            }
        }
        for (int slot = 19; slot <= 25; slot++) {
            ItemStack item = inv.getItem(slot);
            if(item != null) {
                playerInv.addItem(item);
            }
        }
        for (int slot = 28; slot <= 34; slot++) {
            ItemStack item = inv.getItem(slot);
            if(item != null) {
                playerInv.addItem(item);
            }
        }
        for (int slot = 37; slot <= 43; slot++) {
            ItemStack item = inv.getItem(slot);
            if(item != null) {
                playerInv.addItem(item);
            }
        }
    }

    private boolean typeMatches(String mmoItemType, String typeName) {
        return mmoItemType.equals(typeName);
    }

    private boolean itemIdMatches(String mmoItemId, String itemId) {
        return mmoItemId.equals(itemId);
    }

    private int calculatePoint(ItemStack item, Inventory playerInventory, StringBuilder submittedItems) {
        String mmoItemType = MMOItems.getTypeName(item);
        String mmoItemId = MMOItems.getID(item);

        if (item != null) {
            Material itemType = item.getType();
            if (mmoItemType != null && mmoItemId != null) {
                if (config.isConfigurationSection("mmoitems")) {
                    boolean foundMatch = false;

                    for (String mmoType : Objects.requireNonNull(config.getConfigurationSection("mmoitems")).getKeys(false)) {
                        if (config.isList("mmoitems." + mmoType)) {
                            List<String> itemValuePairs = config.getStringList("mmoitems." + mmoType);
                            for (String itemValuePair : itemValuePairs) {
                                String[] parts = itemValuePair.split(":");
                                if (parts.length == 2) {
                                    String itemId = parts[0].trim();
                                    int value = Integer.parseInt(parts[1].trim());

                                    if (typeMatches(mmoItemType, mmoType) && itemIdMatches(mmoItemId, itemId)) {
                                        submittedItems.append(item.getAmount()).append(" ").append(itemId).append(", ");
                                        return value * item.getAmount();
                                    }
                                }
                            }
                        }
                    } if (!foundMatch) {
                        playerInventory.addItem(item);
                    }
                }

            } else {
                if (config.isConfigurationSection("minecraft_items") && config.contains("minecraft_items." + itemType)) {
                    int points = config.getInt("minecraft_items." + itemType);
                    submittedItems.append(item.getAmount()).append(" ").append(itemType).append(", ");
                    return points * item.getAmount();
                } else {
                    playerInventory.addItem(item);
                }
            }
        }
        return 0;
    }

    private int dynamicWorthValue(ItemStack item) {
        if (item == null) {
            return 0;
        }

        String mmoItemType = MMOItems.getTypeName(item);
        String mmoItemId = MMOItems.getID(item);

        int value = 0;

        Material itemType = item.getType();
        if (mmoItemType != null && mmoItemId != null) {
            if (config.isConfigurationSection("mmoitems")) {

                for (String mmoType : Objects.requireNonNull(config.getConfigurationSection("mmoitems")).getKeys(false)) {
                    if (config.isList("mmoitems." + mmoType)) {
                        List<String> itemValuePairs = config.getStringList("mmoitems." + mmoType);
                        for (String itemValuePair : itemValuePairs) {
                            String[] parts = itemValuePair.split(":");
                            if (parts.length == 2) {
                                String itemId = parts[0].trim();
                                int valueMMO = Integer.parseInt(parts[1].trim());

                                if (typeMatches(mmoItemType, mmoType) && itemIdMatches(mmoItemId, itemId)) {
                                    value += valueMMO * item.getAmount();
                                }
                            }
                        }
                    }
                }
            }

        } else {
            if (config.isConfigurationSection("minecraft_items") && config.contains("minecraft_items." + itemType)) {
                int valueVanilla = config.getInt("minecraft_items." + itemType);
                value += valueVanilla * item.getAmount();
            }
        }
        return value;
    }

    private void updateDynamicWorthValue(Inventory inventory) {
        int totalWorth = 0;
        for (int slot = 10; slot <= 16; slot++) {
            ItemStack item = inventory.getItem(slot);
            totalWorth += dynamicWorthValue(item);
        }
        for (int slot = 19; slot <= 25; slot++) {
            ItemStack item = inventory.getItem(slot);
            totalWorth += dynamicWorthValue(item);
        }
        for (int slot = 28; slot <= 34; slot++) {
            ItemStack item = inventory.getItem(slot);
            totalWorth += dynamicWorthValue(item);
        }
        for (int slot = 37; slot <= 43; slot++) {
            ItemStack item = inventory.getItem(slot);
            totalWorth += dynamicWorthValue(item);
        }

        ItemStack totalWorthSign = new ItemStack(Material.ACACIA_HANGING_SIGN);
        ItemMeta totalWorthMeta = totalWorthSign.getItemMeta();
        if (totalWorthMeta != null) {
            String name3 = CMIChatColor.translate("{#FB5848}" + pointName() + ": " + "{#FB8F00}" + totalWorth);
            totalWorthMeta.setDisplayName(name3);

            List<String> lore = new ArrayList<>();
            lore.add(CMIChatColor.translate("{#B9FBE1}Click this to update " + pointName() + " value."));
            totalWorthMeta.setLore(lore);

            totalWorthSign.setItemMeta(totalWorthMeta);
        }
        inventory.setItem(49, totalWorthSign);
    }

    public String pointName() {
        if (config.isString("point_name")) {
            return config.getString("point_name");
        } else {
            return "NoName";
        }
    }

    private void logSubmission(String playerName, String items, int totalPoints) {
        if (totalPoints > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
            String timestamp = dateFormat.format(new Date());
            String logMessage = "[" + timestamp + "] " + playerName + " submitted " + items + " and gained " + totalPoints + " " + pointName() + " value.";

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

package me.merunko.holocraft.Listener;

import me.merunko.holocraft.Configuration.Configuration;
import me.merunko.holocraft.Holder.InventoryGuiHolder;
import net.Indyuce.mmoitems.MMOItems;
import net.Zrips.CMILib.Colors.CMIChatColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

public class InventoryInteractListener implements Listener {

    Configuration config;
    private final Logger logger;
    private final File logFile;

    public InventoryInteractListener(Configuration config, File logFile, Logger logger) {
        this.config = config;
        this.logger = logger;
        this.logFile = logFile;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory clickedInventory = e.getClickedInventory();
        InventoryCloseListener invCloseL = new InventoryCloseListener();
        int clickedSlot = e.getRawSlot();

        if (clickedInventory != null && clickedInventory.getHolder() instanceof InventoryGuiHolder) {

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

            if (clickedSlot == 49) {
                e.setCancelled(true);
                updateDynamicWorthValue(clickedInventory);

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
                    List<String> commands = config.getCommands(player, totalPoints);
                    for (String command : commands) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                    }

                    if (config.getDefaultMsg()) {
                        if (config.getCMILib()) {
                            player.sendMessage(config.getDefaultMsgHookCMILib(player, totalPoints));
                        } else {
                            player.sendMessage(config.getDefaultMsgNoCMILib(player, totalPoints));
                        }
                    }

                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1.0F, 1.0F);

                } else {
                    player.sendMessage(ChatColor.RED + "All the items submitted have no value.");
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                }

                player.closeInventory();
            }
        }
    }

    private int calculatePoint(ItemStack item, Inventory playerInventory, StringBuilder submittedItems) {
        String mmoItemType = MMOItems.getTypeName(item);
        String mmoItemId = MMOItems.getID(item);

        if (item != null) {
            Material itemType = item.getType();
            if (mmoItemType != null && mmoItemId != null) {
                int value = config.getMMOItemValue(mmoItemType, mmoItemId);
                if (value > 0) {
                    submittedItems.append(item.getAmount()).append(" ").append(mmoItemId).append(", ");
                    return value * item.getAmount();
                } else {
                    playerInventory.addItem(item);
                }
            } else {
                int points = config.getMinecraftItemValue(itemType.toString());
                if (points > 0) {
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

        if (mmoItemType != null && mmoItemId != null) {
            value = config.getMMOItemValue(mmoItemType, mmoItemId) * item.getAmount();
        } else {
            Material itemType = item.getType();
            int vanillaValue = config.getMinecraftItemValue(itemType.toString()) * item.getAmount();
            value += vanillaValue;
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
            String name3 = CMIChatColor.translate("{#FB5848}" + config.getPointName() + ": " + "{#FB8F00}" + totalWorth);
            totalWorthMeta.setDisplayName(name3);

            List<String> lore = new ArrayList<>();
            lore.add(CMIChatColor.translate("{#B9FBE1}Click this to update " + config.getPointName().toLowerCase() + " value."));
            totalWorthMeta.setLore(lore);

            totalWorthSign.setItemMeta(totalWorthMeta);
        }
        inventory.setItem(49, totalWorthSign);
    }

    private void logSubmission(String playerName, String items, int totalPoints) {
        if (totalPoints > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
            String timestamp = dateFormat.format(new Date());
            String logMessage = "[" + timestamp + "] " + playerName + " submitted " + items + " and gained " + totalPoints + " " + config.getPointName().toLowerCase() + " value.";

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

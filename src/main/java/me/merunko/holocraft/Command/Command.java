package me.merunko.holocraft.Command;

import me.merunko.holocraft.Configuration.Configuration;
import me.merunko.holocraft.GUI.Gui;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Command implements CommandExecutor {

    Configuration config;

    public Command(Configuration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (commandSender.hasPermission("submitter.reload")) {
                config.load();
                commandSender.sendMessage(ChatColor.GREEN + "Submitter reloaded.");
            } else {
                commandSender.sendMessage(ChatColor.RED + "You don't have permission to run this command!");
            }
            return true;

        } else if (args.length == 2 && args[0].equalsIgnoreCase("open") && commandSender instanceof Player) {
            Player player = (Player) commandSender;
            String targetPlayerName = args[1];
            Player targetPlayer = player.getServer().getPlayer(targetPlayerName);

            if (targetPlayer != null) {
                Gui gui = new Gui(config);
                targetPlayer.openInventory(gui.createDragDropInventory());
            } else {
                player.sendMessage(ChatColor.RED + "Player not found: " + targetPlayerName);
            }
            return true;

        } else if (args.length == 1 && args[0].equalsIgnoreCase("mmoinspect") && commandSender instanceof Player) {
            if (commandSender.hasPermission("submitter.reload")) {
                Player player = (Player) commandSender;
                ItemStack mainHandItem = player.getInventory().getItemInMainHand();

                if (mainHandItem == null) {
                    player.sendMessage(ChatColor.RED + "Your main hand is empty.");

                } else {
                    if (Bukkit.getPluginManager().isPluginEnabled("MMOItems")) {
                        String mmoItemId = MMOItems.getID((mainHandItem));
                        String mmoTypeName = MMOItems.getTypeName(mainHandItem);
                        player.sendMessage(ChatColor.GREEN + "MMOItems ID: " + ChatColor.GOLD + mmoItemId);
                        player.sendMessage(ChatColor.GREEN + "MMOItems Type: " + ChatColor.GOLD + mmoTypeName);
                    } else {
                        player.sendMessage(ChatColor.RED + "MMOItems plugin is not installed.");
                    }
                }
                return true;
            }

        } else if (args.length == 1 && args[0].equalsIgnoreCase("valueinspect") && commandSender instanceof Player) {
            if (commandSender.hasPermission("submitter.valueinspect")) {
                Player player = (Player) commandSender;
                ItemStack mainHandItem = player.getInventory().getItemInMainHand();
                Material itemType = mainHandItem.getType();
                String mmoItemType = MMOItems.getTypeName(mainHandItem);
                String mmoItemId = MMOItems.getID(mainHandItem);

                if (mainHandItem == null) {
                    player.sendMessage(ChatColor.RED + "Your main hand is empty.");
                } else {
                    if (mmoItemType != null && mmoItemId != null) {
                        int value = config.getMMOItemValue(mmoItemType, mmoItemId);
                        if (value > 0) {
                            value = value * mainHandItem.getAmount();
                            player.sendMessage(ChatColor.GREEN + config.getPointName() + ": " + ChatColor.GOLD + value);
                        } else {
                            player.sendMessage(ChatColor.GREEN + config.getPointName() + ": " + ChatColor.GOLD + "0");
                        }
                    } else {
                        int points = config.getMinecraftItemValue(itemType.toString());
                        if (points > 0) {
                            points = points * mainHandItem.getAmount();
                            player.sendMessage(ChatColor.GREEN + config.getPointName() + ": " + ChatColor.GOLD + points);
                        } else {
                            player.sendMessage(ChatColor.GREEN + config.getPointName() + ": " + ChatColor.GOLD + "0");
                        }
                    }
                    return true;
                }
            }
        }
            return false;
    }
}

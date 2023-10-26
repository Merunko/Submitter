package me.merunko.holocraft.Command;

import me.merunko.holocraft.GUI.Gui;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class OpenSubmitter implements CommandExecutor {

    private final FileConfiguration config;

    public OpenSubmitter(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (commandSender.hasPermission("submitter.reload")) {
                reloadConfig();
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
                Gui gui = new Gui();
                targetPlayer.openInventory(gui.createDragDropInventory());
            } else {
                player.sendMessage(ChatColor.RED + "Player not found: " + targetPlayerName);
            }
            return true;
        }

        return false;
    }

    private void reloadConfig() {
        try {
            config.load(new File("plugins/Submitter/config.yml"));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}

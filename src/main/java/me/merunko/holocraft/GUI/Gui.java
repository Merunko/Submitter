package me.merunko.holocraft.GUI;

import me.merunko.holocraft.Configuration.Configuration;
import me.merunko.holocraft.Holder.InventoryGuiHolder;
import net.Zrips.CMILib.Colors.CMIChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Gui {

    Configuration config;

    public Gui(Configuration config) {
        this.config = config;
    }

    String title;
    int slot = 54;

    public Inventory createDragDropInventory() {

        if (config.getCMILib()) {
            title = config.getTitleHookCMILib();
        } else {
            title = config.getTitleNoCMILib();
        }

        Inventory inventory = Bukkit.createInventory(new InventoryGuiHolder(), slot, title);

        ItemStack glassPane = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        ItemMeta glassPaneMeta = glassPane.getItemMeta();
        if (glassPaneMeta != null) {
            glassPaneMeta.setDisplayName(" ");
            glassPane.setItemMeta(glassPaneMeta);
        }

        ItemStack submitButton = new ItemStack(Material.LIME_WOOL);
        ItemMeta submitButtonMeta = submitButton.getItemMeta();
        if (submitButtonMeta != null) {
            String name1 = CMIChatColor.translate("{#00FB3F}Submit");
            submitButtonMeta.setDisplayName(name1);
            submitButton.setItemMeta(submitButtonMeta);
        }

        ItemStack cancelButton = new ItemStack(Material.RED_WOOL);
        ItemMeta cancelButtonMeta = cancelButton.getItemMeta();
        if (cancelButtonMeta != null) {
            String name2 = CMIChatColor.translate("{#FB5848}Cancel");
            cancelButtonMeta.setDisplayName(name2);
            cancelButton.setItemMeta(cancelButtonMeta);
        }

        ItemStack totalWorth = new ItemStack(Material.ACACIA_HANGING_SIGN);
        ItemMeta totalWorthMeta = totalWorth.getItemMeta();
        if (totalWorthMeta != null) {
            String name3 = CMIChatColor.translate("{#FB5848}" + config.getPointName() + ": " + "{#FB8F00}0");
            totalWorthMeta.setDisplayName(name3);

            List<String> lore = new ArrayList<>();
            lore.add(CMIChatColor.translate("{#B9FBE1}Click this to update " + config.getPointName().toLowerCase() + " value."));
            totalWorthMeta.setLore(lore);

            totalWorth.setItemMeta(totalWorthMeta);
        }

        for (int i = 0; i < slot ; i++) {
            if (!((i >= 10 && i <= 16) || (i >= 19 && i <= 25) || (i >= 28 && i <= 34) || (i >= 37 && i <= 43))) {
                inventory.setItem(i, glassPane);
            }
        }

        inventory.setItem(48, submitButton);
        inventory.setItem(50, cancelButton);
        inventory.setItem(49, totalWorth);

        return inventory;
    }

}

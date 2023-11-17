package me.merunko.holocraft.GUI;

import me.merunko.holocraft.Configuration.MainConfiguration;
import me.merunko.holocraft.Holder.RewardsInventoryGuiHolder;
import me.merunko.holocraft.Rewards.RewardsConfiguration;

import net.Zrips.CMILib.Colors.CMIChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RewardsGUI {

    RewardsConfiguration reward;
    MainConfiguration config;

    public RewardsGUI(RewardsConfiguration reward, MainConfiguration config) {
        this.reward = reward;
        this.config = config;
    }

    String concatenatedTitle;
    int slot = 54;

    public Inventory createRewardsSubmitter(String title, int position) {

        concatenatedTitle = CMIChatColor.translate("{#FB5848}&l".concat(title.toUpperCase().concat(" {#FB5848}&lRewards")));

        Inventory inventory = Bukkit.createInventory(new RewardsInventoryGuiHolder(), slot, concatenatedTitle);

        ItemStack glassPane = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        ItemMeta glassPaneMeta = glassPane.getItemMeta();
        if (glassPaneMeta != null) {
            glassPaneMeta.setDisplayName(" ");
            glassPane.setItemMeta(glassPaneMeta);
        }

        ItemStack confirmButton = new ItemStack(Material.LIME_WOOL);
        ItemMeta submitButtonMeta = confirmButton.getItemMeta();
        if (submitButtonMeta != null) {
            String name1 = CMIChatColor.translate("{#00FB3F}Confirm");
            submitButtonMeta.setDisplayName(name1);
            confirmButton.setItemMeta(submitButtonMeta);
        }

        ItemStack cancelButton = new ItemStack(Material.RED_WOOL);
        ItemMeta cancelButtonMeta = cancelButton.getItemMeta();
        if (cancelButtonMeta != null) {
            String name2 = CMIChatColor.translate("{#FB5848}Cancel");
            cancelButtonMeta.setDisplayName(name2);
            cancelButton.setItemMeta(cancelButtonMeta);
        }

        ItemStack positionNumber = new ItemStack(Material.ACACIA_HANGING_SIGN);
        ItemMeta positionNumberMeta = positionNumber.getItemMeta();
        if (positionNumberMeta != null) {
            String name3 = String.valueOf(position);
            positionNumberMeta.setDisplayName(name3);
            positionNumber.setItemMeta(positionNumberMeta);
        }

        for (int i = 0; i < slot ; i++) {
            if (!((i >= 10 && i <= 16) || (i >= 19 && i <= 25) || (i >= 28 && i <= 34) || (i >= 37 && i <= 43))) {
                inventory.setItem(i, glassPane);
            }
        }

        inventory.setItem(49, positionNumber);
        inventory.setItem(48, confirmButton);
        inventory.setItem(50, cancelButton);

        return inventory;
    }

}

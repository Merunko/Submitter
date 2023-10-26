package me.merunko.holocraft.GUI;

import me.merunko.holocraft.Holder.InventoryGuiHolder;
import net.Zrips.CMILib.Colors.CMIChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gui {

    String title = CMIChatColor.translate("{#FB5848}&lSubmit your items");
    int slot = 54;

    public Inventory createDragDropInventory() {
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

        for (int i = 0; i < slot ; i++) {
            if (!((i >= 10 && i <= 16) || (i >= 19 && i <= 25) || (i >= 28 && i <= 34) || (i >= 37 && i <= 43))) {
                inventory.setItem(i, glassPane);
            }
        }

        inventory.setItem(48, submitButton);
        inventory.setItem(50, cancelButton);

        return inventory;
    }
}

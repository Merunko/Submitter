package me.merunko.holocraft.Listener;

import me.merunko.holocraft.Holder.InventoryGuiHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryCloseListener implements Listener {

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

}

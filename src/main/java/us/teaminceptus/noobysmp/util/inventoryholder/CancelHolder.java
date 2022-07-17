package us.teaminceptus.noobysmp.util.inventoryholder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * This class is used to cancell ALL clicks inside the inventory
 */
public class CancelHolder implements InventoryHolder {

    public Inventory getInventory() {
        return null;
    }
    
}

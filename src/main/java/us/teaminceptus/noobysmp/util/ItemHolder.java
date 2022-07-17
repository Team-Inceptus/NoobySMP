package us.teaminceptus.noobysmp.util;

import org.bukkit.inventory.ItemStack;

public interface ItemHolder {
    
    ItemStack getItem();

    default String getLocalization() {
        return getItem().getItemMeta().getLocalizedName();
    }
}

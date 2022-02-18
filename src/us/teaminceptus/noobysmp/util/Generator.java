package us.teaminceptus.noobysmp.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class Generator {
	
	public static final Inventory genGUI(int size, String name) {
		return genGUI(size, name, null);
	}
	
	public static final Inventory genGUI(int size, String name, InventoryHolder holder) {
		if (size < 9 || size > 54) return null;
		if (size % 9 > 0) return null;
		
		Inventory inv = Bukkit.createInventory(holder, size, name);

		ItemStack guiBG = Items.Inventory.GUI_PANE;
		
		if (size < 36) return inv;

		for (int i = 0; i < 9; i++) {
			inv.setItem(i, guiBG);
		}

		for (int i = size - 9; i < size; i++) {
			inv.setItem(i, guiBG);
		}

		if (size >= 27) {
			inv.setItem(9, guiBG);
			inv.setItem(17, guiBG);
		}
		
		if (size >= 36) {
			inv.setItem(18, guiBG);
			inv.setItem(26, guiBG);
		}
		
		if (size >= 45) {
			inv.setItem(27, guiBG);
			inv.setItem(35, guiBG);
		}
		
		if (size == 54) {
			inv.setItem(36, guiBG);
			inv.setItem(44, guiBG);
		}
		
		return inv;
	}
	
}
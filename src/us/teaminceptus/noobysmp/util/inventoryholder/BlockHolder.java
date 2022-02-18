package us.teaminceptus.noobysmp.util.inventoryholder;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class BlockHolder implements InventoryHolder {

	private final Material type;
	
	public BlockHolder(Material type) {
		this.type = type;
	}

	public Material getMaterial() {
		return this.type;
	}
	
	@Override
	public Inventory getInventory() {
		return null;
	}
	
	

}

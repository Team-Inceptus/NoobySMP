package us.teaminceptus.noobysmp.util.inventoryholder;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class PlayerHolder implements InventoryHolder {

	private final OfflinePlayer player;
	
	public PlayerHolder(OfflinePlayer p) {
		this.player = p;
	}
	
	public OfflinePlayer getPlayer() {
		return this.player;
	}
	
	public Player getOnlinePlayer() {
		if (this.player.isOnline()) {
			return this.player.getPlayer();
		} else return null;
	}

	@Override
	public Inventory getInventory() {
		return null;
	}

}

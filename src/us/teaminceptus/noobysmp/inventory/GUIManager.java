package us.teaminceptus.noobysmp.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Items;

public class GUIManager implements Listener {

	protected SMP plugin;

	public GUIManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		plugin.getLogger().info("Successfully Loaded GUI Manager.");
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player p)) return;
		InventoryView view = e.getView();
		// Avoid Taking Items with Panes (p.1)
		if (view.getTopInventory().contains(Items.Inventory.GUI_PANE)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onMoveItem(InventoryMoveItemEvent e) {
		Inventory inv = e.getSource();

		if (inv.contains(Items.Inventory.GUI_PANE)) {
			e.setCancelled(true);
		}
	}
	
}
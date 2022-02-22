package us.teaminceptus.noobysmp.generation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import us.teaminceptus.noobysmp.SMP;

public class ItemManager implements Listener {

	protected SMP plugin;
	
	public ItemManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		plugin.getLogger().info("Loaded Item Manager");
	}
	
	@EventHandler
	public void onItemSpawn(ItemSpawnEvent e) {
		Item itemEntity = e.getEntity();
		ItemStack item = e.getEntity().getItemStack();
		
		if (item == null) return;
		ItemMeta meta = item.getItemMeta();
		if (!(meta.hasLocalizedName())) {
			meta.setLocalizedName(meta.hasDisplayName() ? ChatColor.stripColor(meta.getDisplayName()).toLowerCase().replace(' ', '_') : item.getType().name().toLowerCase());
		}
		item.setItemMeta(meta);
		
		itemEntity.setItemStack(item);
	}
	
	@EventHandler
	public void onInventoryUpdate(InventoryClickEvent e) {
		InventoryView view = e.getView();
		if (!(e.getWhoClicked() instanceof Player p)) return;
		
		for (ItemStack item : view.getTopInventory().getContents()) {
			if (item == null) continue;
			if (!(item.hasItemMeta())) continue;
			ItemMeta meta = item.getItemMeta();
			if (!(meta.hasLocalizedName())) {
				meta.setLocalizedName(meta.hasDisplayName() ? ChatColor.stripColor(meta.getDisplayName()).toLowerCase().replace(' ', '_') : item.getType().name().toLowerCase());
			}
			
			item.setItemMeta(meta);
		}
		
		for (ItemStack item : view.getBottomInventory().getContents()) {
			if (item == null) continue;
			if (!(item.hasItemMeta())) continue;
			ItemMeta meta = item.getItemMeta();
			if (!(meta.hasLocalizedName())) {
				meta.setLocalizedName(meta.hasDisplayName() ? ChatColor.stripColor(meta.getDisplayName()).toLowerCase().replace(' ', '_') : item.getType().name().toLowerCase());
			}
			item.setItemMeta(meta);
		}
		
		for (ItemStack item : p.getInventory().getContents()) {
			if (item == null) continue;
			if (!(item.hasItemMeta())) continue;
			ItemMeta meta = item.getItemMeta();
			if (!(meta.hasLocalizedName())) {
				meta.setLocalizedName(meta.hasDisplayName() ? ChatColor.stripColor(meta.getDisplayName()).toLowerCase().replace(' ', '_') : item.getType().name().toLowerCase());
			}
			item.setItemMeta(meta);
		}
	}
}

package us.teaminceptus.noobysmp.inventory;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import com.google.common.collect.ImmutableMap;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.recipes.SMPRecipe;
import us.teaminceptus.noobysmp.recipes.SMPRecipe.AnvilData;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.inventoryholder.BlockHolder;

public class BlockInventories implements Listener {
	
	protected SMP plugin;
	
	public BlockInventories(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public static final Inventory getAnvilInventory() {
		Inventory inv = Generator.genGUI(27, "Anvil", new BlockHolder(Material.ANVIL));
		
		inv.setItem(11, Items.Inventory.GUI_PANE);
		for (int i = 13; i < 16; i++) inv.setItem(i, Items.Inventory.GUI_PANE);
		
		return inv;
	}
	
	private static final Map<Material, Inventory> inventoryMap = ImmutableMap.<Material, Inventory>builder()
			.put(Material.ANVIL, getAnvilInventory())
			
			.build();
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		InventoryView view = e.getView();
		Inventory inv = view.getTopInventory();
		if (!(e.getWhoClicked() instanceof Player p)) return;
		PlayerConfig config = new PlayerConfig(p);
		if (inv.getHolder() == null) return;
		
		if (inv.getHolder() instanceof BlockHolder holder) {
			e.setCancelled(true);
			
			if (holder.getMaterial() == Material.ANVIL) {
				// Generate Recipe
				if (inv.getItem(10) != null && inv.getItem(12) != null) {
					for (AnvilData data : SMPRecipe.getAnvilRecipes().values()) {
						if (Items.compareLocalization(inv.getItem(10), data.getInput()) && Items.compareLocalization(inv.getItem(12), data.getCombination())) {
							// Check Level
							String outputL = data.getResult().getItemMeta().getLocalizedName();
							
							if (SMPMaterial.getByLocalization(outputL) != null) {
								SMPMaterial m = SMPMaterial.getByLocalization(outputL);
								
								if (m.getLevelUnlocked() < config.getLevel()) {
									
								}
							}
						}
					}
				}
				
				
				// Take Item
				
				
				// Other Inventory Handles
				if (inv.getItem(10) == null || inv.getItem(12) == null) {
					inv.setItem(16, null);
				}
			}
			
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.useInteractedBlock() == Result.DENY) return;
		if (e.getClickedBlock() == null) return;
		Player p = e.getPlayer();
		Block b = e.getClickedBlock();
		
		Material type = b.getType();
		if (inventoryMap.get(type) == null) return;
		
		p.openInventory(inventoryMap.get(type));
	}

}

package us.teaminceptus.noobysmp.generation;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.google.common.collect.ImmutableMap;
import com.jeff_media.customblockdata.CustomBlockData;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

public class BlockManager implements Listener {
	
	protected SMP plugin;
	private NamespacedKey typeKey;
	
	public BlockManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.typeKey =new NamespacedKey(plugin, "type");
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Block b = e.getBlock();
		if (!(e.getItemInHand().hasItemMeta())) return;
		if (!(e.getItemInHand().getItemMeta().hasLocalizedName())) return;
		String id = e.getItemInHand().getItemMeta().getLocalizedName();
		
		if (AbilityItem.getByLocalization(id) != null) {
			e.setCancelled(true);
			return;
		}
		
		PersistentDataContainer c = new CustomBlockData(b, plugin);
		c.set(typeKey, PersistentDataType.STRING, id);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		final Block b = e.getBlock();
		PersistentDataContainer c = new CustomBlockData(b, plugin);
		if (!(c.has(typeKey, PersistentDataType.STRING))) return;
		
		String type = c.get(typeKey, PersistentDataType.STRING);
		
		if (SMPMaterial.getByLocalization(type) != null) {
			SMPMaterial m = SMPMaterial.getByLocalization(type);
			ItemStack item = m.getItem();
			e.setDropItems(false);
			
			if (SMPMaterial.ORE_DROPS.containsKey(m)) {
				b.getWorld().dropItemNaturally(b.getLocation(), SMPMaterial.ORE_DROPS.get(m).getItem());
			} else b.getWorld().dropItemNaturally(b.getLocation(), item);
		}
	}
	
	public static final Map<Material, SMPMaterial> REPLACEABLES = ImmutableMap.<Material, SMPMaterial>builder()
			.put(Material.END_STONE, SMPMaterial.ENDERITE_ORE)
			.put(Material.DIAMOND_ORE, SMPMaterial.RUBY_ORE)
			.put(Material.DEEPSLATE_DIAMOND_ORE, SMPMaterial.DEEPSLATE_RUBY_ORE)
			.build();
	
	public static final Map<Material, SMPMaterial> TITAN_REPLACEABLES = ImmutableMap.<Material, SMPMaterial>builder()
			.put(Material.COAL_ORE, SMPMaterial.BEDROCK_ORE)
			.put(Material.DEEPSLATE_COAL_ORE, SMPMaterial.DEEPSLATE_BEDROCK_ORE)
			.build();
	
}

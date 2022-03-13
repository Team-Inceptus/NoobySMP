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

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.util.BlockPDC;

public class BlockManager implements Listener {
	
	protected SMP plugin;
	private static NamespacedKey typeKey;
	
	public BlockManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		typeKey = new NamespacedKey(plugin, "type");
	}

	public static record ReplaceData(Material replace, int chance) {};
	
	public static void setType(Block b, SMPMaterial m) {
		m.setBlock(b);
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
		
		PersistentDataContainer c = new BlockPDC(b);
		c.set(typeKey, PersistentDataType.STRING, id);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		final Block b = e.getBlock();
		PersistentDataContainer c = new BlockPDC(b);
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
	
	public static final Map<SMPMaterial, ReplaceData> REPLACEABLES = ImmutableMap.<SMPMaterial, ReplaceData>builder()
			.put(SMPMaterial.RUBY_ORE, new ReplaceData(Material.DIAMOND_ORE, 50))
			.put(SMPMaterial.DEEPSLATE_RUBY_ORE, new ReplaceData(Material.DEEPSLATE_DIAMOND_ORE, 50))
			.put(SMPMaterial.ENDERITE_ORE, new ReplaceData(Material.END_STONE, 5))
			.build();
	
	public static final Map<Material, SMPMaterial> TITAN_REPLACEABLES = ImmutableMap.<Material, SMPMaterial>builder()
			.put(Material.STONE, SMPMaterial.TITAN_STONE)
			.put(Material.DEEPSLATE, SMPMaterial.TITAN_DEEPSLATE)
			.put(Material.OAK_LOG, SMPMaterial.HARDENED_OAK_LOG)
			.put(Material.SPRUCE_LOG, SMPMaterial.HARDENED_SPRUCE_LOG)
			.put(Material.ACACIA_LOG, SMPMaterial.HARDENED_ACACIA_LOG)
			.put(Material.JUNGLE_LOG, SMPMaterial.HARDENED_JUNGLE_LOG)
			.put(Material.DARK_OAK_LOG, SMPMaterial.HARDENED_DARK_OAK_LOG)
			.put(Material.BIRCH_LOG, SMPMaterial.HARDENED_BIRCH_LOG)		

			.put(Material.COAL_ORE, SMPMaterial.BEDROCK_ORE)
			.put(Material.DEEPSLATE_COAL_ORE, SMPMaterial.DEEPSLATE_BEDROCK_ORE)
			.put(Material.COPPER_ORE, SMPMaterial.AMBER_ORE)
			.put(Material.DEEPSLATE_COPPER_ORE, SMPMaterial.DEEPSLATE_AMBER_ORE)
			.put(Material.IRON_ORE, SMPMaterial.APATITE_ORE)
			.put(Material.DEEPSLATE_IRON_ORE, SMPMaterial.DEEPSLATE_APATITE_ORE)
			.put(Material.LAPIS_ORE, SMPMaterial.JADE_ORE)
			.put(Material.DEEPSLATE_LAPIS_ORE, SMPMaterial.DEEPSLATE_JADE_ORE)
			.put(Material.GOLD_ORE, SMPMaterial.TOPAZ_ORE)
			.put(Material.DEEPSLATE_GOLD_ORE, SMPMaterial.DEEPSLATE_TOPAZ_ORE)
			.put(Material.DIAMOND_ORE, SMPMaterial.SAPPHIRE_ORE)
			.put(Material.DEEPSLATE_DIAMOND_ORE, SMPMaterial.DEEPSLATE_SAPPHIRE_ORE)
			.put(Material.EMERALD_ORE, SMPMaterial.QARDITE_ORE)
			.put(Material.DEEPSLATE_EMERALD_ORE, SMPMaterial.DEEPSLATE_QARDITE_ORE)

			.build();
	
}

package us.teaminceptus.noobysmp.generation;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.google.common.collect.ImmutableMap;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

public class BlockManager implements Listener {
	
	protected SMP plugin;
	
	public BlockManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Block b = e.getBlock();
		if (!(e.getItemInHand().hasItemMeta())) return;
		if (!(e.getItemInHand().getItemMeta().hasLocalizedName())) return;
		
		if (AbilityItem.getByLocalization(e.getItemInHand().getItemMeta().getLocalizedName()) != null) {
			e.setCancelled(true);
			return;
		}
		
		b.setMetadata("type", new FixedMetadataValue(plugin, e.getItemInHand().getItemMeta().getLocalizedName()));
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		final Block b = e.getBlock();
		try {
			String type = b.getMetadata("type").stream().filter(v -> v.getOwningPlugin() instanceof SMP).toList().get(0).asString();
			
			if (SMPMaterial.getByLocalization(type) != null) {
				SMPMaterial m = SMPMaterial.getByLocalization(type);
				ItemStack item = m.getItem();
				e.setDropItems(false);
				
				if (SMPMaterial.ORE_DROPS.containsKey(m)) {
					b.getWorld().dropItemNaturally(b.getLocation(), SMPMaterial.ORE_DROPS.get(m).getItem());
				} else b.getWorld().dropItemNaturally(b.getLocation(), item);
			}
		} catch (ArrayIndexOutOfBoundsException err) {
			// do nothing, block does not apply
		}
	}
	
	public static final Map<Material, SMPMaterial> REPLACEABLES = ImmutableMap.<Material, SMPMaterial>builder()
			.put(Material.END_STONE, SMPMaterial.ENDERITE_ORE)
			.put(Material.DIAMOND_ORE, SMPMaterial.RUBY_ORE)
			.put(Material.DEEPSLATE_DIAMOND_ORE, SMPMaterial.DEEPSLATE_RUBY_ORE)
			.build();
	
}

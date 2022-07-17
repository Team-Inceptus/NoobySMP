package us.teaminceptus.noobysmp.generation;

import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.util.BlockPDC;

public class BlockManager implements Listener {
	
	protected final SMP plugin;
	private static NamespacedKey typeKey;
	
	public BlockManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		typeKey = new NamespacedKey(plugin, "type");
	}

	public record ReplaceData(SMPMaterial replace, int intScale, int chance) {
		public ReplaceData(SMPMaterial replace, int chance) {
			this(replace, 100, chance);
		}
	}

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
		Player p = e.getPlayer();
		PersistentDataContainer c = new BlockPDC(b);
		
		if (!(c.has(typeKey, PersistentDataType.STRING))) return;

		if (b.getWorld().getName().equalsIgnoreCase("world_titan")) {
			e.setDropItems(false);
			b.getWorld().dropItemNaturally(b.getLocation(), TitanManager.getReplaceable(b).getItem());
			return;
		}


		int fortuneMultiplier = 1;

		if (p.getInventory().getItemInMainHand() != null) {
			ItemStack item = p.getInventory().getItemInMainHand();

			
			if (item.hasItemMeta() && item.getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_BLOCKS))
				fortuneMultiplier = 1 + r.nextInt(item.getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS));
		}

		String type = c.get(typeKey, PersistentDataType.STRING);
		
		if (SMPMaterial.getByLocalization(type) != null) {
			SMPMaterial m = SMPMaterial.getByLocalization(type);
			ItemStack item = m.getItem();
			e.setDropItems(false);
			
			if (SMPMaterial.ORE_DROPS.containsKey(m)) {
				for (int i = 0; i < fortuneMultiplier; i++) b.getWorld().dropItemNaturally(b.getLocation(), SMPMaterial.ORE_DROPS.get(m).getItem());
			} else b.getWorld().dropItemNaturally(b.getLocation(), item);
		}
	}

	private static final Random r = new Random();
	
	public static final Map<Material, ReplaceData> REPLACEABLES = ImmutableMap.<Material, ReplaceData>builder()
			.put(Material.DIAMOND_ORE, new ReplaceData(SMPMaterial.RUBY_ORE, 50))
			.put(Material.DEEPSLATE_DIAMOND_ORE, new ReplaceData(SMPMaterial.DEEPSLATE_RUBY_ORE, 50))
			.put(Material.END_STONE, new ReplaceData(SMPMaterial.ENDERITE_ORE, 5))
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

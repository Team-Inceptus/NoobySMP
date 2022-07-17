package us.teaminceptus.noobysmp.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Drop;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.SMPBoss;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.recipes.SMPRecipe.AnvilData;
import us.teaminceptus.noobysmp.recipes.SMPRecipe.FurnaceData;
import us.teaminceptus.noobysmp.recipes.SMPRecipe.SmithingData;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

public class RecipeManager implements Listener {

	public static final String RING = "III_I I_III";

	protected final SMP plugin;
	// _ for separator in rows
	
	public static final String PICKAXE = "III_ S _ S ";
	public static final String AXE = "II _IS _ S ";
	public static final String AXE_2 = " II_ SI_ S ";
	public static final String HOE = "II _ S _ S ";
	public static final String HOE_2 = " II_ S _ S ";
	public static final String SHOVEL = " I _ S _ S ";
	public static final String SWORD = " I _ I _ S ";

	public static final String BOW = "RI _R I_RI ";
	public static final String TRIDENT = " II_ SI_S  ";

	public static final String HELMET = "III_I I";
	public static final String CHESTPLATE = "I I_III_III";	
	public static final String LEGGINGS = "III_I I_I I";
	public static final String BOOTS = "I I_I I";

	public static final String[] ALL_NAMES = {
		"PICKAXE",
		"AXE",
		"AXE_2",
		"SWORD",
		"HOE",
		"HOE_2",
		"SHOVEL",
		"HELMET",
		"CHESTPlATE",
		"LEGGINGS",
		"BOOTS",
	};

	public static final String BLOCK_9 = "III_III_III";
	public static final String BLOCK_4 = "II _II ";
	
	public static HashMap<Character, ItemStack> vanillaShape(ItemStack item, String smap) {
		HashMap<Character, ItemStack> map = new HashMap<>();
		
		map.put('I', item);
		if (smap.contains("S")) map.put('S', new ItemStack(Material.STICK));
		if (smap.contains("R")) map.put('R', new ItemStack(Material.STRING));

		return map;
	}

	public static HashMap<Character, Material> vanillaShape(Material mat, String smap) {
		HashMap<Character, Material> map = new HashMap<>();
		
		map.put('I', mat);
		if (smap.contains("S")) map.put('S', Material.STICK);
		if (smap.contains("R")) map.put('R', Material.STRING);

		return map;
	}

	public static HashMap<Character, ItemStack> vanillaShape(SMPMaterial item, String smap) {
		return vanillaShape(item.getItem(), smap);
	}
	
	private void createBossDropRecipes() {
		plugin.getLogger().info("Loading Boss Drop Recipes...");
		// Craftables from Boss Drops
		new SMPRecipe(SMPMaterial.AQUATIC_AXE, AXE, vanillaShape(SMPMaterial.AQUATIC_CORE, AXE));
		new SMPRecipe(SMPMaterial.AQUATIC_PICKAXE, PICKAXE, vanillaShape(SMPMaterial.AQUATIC_CORE, PICKAXE));
		new SMPRecipe(SMPMaterial.AQUATIC_SHOVEL, SHOVEL, vanillaShape(SMPMaterial.AQUATIC_CORE, SHOVEL));
		new SMPRecipe(SMPMaterial.AQUATIC_HOE, HOE, vanillaShape(SMPMaterial.AQUATIC_CORE, HOE));
		
		// Smithing Recipes
		new SMPRecipe(new ItemStack(Material.CROSSBOW), SMPMaterial.END_CORE.getItem(), SMPMaterial.END_CROSSBOW.getItem());
		
		new SMPRecipe(SMPMaterial.DAMAGED_WITHERING_BOOTS.getItem(), SMPMaterial.NETHER_CORE.getItem(), SMPMaterial.PATCHED_WITHERING_BOOTS.getItem());
		new SMPRecipe(SMPMaterial.PATCHED_WITHERING_BOOTS.getItem(), SMPMaterial.NETHERITE_STAR.getItem(), SMPMaterial.IMPROVED_WITHERING_BOOTS.getItem());
		new SMPRecipe(SMPMaterial.IMPROVED_WITHERING_BOOTS.getItem(), SMPMaterial.BEDROCK_INGOT.getItem(), SMPMaterial.SUPER_WITHERING_BOOTS.getItem());
	}
	
	public static Map<Character, ItemStack> enrichment(ItemStack item) {
		return Map.of('I', item, 'E', SMPMaterial.CHARGED_ESSENCE.getItem());
	}
	
	public static final String ENRICHMENT = "III_IEI_III";

	public static Map<Character, ItemStack> meliorate(ItemStack item, SMPMaterial ore) {
		return Map.of('O', ore.getItem(), 'I', item);
	}

	public static final String MELIORATE = "OOO_OIO_OOO";

	public static Map<Character, ItemStack> scroll(ItemStack item) {
		return Map.of('E', SMPMaterial.ENDERITE.getItem(), 'I', item, 'R', SMPMaterial.RAW_SCROLL.getItem());
	}

	public static final String SCROLL = "EIE_IRI_EIE";

	private void createAbilityRecipes() {
		plugin.getLogger().info("Loading Ability Recipes...");
		
		// Crafting Recipes
		new SMPRecipe(SMPMaterial.RAW_SCROLL, "GDG_APA_GDG", Map.of('G', SMPMaterial.GOLDEN_ENCHANT.getItem(), 'D', SMPMaterial.DIAMOND_ENCHANT.getItem(), 'A', SMPMaterial.AMETHYST_ENCHANT.getItem()));

		new SMPRecipe(AbilityItem.INFINIBALL, "FFF_FTF_FFF", Map.of('F', new ItemStack(Material.FIRE_CHARGE), 'T', new ItemStack(Material.TNT)));
		
		new SMPRecipe(SMPMaterial.UNCHARGED_ESSENCE, BLOCK_9, vanillaShape(Material.NETHER_STAR, BLOCK_9));
		
		new SMPRecipe(SMPMaterial.CHARGED_END_CORE, ENRICHMENT, Map.of('I', SMPMaterial.END_CORE.getItem(), 'E', SMPMaterial.CHARGED_ESSENCE.getItem()));
		new SMPRecipe(SMPMaterial.CHARGED_NETHER_CORE, ENRICHMENT, Map.of('I', SMPMaterial.NETHER_CORE.getItem(), 'E', SMPMaterial.CHARGED_ESSENCE.getItem()));
		
		new SMPRecipe(SMPMaterial.TITAN_CORE, "NLE_DQD_ELN", Map.of('N', SMPMaterial.CHARGED_NETHER_CORE.getItem(), 'L', SMPMaterial.LIFE_CORE.getItem(), 'D', SMPMaterial.TITAN_DEEPSLATE.getItem(), 'Q', SMPMaterial.QARDITE_BLOCK.getItem(), 'E', SMPMaterial.CHARGED_END_CORE.getItem()));
		
		// Scrolls & Scroll Materials
		new SMPRecipe(SMPMaterial.ELECTRICIY_ROD, "FFF_RRR_FFF", Map.of('F', new ItemStack(Material.FLINT_AND_STEEL), 'R', new ItemStack(Material.LIGHTNING_ROD)));
		
		new SMPRecipe(SMPMaterial.TRINITE_SHARD, "TTT_TET_TTT", Map.of('T', new ItemStack(Material.TNT), 'E', SMPMaterial.ENDERITE.getItem()));
		new SMPRecipe(SMPMaterial.TRINITE_BLOCK, BLOCK_4, vanillaShape(SMPMaterial.TRINITE_SHARD, BLOCK_4));

		new SMPRecipe(AbilityItem.SCROLL_SATURATION, SCROLL, scroll(new ItemStack(Material.CAKE)));
		new SMPRecipe(AbilityItem.SCROLL_MULTIBREAK, SCROLL, scroll(SMPMaterial.RUBY_PICKAXE.getItem()));
		new SMPRecipe(AbilityItem.SCROLL_ELECTRIC, SCROLL, scroll(SMPMaterial.ELECTRICIY_ROD.getItem()));
		new SMPRecipe(AbilityItem.SCROLL_EXPLOSION, SCROLL, scroll(SMPMaterial.TRINITE_BLOCK.getItem()));

		// Enrichments & Meliorates, and their materials
		new SMPRecipe(SMPMaterial.SPONGE_DUST, BLOCK_9, vanillaShape(Material.SPONGE, BLOCK_9));
		new SMPRecipe(SMPMaterial.SUPER_SLIMEBALL, BLOCK_9, vanillaShape(Material.SLIME_BLOCK, BLOCK_9));
		new SMPRecipe(SMPMaterial.FLOTATION_MOLD, RING, vanillaShape(SMPMaterial.SPONGE_DUST, RING));
		new SMPRecipe(SMPMaterial.REINFORCED_TRINITE, "BTB_TBT_BTB", Map.of('B', SMPMaterial.COMPRESSED_BEDROCK.getItem(), 'T', SMPMaterial.COMPRESSED_TRINITE_BLOCK.getItem()));

		new SMPRecipe(AbilityItem.STRENGTH_MELIORATE, MELIORATE, meliorate(new ItemStack(Material.IRON_INGOT), SMPMaterial.RUBY_BLOCK));
		new SMPRecipe(AbilityItem.REPLENISH_MELIORATE, MELIORATE, meliorate(new ItemStack(Material.POPPY), SMPMaterial.RUBY));
		new SMPRecipe(AbilityItem.STICKY_MELIORATE, MELIORATE, meliorate(new ItemStack(Material.HONEY_BLOCK), SMPMaterial.RUBY));
		new SMPRecipe(AbilityItem.POISON_MELIORATE, MELIORATE, meliorate(new ItemStack(Material.POISONOUS_POTATO), SMPMaterial.RUBY));
		new SMPRecipe(AbilityItem.WITHER_MELIORATE, MELIORATE, meliorate(new ItemStack(Material.WITHER_SKELETON_SKULL), SMPMaterial.RUBY_BLOCK));
		new SMPRecipe(AbilityItem.SOAKING_MELIORATE, MELIORATE, meliorate(SMPMaterial.SPONGE_DUST.getItem(), SMPMaterial.RUBY_BLOCK));
		new SMPRecipe(AbilityItem.SLIMY_MELIORATE, MELIORATE, meliorate(SMPMaterial.SUPER_SLIMEBALL.getItem(), SMPMaterial.RUBY));

		new SMPRecipe(AbilityItem.SNOWY_ENRICHMENT, ENRICHMENT, enrichment(new ItemStack(Material.SNOW_BLOCK)));
		new SMPRecipe(AbilityItem.SWAMP_ENRICHMENT, ENRICHMENT, enrichment(new ItemStack(Material.VINE)));
		new SMPRecipe(AbilityItem.AQUATIC_ENRICHMENT, ENRICHMENT, enrichment(SMPMaterial.AQUATIC_CORE.getItem()));
		new SMPRecipe(AbilityItem.NETHER_ENRICHMENT, ENRICHMENT, enrichment(SMPMaterial.CHARGED_NETHER_CORE.getItem()));
		new SMPRecipe(AbilityItem.END_ENRICHMENT, ENRICHMENT, enrichment(SMPMaterial.CHARGED_END_CORE.getItem()));
		new SMPRecipe(AbilityItem.TITAN_ENRICHMENT, ENRICHMENT, enrichment(SMPMaterial.TITAN_CORE.getItem()));
		new SMPRecipe(AbilityItem.BUOYANT_ENRICHMENT, ENRICHMENT, enrichment(SMPMaterial.FLOTATION_MOLD.getItem()));

		// Anvil Reicpes
		new SMPRecipe(AbilityItem.OCASSUS_BOW_1.getItem(), SMPMaterial.END_BOW.getItem(), AbilityItem.OCASSUS_BOW_2.getItem(), 20);
		new SMPRecipe(new ItemStack(Material.CROSSBOW), AbilityItem.OCASSUS_BOW_1.getItem(), AbilityItem.OCASSUS_CROSSBOW.getItem(), 20);
		new SMPRecipe(new ItemStack(Material.TRIDENT), AbilityItem.OCASSUS_BOW_1.getItem(), AbilityItem.OCASSUS_TRIDENT.getItem(), 20);
		
	}
	
	private void createRecipes() {
		// Crafting Vanilla Items
		new SMPRecipe(new ItemStack(Material.BUNDLE), "LLL_L L_LLL", new HashMap<>(Map.of('L', Material.LEATHER)));
		new SMPRecipe(new ItemStack(Material.EXPERIENCE_BOTTLE), "   _ I _ B ", new HashMap<>(Map.of('I', Material.GOLD_INGOT, 'B', Material.GLASS_BOTTLE)));
		
		// Crafting Recipes
		new SMPRecipe(SMPMaterial.RUBY_HELMET, HELMET, vanillaShape(SMPMaterial.RUBY, HELMET));
		new SMPRecipe(SMPMaterial.RUBY_CHESTPLATE, CHESTPLATE, vanillaShape(SMPMaterial.RUBY, CHESTPLATE));
		new SMPRecipe(SMPMaterial.RUBY_LEGGINGS, LEGGINGS, vanillaShape(SMPMaterial.RUBY, LEGGINGS));
		new SMPRecipe(SMPMaterial.RUBY_BOOTS, BOOTS, vanillaShape(SMPMaterial.RUBY, BOOTS));
		
		new SMPRecipe(SMPMaterial.RUBY_SWORD, SWORD, vanillaShape(SMPMaterial.RUBY, SWORD));
		new SMPRecipe(SMPMaterial.RUBY_AXE, AXE, vanillaShape(SMPMaterial.RUBY, AXE));
		new SMPRecipe(SMPMaterial.RUBY_PICKAXE, PICKAXE, vanillaShape(SMPMaterial.RUBY, PICKAXE));
		new SMPRecipe(SMPMaterial.RUBY_SHOVEL, SHOVEL, vanillaShape(SMPMaterial.RUBY, SHOVEL));
		new SMPRecipe(SMPMaterial.RUBY_HOE, HOE, vanillaShape(SMPMaterial.RUBY, HOE));
		
		new SMPRecipe(SMPMaterial.RUBY_BLOCK, BLOCK_9, vanillaShape(SMPMaterial.RUBY, BLOCK_9));
		
		
		new SMPRecipe(SMPMaterial.COPPER_SWORD, SWORD, vanillaShape(Material.COPPER_INGOT, SWORD));
		new SMPRecipe(SMPMaterial.COPPER_AXE, AXE, vanillaShape(Material.COPPER_INGOT, AXE));
		new SMPRecipe(SMPMaterial.COPPER_PICKAXE, PICKAXE, vanillaShape(Material.COPPER_INGOT, PICKAXE));
		new SMPRecipe(SMPMaterial.COPPER_SHOVEL, SHOVEL, vanillaShape(Material.COPPER_INGOT, SHOVEL));
		new SMPRecipe(SMPMaterial.COPPER_HOE, HOE, vanillaShape(Material.COPPER_INGOT, HOE));
		
		
		new SMPRecipe(SMPMaterial.EMERALD_SWORD, SWORD, vanillaShape(Material.EMERALD, SWORD));
		new SMPRecipe(SMPMaterial.EMERALD_BOW, BOW, vanillaShape(Material.EMERALD, BOW));
		new SMPRecipe(SMPMaterial.EMERALD_TRIDENT, TRIDENT, vanillaShape(Material.EMERALD, TRIDENT));
		
		
		new SMPRecipe(SMPMaterial.BLACKSTONE_HELMET, HELMET, vanillaShape(Material.BLACKSTONE, HELMET));
		new SMPRecipe(SMPMaterial.BLACKSTONE_CHESTPLATE, CHESTPLATE, vanillaShape(Material.BLACKSTONE, CHESTPLATE));
		new SMPRecipe(SMPMaterial.BLACKSTONE_LEGGINGS, LEGGINGS, vanillaShape(Material.BLACKSTONE, LEGGINGS));
		new SMPRecipe(SMPMaterial.BLACKSTONE_BOOTS, BOOTS, vanillaShape(Material.BLACKSTONE, BOOTS));
		
		
		new SMPRecipe(SMPMaterial.QUARTZ_HELMET, HELMET, vanillaShape(Material.QUARTZ, HELMET));
		new SMPRecipe(SMPMaterial.QUARTZ_CHESTPLATE, CHESTPLATE, vanillaShape(Material.QUARTZ, CHESTPLATE));
		new SMPRecipe(SMPMaterial.QUARTZ_LEGGINGS, LEGGINGS, vanillaShape(Material.QUARTZ, LEGGINGS));
		new SMPRecipe(SMPMaterial.QUARTZ_BOOTS, BOOTS, vanillaShape(Material.QUARTZ, BOOTS));
		
		
		new SMPRecipe(SMPMaterial.TINY_EXPERIENCE_BAG, "BBB_BEB_BBB", new HashMap<>(Map.of('B', Material.BUNDLE, 'E', Material.EXPERIENCE_BOTTLE)));
		// Level 1
		new SMPRecipe(SMPMaterial.ENDERITE, BLOCK_9, vanillaShape(SMPMaterial.ENDER_FRAGMENT, BLOCK_9));
		new SMPRecipe(SMPMaterial.ENDERITE_HELMET, HELMET, vanillaShape(SMPMaterial.ENDERITE, HELMET));
		new SMPRecipe(SMPMaterial.ENDERITE_CHESTPLATE, CHESTPLATE, vanillaShape(SMPMaterial.ENDERITE, CHESTPLATE));
		new SMPRecipe(SMPMaterial.ENDERITE_LEGGINGS, LEGGINGS, vanillaShape(SMPMaterial.ENDERITE, LEGGINGS));
		new SMPRecipe(SMPMaterial.ENDERITE_BOOTS, BOOTS, vanillaShape(SMPMaterial.ENDERITE, BOOTS));
		
		new SMPRecipe(SMPMaterial.ENDERITE_SWORD, SWORD, vanillaShape(SMPMaterial.ENDERITE, SWORD));
		new SMPRecipe(SMPMaterial.ENDERITE_AXE, AXE, vanillaShape(SMPMaterial.ENDERITE, AXE));
		new SMPRecipe(SMPMaterial.ENDERITE_PICKAXE, PICKAXE, vanillaShape(SMPMaterial.ENDERITE, PICKAXE));
		new SMPRecipe(SMPMaterial.ENDERITE_SHOVEL, SHOVEL, vanillaShape(SMPMaterial.ENDERITE, SHOVEL));
		new SMPRecipe(SMPMaterial.ENDERITE_HOE, HOE, vanillaShape(SMPMaterial.ENDERITE, HOE));
		new SMPRecipe(SMPMaterial.ENDERITE_BOW, BOW, vanillaShape(SMPMaterial.ENDERITE, BOW));
		
		
		new SMPRecipe(SMPMaterial.NETHERITE_BEACON, "GGG_GSG_OOO",new HashMap<>(Map.of('G', new ItemStack(Material.GLASS), 'S', SMPMaterial.NETHERITE_STAR.getItem(), 'O', new ItemStack(Material.OBSIDIAN))));
		
		// Level 2 - 4
		new SMPRecipe(SMPMaterial.ENCHANTED_GOLD, RING, vanillaShape(Material.GOLD_INGOT, RING));
		new SMPRecipe(SMPMaterial.ENCHANTED_GOLD_BLOCK, BLOCK_9, vanillaShape(SMPMaterial.ENCHANTED_GOLD, BLOCK_9));
		new SMPRecipe(SMPMaterial.GOLDEN_ENCHANT, "III_IBI_III", new HashMap<>(Map.of('I', SMPMaterial.ENCHANTED_GOLD.getItem(), 'B', new ItemStack(Material.EXPERIENCE_BOTTLE))));
		
		new SMPRecipe(SMPMaterial.ENCHANTED_DIAMOND, "DDD_D D_DDD", new HashMap<>(Map.of('D', Material.DIAMOND)));
		new SMPRecipe(SMPMaterial.ENCHANTED_DIAMOND_BLOCK, BLOCK_9, vanillaShape(SMPMaterial.ENCHANTED_DIAMOND_BLOCK, BLOCK_9));
		new SMPRecipe(SMPMaterial.DIAMOND_ENCHANT, "III_IBI_III", new HashMap<>(Map.of('I', SMPMaterial.ENCHANTED_DIAMOND.getItem(), 'B', new ItemStack(Material.EXPERIENCE_BOTTLE))));
		
		new SMPRecipe(SMPMaterial.AMETHYST_HELMET, HELMET, vanillaShape(Material.AMETHYST_SHARD, HELMET));
		new SMPRecipe(SMPMaterial.AMETHYST_CHESTPLATE, CHESTPLATE, vanillaShape(Material.AMETHYST_SHARD, CHESTPLATE));
		new SMPRecipe(SMPMaterial.AMETHYST_LEGGINGS, LEGGINGS, vanillaShape(Material.AMETHYST_SHARD, LEGGINGS));
		new SMPRecipe(SMPMaterial.AMETHYST_BOOTS, BOOTS, vanillaShape(Material.AMETHYST_SHARD, BOOTS));
		
		// Level 5 - 14
		new SMPRecipe(SMPMaterial.SMALL_EXPERIENCE_BAG, "ESE_EBE_EEE", new HashMap<>(Map.of('E', new ItemStack(Material.EXPERIENCE_BOTTLE), 'S', new ItemStack(Material.NETHER_STAR), 'B', SMPMaterial.TINY_EXPERIENCE_BAG.getItem())));
		
		
		new SMPRecipe(SMPMaterial.SUPER_NETHERITE_HELMET, HELMET, vanillaShape(Material.NETHERITE_INGOT, HELMET));
		new SMPRecipe(SMPMaterial.SUPER_NETHERITE_CHESTPLATE, CHESTPLATE, vanillaShape(Material.NETHERITE_INGOT, CHESTPLATE));
		new SMPRecipe(SMPMaterial.SUPER_NETHERITE_LEGGINGS, LEGGINGS, vanillaShape(Material.NETHERITE_INGOT, LEGGINGS));
		new SMPRecipe(SMPMaterial.SUPER_NETHERITE_BOOTS, BOOTS, vanillaShape(Material.NETHERITE_INGOT, BOOTS));
		new SMPRecipe(SMPMaterial.SUPER_NETHERITE_SWORD, SWORD, vanillaShape(Material.NETHERITE_INGOT, SWORD));
		
		
		new SMPRecipe(SMPMaterial.ENCHANTED_AMETHYST_SHARD, RING, vanillaShape(Material.AMETHYST_SHARD, RING));
		new SMPRecipe(SMPMaterial.ENCHANTED_AMETHYST_BLOCK, BLOCK_4, vanillaShape(SMPMaterial.ENCHANTED_AMETHYST_SHARD, BLOCK_4));
		new SMPRecipe(SMPMaterial.AMETHYST_ENCHANT, "III_IBI_III", new HashMap<>(Map.of('I', SMPMaterial.ENCHANTED_AMETHYST_SHARD.getItem(), 'B', new ItemStack(Material.EXPERIENCE_BOTTLE))));
		
		
		new SMPRecipe(SMPMaterial.EMERALD_INGOT, RING, vanillaShape(Material.EMERALD, RING));
		new SMPRecipe(SMPMaterial.COMPRESSED_EMERALD_BLOCK, BLOCK_9, vanillaShape(SMPMaterial.EMERALD_INGOT, BLOCK_9));
		
		new SMPRecipe(SMPMaterial.EMERALD_HELMET, HELMET, vanillaShape(Material.EMERALD, HELMET));
		new SMPRecipe(SMPMaterial.EMERALD_CHESTPLATE, CHESTPLATE, vanillaShape(Material.EMERALD, CHESTPLATE));
		new SMPRecipe(SMPMaterial.EMERALD_LEGGINGS, LEGGINGS, vanillaShape(Material.EMERALD, LEGGINGS));
		new SMPRecipe(SMPMaterial.EMERALD_BOOTS, BOOTS, vanillaShape(Material.EMERALD, BOOTS));
		
		
		new SMPRecipe(SMPMaterial.REDSTONE_HELMET, HELMET, vanillaShape(Material.REDSTONE_BLOCK, HELMET));
		new SMPRecipe(SMPMaterial.REDSTONE_CHESTPLATE, CHESTPLATE, vanillaShape(Material.REDSTONE_BLOCK, CHESTPLATE));
		new SMPRecipe(SMPMaterial.REDSTONE_LEGGINGS, LEGGINGS, vanillaShape(Material.REDSTONE_BLOCK, LEGGINGS));
		new SMPRecipe(SMPMaterial.REDSTONE_BOOTS, BOOTS, vanillaShape(Material.REDSTONE_BLOCK, BOOTS));
		
		new SMPRecipe(SMPMaterial.REDSTONE_AXE, AXE, vanillaShape(Material.REDSTONE_BLOCK, AXE));
		new SMPRecipe(SMPMaterial.REDSTONE_PICKAXE, PICKAXE, vanillaShape(Material.REDSTONE_BLOCK, PICKAXE));
		new SMPRecipe(SMPMaterial.REDSTONE_SHOVEL, SHOVEL, vanillaShape(Material.REDSTONE_BLOCK, SHOVEL));
		new SMPRecipe(SMPMaterial.REDSTONE_HOE, HOE, vanillaShape(Material.REDSTONE_BLOCK, HOE));
		new SMPRecipe(SMPMaterial.REDSTONE_BOW, BOW, vanillaShape(Material.REDSTONE_BLOCK, BOW));
		
		
		new SMPRecipe(SMPMaterial.STAR_HELMET, HELMET, vanillaShape(SMPMaterial.NETHERITE_STAR, HELMET));
		new SMPRecipe(SMPMaterial.STAR_CHESTPLATE, CHESTPLATE, vanillaShape(SMPMaterial.NETHERITE_STAR, CHESTPLATE));
		new SMPRecipe(SMPMaterial.STAR_LEGGINGS, LEGGINGS, vanillaShape(SMPMaterial.NETHERITE_STAR, LEGGINGS));
		new SMPRecipe(SMPMaterial.STAR_BOOTS, BOOTS, vanillaShape(SMPMaterial.NETHERITE_STAR, BOOTS));
		// Level 15 - 30
		new SMPRecipe(SMPMaterial.STAR_AXE, AXE, vanillaShape(SMPMaterial.NETHERITE_STAR, AXE));
		new SMPRecipe(SMPMaterial.STAR_PICKAXE, PICKAXE, vanillaShape(SMPMaterial.NETHERITE_STAR, PICKAXE));
		new SMPRecipe(SMPMaterial.STAR_SHOVEL, SHOVEL, vanillaShape(SMPMaterial.NETHERITE_STAR, SHOVEL));
		new SMPRecipe(SMPMaterial.STAR_SCYTHE, HOE, vanillaShape(SMPMaterial.NETHERITE_STAR, HOE));
		
		
		new SMPRecipe(SMPMaterial.MEDIUM_EXPERIENCE_BAG, "ESE_EBE_EEE", new HashMap<>(Map.of('E', SMPMaterial.GOLDEN_ENCHANT.getItem(), 'S', new ItemStack(Material.NETHER_STAR), 'B', SMPMaterial.TINY_EXPERIENCE_BAG.getItem())));
		
		
		new SMPRecipe(SMPMaterial.ENCHANTED_OBSIDIAN, "OOO_OEO_OOO", new HashMap<>(Map.of('O', new ItemStack(Material.OBSIDIAN), 'E', SMPMaterial.COMPRESSED_EMERALD_BLOCK.getItem())));
		new SMPRecipe(SMPMaterial.OBSIDIAN_AXE, AXE, vanillaShape(SMPMaterial.ENCHANTED_OBSIDIAN, AXE));
		new SMPRecipe(SMPMaterial.OBSIDIAN_PICKAXE, PICKAXE, vanillaShape(SMPMaterial.ENCHANTED_OBSIDIAN, PICKAXE));
		new SMPRecipe(SMPMaterial.OBSIDIAN_SHOVEL, SHOVEL, vanillaShape(SMPMaterial.ENCHANTED_OBSIDIAN, SHOVEL));
		new SMPRecipe(SMPMaterial.OBSIDIAN_HOE, HOE, vanillaShape(SMPMaterial.ENCHANTED_OBSIDIAN, HOE));
		new SMPRecipe(SMPMaterial.OBSIDIAN_BOW, BOW, vanillaShape(SMPMaterial.ENCHANTED_OBSIDIAN, BOW));
		
		new SMPRecipe(SMPMaterial.GRAPHENE_DUST, "GGG_OOO_GGG", new HashMap<>(Map.of('G', new ItemStack(Material.GUNPOWDER), 'O', SMPMaterial.ENCHANTED_OBSIDIAN.getItem())));
		new SMPRecipe(SMPMaterial.GRAPHENE, BLOCK_9, vanillaShape(SMPMaterial.GRAPHENE_DUST, BLOCK_9));
		new SMPRecipe(SMPMaterial.GRAPHENE_HELMET, HELMET, vanillaShape(SMPMaterial.GRAPHENE, HELMET));
		new SMPRecipe(SMPMaterial.GRAPHENE_CHESTPLATE, CHESTPLATE, vanillaShape(SMPMaterial.GRAPHENE, CHESTPLATE));
		new SMPRecipe(SMPMaterial.GRAPHENE_LEGGINGS, LEGGINGS, vanillaShape(SMPMaterial.GRAPHENE, LEGGINGS));
		new SMPRecipe(SMPMaterial.GRAPHENE_BOOTS, BOOTS, vanillaShape(SMPMaterial.GRAPHENE, BOOTS));
		
		new SMPRecipe(SMPMaterial.GRAPHENE_AXE, AXE, vanillaShape(SMPMaterial.GRAPHENE, AXE));
		new SMPRecipe(SMPMaterial.GRAPHENE_PICKAXE, PICKAXE, vanillaShape(SMPMaterial.GRAPHENE, PICKAXE));
		new SMPRecipe(SMPMaterial.GRAPHENE_SHOVEL, SHOVEL, vanillaShape(SMPMaterial.GRAPHENE, SHOVEL));
		new SMPRecipe(SMPMaterial.GRAPHENE_HOE, HOE, vanillaShape(SMPMaterial.GRAPHENE, HOE));
		
		
		new SMPRecipe(SMPMaterial.BEDROCK_INGOT, RING, vanillaShape(Material.BEDROCK, RING));
		new SMPRecipe(SMPMaterial.COMPRESSED_BEDROCK, BLOCK_9, vanillaShape(SMPMaterial.BEDROCK_INGOT, BLOCK_9));
		new SMPRecipe(SMPMaterial.BEDROCK_HELMET, HELMET, vanillaShape(SMPMaterial.BEDROCK_INGOT, HELMET));
		new SMPRecipe(SMPMaterial.BEDROCK_CHESTPLATE, CHESTPLATE, vanillaShape(SMPMaterial.BEDROCK_INGOT, CHESTPLATE));
		new SMPRecipe(SMPMaterial.BEDROCK_LEGGINGS, LEGGINGS, vanillaShape(SMPMaterial.BEDROCK_INGOT, LEGGINGS));
		new SMPRecipe(SMPMaterial.BEDROCK_BOOTS, BOOTS, vanillaShape(SMPMaterial.BEDROCK_INGOT, BOOTS));
		
		
		new SMPRecipe(SMPMaterial.LARGE_EXPERIENCE_BAG, "BGB_BEB_BGB", new HashMap<>(Map.of('B', SMPMaterial.COMPRESSED_BEDROCK.getItem(), 'G', SMPMaterial.GRAPHENE.getItem(), 'E', SMPMaterial.LARGE_EXPERIENCE_BAG.getItem())));
		// Titan Recipes (Level 25+)
		new SMPRecipe(SMPMaterial.AMBER_BLOCK, BLOCK_9, vanillaShape(SMPMaterial.AMBER, BLOCK_9));
		
		new SMPRecipe(SMPMaterial.CUT_AMBER_BLOCK, BLOCK_9, vanillaShape(SMPMaterial.CUT_AMBER, BLOCK_9));
		new SMPRecipe(SMPMaterial.AMBER_HELMET, HELMET, vanillaShape(SMPMaterial.CUT_AMBER, HELMET));
		new SMPRecipe(SMPMaterial.AMBER_CHESTPLATE, CHESTPLATE, vanillaShape(SMPMaterial.CUT_AMBER, CHESTPLATE));
		new SMPRecipe(SMPMaterial.AMBER_LEGGINGS, LEGGINGS, vanillaShape(SMPMaterial.CUT_AMBER, LEGGINGS));
		new SMPRecipe(SMPMaterial.AMBER_BOOTS, BOOTS, vanillaShape(SMPMaterial.CUT_AMBER, BOOTS));
		new SMPRecipe(SMPMaterial.AMBER_BOW, BOW, vanillaShape(SMPMaterial.CUT_AMBER, BOW));
		
		new SMPRecipe(SMPMaterial.APATITE, BLOCK_4, vanillaShape(SMPMaterial.APATITE_CRYSTAL, BLOCK_4));
		new SMPRecipe(SMPMaterial.APATITE_CRYSTAL_BLOCK, BLOCK_9, vanillaShape(SMPMaterial.APATITE_CRYSTAL, BLOCK_9));
		new SMPRecipe(SMPMaterial.APATITE_BLOCK, BLOCK_9, vanillaShape(SMPMaterial.APATITE, BLOCK_9));
		
		new SMPRecipe(SMPMaterial.APATITE_SWORD, SWORD, vanillaShape(SMPMaterial.APATITE, SWORD));
		new SMPRecipe(SMPMaterial.APATITE_AXE, AXE, vanillaShape(SMPMaterial.APATITE, AXE));
		new SMPRecipe(SMPMaterial.APATITE_PICKAXE, PICKAXE, vanillaShape(SMPMaterial.APATITE, PICKAXE));
		new SMPRecipe(SMPMaterial.APATITE_SHOVEL, SHOVEL, vanillaShape(SMPMaterial.APATITE, SHOVEL));
		new SMPRecipe(SMPMaterial.APATITE_HOE, HOE, vanillaShape(SMPMaterial.APATITE, HOE));
		
		
		new SMPRecipe(SMPMaterial.HUGE_EXPERIENCE_BAG, "SSS_GBG_SSS", new HashMap<>(Map.of('B', SMPMaterial.LARGE_EXPERIENCE_BAG.getItem(), 'S', SMPMaterial.TITAN_STONE.getItem(), 'G', SMPMaterial.GOLDEN_MATTER.getItem())));
		
		
		new SMPRecipe(SMPMaterial.JADE_BLOCK, BLOCK_9, vanillaShape(SMPMaterial.JADE, BLOCK_9));
		new SMPRecipe(SMPMaterial.JADE_HELMET, HELMET, vanillaShape(SMPMaterial.JADE, HELMET));
		new SMPRecipe(SMPMaterial.JADE_CHESTPLATE, CHESTPLATE, vanillaShape(SMPMaterial.JADE, CHESTPLATE));
		new SMPRecipe(SMPMaterial.JADE_LEGGINGS, LEGGINGS, vanillaShape(SMPMaterial.JADE, LEGGINGS));
		new SMPRecipe(SMPMaterial.JADE_BOOTS, BOOTS, vanillaShape(SMPMaterial.JADE, BOOTS));
		
		
		new SMPRecipe(SMPMaterial.TOPAZ_BLOCK, BLOCK_9, vanillaShape(SMPMaterial.TOPAZ, BLOCK_9));
		
		new SMPRecipe(SMPMaterial.CUT_TOPAZ_BLOCK, BLOCK_9, vanillaShape(SMPMaterial.CUT_TOPAZ, BLOCK_9));
		new SMPRecipe(SMPMaterial.TOPAZ_HELMET, HELMET, vanillaShape(SMPMaterial.CUT_TOPAZ, HELMET));
		new SMPRecipe(SMPMaterial.TOPAZ_CHESTPLATE, CHESTPLATE, vanillaShape(SMPMaterial.CUT_TOPAZ, CHESTPLATE));
		new SMPRecipe(SMPMaterial.TOPAZ_LEGGINGS, LEGGINGS, vanillaShape(SMPMaterial.CUT_TOPAZ, LEGGINGS));
		new SMPRecipe(SMPMaterial.TOPAZ_BOOTS, BOOTS, vanillaShape(SMPMaterial.CUT_TOPAZ, BOOTS));
		
		
		new SMPRecipe(SMPMaterial.TOPAZ_JADE_SWORD, SWORD, vanillaShape(SMPMaterial.TOPAZ_JADE, SWORD));
		new SMPRecipe(SMPMaterial.TOPAZ_JADE_AXE, AXE, vanillaShape(SMPMaterial.TOPAZ_JADE, AXE));
		new SMPRecipe(SMPMaterial.TOPAZ_JADE_PICKAXE, PICKAXE, vanillaShape(SMPMaterial.TOPAZ_JADE, PICKAXE));
		new SMPRecipe(SMPMaterial.TOPAZ_JADE_SHOVEL, SHOVEL, vanillaShape(SMPMaterial.TOPAZ_JADE, SHOVEL));
		new SMPRecipe(SMPMaterial.TOPAZ_JADE_HOE, HOE, vanillaShape(SMPMaterial.TOPAZ_JADE, HOE));
		new SMPRecipe(SMPMaterial.TOPAZ_JADE_BOW, BOW, vanillaShape(SMPMaterial.TOPAZ_JADE, BOW));
		
		
		new SMPRecipe(SMPMaterial.SAPPHIRE_HELMET, HELMET, vanillaShape(SMPMaterial.SAPPHIRE, HELMET));
		new SMPRecipe(SMPMaterial.SAPPHIRE_CHESTPLATE, CHESTPLATE, vanillaShape(SMPMaterial.SAPPHIRE, CHESTPLATE));
		new SMPRecipe(SMPMaterial.SAPPHIRE_LEGGINGS, LEGGINGS, vanillaShape(SMPMaterial.SAPPHIRE, LEGGINGS));
		new SMPRecipe(SMPMaterial.SAPPHIRE_BOOTS, BOOTS, vanillaShape(SMPMaterial.SAPPHIRE, BOOTS));
		
		
		new SMPRecipe(SMPMaterial.QARDITE_BLOCK, BLOCK_9, vanillaShape(SMPMaterial.QARDITE, BLOCK_9));
		new SMPRecipe(SMPMaterial.QARDITE_BOW, BOW, vanillaShape(SMPMaterial.QARDITE_BLOCK, BLOCK_9));
		// Furnace Recipes
		new SMPRecipe(SMPMaterial.RUBY_ORE.getItem(), SMPMaterial.RUBY.getItem(), 10, 100);
		new SMPRecipe(SMPMaterial.DEEPSLATE_RUBY_ORE.getItem(), SMPMaterial.RUBY.getItem(), 10, 100);
		
		new SMPRecipe(SMPMaterial.ENDERITE_ORE.getItem(), SMPMaterial.ENDER_FRAGMENT.getItem(), 20, 120);
		new SMPRecipe(SMPMaterial.ENDERITE.getItem(), SMPMaterial.ENDER_FRAGMENT.getItem(), 10, 80);
		
		// Titan Reicpes
		new SMPRecipe(SMPMaterial.AMBER_ORE.getItem(), SMPMaterial.AMBER.getItem(), 30, 200);
		new SMPRecipe(SMPMaterial.DEEPSLATE_AMBER_ORE.getItem(), SMPMaterial.AMBER.getItem(), 30, 200);
		new SMPRecipe(SMPMaterial.AMBER.getItem(), SMPMaterial.CUT_AMBER.getItem(), 40, 240);
		
		new SMPRecipe(SMPMaterial.APATITE_ORE.getItem(), SMPMaterial.APATITE.getItem(), 30, 200);
		new SMPRecipe(SMPMaterial.DEEPSLATE_APATITE_ORE.getItem(), SMPMaterial.APATITE.getItem(), 30, 200);
		
		new SMPRecipe(SMPMaterial.JADE_ORE.getItem(), SMPMaterial.JADE.getItem(), 40, 240);
		new SMPRecipe(SMPMaterial.DEEPSLATE_JADE_ORE.getItem(), SMPMaterial.JADE.getItem(), 40, 240);
		
		new SMPRecipe(SMPMaterial.SAPPHIRE_ORE.getItem(), SMPMaterial.SAPPHIRE.getItem(), 40, 240);
		new SMPRecipe(SMPMaterial.DEEPSLATE_SAPPHIRE_ORE.getItem(), SMPMaterial.SAPPHIRE.getItem(), 40, 240);
		
		new SMPRecipe(SMPMaterial.QARDITE_ORE.getItem(), SMPMaterial.QARDITE.getItem(), 60, 320);
		new SMPRecipe(SMPMaterial.DEEPSLATE_QARDITE_ORE.getItem(), SMPMaterial.QARDITE.getItem(), 60, 320);		
		// Smithing Recipes
		new SMPRecipe(new ItemStack(Material.ELYTRA), SMPMaterial.ENDERITE.getItem(), SMPMaterial.ENDERITE_ELYTRA.getItem());
		new SMPRecipe(SMPMaterial.EMERALD_TRIDENT.getItem(), SMPMaterial.ENDERITE.getItem(), SMPMaterial.ENDERITE_TRIDENT.getItem());
		
		new SMPRecipe(new ItemStack(Material.BEACON), SMPMaterial.NETHERITE_STAR.getItem(), SMPMaterial.NETHERITE_BEACON.getItem());
		
		new SMPRecipe(new ItemStack(Material.CROSSBOW), SMPMaterial.EMERALD_INGOT.getItem(), SMPMaterial.EMERALD_CROSSBOW.getItem());
		
		new SMPRecipe(SMPMaterial.UNCHARGED_ESSENCE.getItem(), SMPMaterial.NETHERITE_STAR.getItem(), SMPMaterial.CHARGED_ESSENCE.getItem());
		
		new SMPRecipe(SMPMaterial.SUPER_NETHERITE_TRIDENT.getItem(), SMPMaterial.NETHERITE_STAR.getItem(), SMPMaterial.EMERALD_TRIDENT.getItem());
		new SMPRecipe(SMPMaterial.NETHERITE_BEACON.getItem(), SMPMaterial.NETHERITE_STAR.getItem(), SMPMaterial.STAR_BEACON.getItem());
		
		new SMPRecipe(SMPMaterial.EMERALD_TRIDENT.getItem(), SMPMaterial.NETHERITE_STAR.getItem(), SMPMaterial.STAR_TRIDENT.getItem());
		
		new SMPRecipe(SMPMaterial.STAR_TRIDENT.getItem(), SMPMaterial.GRAPHENE.getItem(), SMPMaterial.GRAPHENE_TRIDENT.getItem());
		
		new SMPRecipe(SMPMaterial.APATITE_HOE.getItem(), SMPMaterial.CUT_AMBER_BLOCK.getItem(), SMPMaterial.APATITE_AMBER_SCYTHE.getItem());
		
		new SMPRecipe(new ItemStack(Material.CROSSBOW), SMPMaterial.CUT_TOPAZ_BLOCK.getItem(), SMPMaterial.TOPAZ_CROSSBOW.getItem());
		new SMPRecipe(new ItemStack(Material.BEACON), SMPMaterial.CUT_TOPAZ_BLOCK.getItem(), SMPMaterial.TOPAZ_BEACON.getItem());
		
		new SMPRecipe(SMPMaterial.JADE.getItem(), SMPMaterial.TOPAZ.getItem(), SMPMaterial.TOPAZ_JADE.getItem());
		new SMPRecipe(SMPMaterial.TOPAZ.getItem(), SMPMaterial.JADE.getItem(), SMPMaterial.TOPAZ_JADE.getItem());
		
		new SMPRecipe(SMPMaterial.GRAPHENE_TRIDENT.getItem(), SMPMaterial.SAPPHIRE.getItem(), SMPMaterial.SAPPHIRE_TRIDENT.getItem());
		// Anvil Recipes
		// Remember to add XP after input, addition and result else it will register as a Smithing Recipe
		new SMPRecipe(new ItemStack(Material.NETHER_STAR), new ItemStack(Material.NETHERITE_INGOT), SMPMaterial.NETHERITE_STAR.getItem(), 10);
		
		createBossDropRecipes();
		createAbilityRecipes();
	}
	
	public RecipeManager(SMP plugin) {
		this.plugin = plugin;
		plugin.getLogger().info("Loading recipes...");
		createRecipes();
		plugin.getLogger().info("Successfully loaded " + SMPRecipe.getRecipes().size() + " custom recipes for NoobySMP. Loading other event recipes...");
		Bukkit.getPluginManager().registerEvents(this, plugin);
		plugin.getLogger().info("Successfully loaded recipe events!");
	}
	
	public static final class RecipeHolder extends CancelHolder {
		
		public final int index;
		
		private RecipeHolder(int index) {
			this.index = index;
		}
		
	}

	public static List<Inventory> getRecipeMenus(ItemStack item) {
		List<Inventory> inventories = new ArrayList<>();
		
		if (item == null) return inventories;
		if (!(item.hasItemMeta())) return inventories;
		
		int index = 0;
		
		String invName = "Recipe Finder - Page " + (index + 1);
		
		
		List<SMPRecipe> recipes = SMPRecipe.getByResult(item);

		for (SMPRecipe r : recipes) {
			Inventory inv = Generator.genGUI(45, invName, new RecipeHolder(index));
			
			inv.setItem(13, Items.Inventory.GUI_PANE);
			inv.setItem(14, Items.Inventory.GUI_PANE);
			inv.setItem(15, Items.Inventory.GUI_PANE);
			inv.setItem(16, Items.Inventory.GUI_PANE);
			inv.setItem(22, Items.Inventory.GUI_PANE);
			inv.setItem(25, Items.Inventory.GUI_PANE);
			inv.setItem(31, Items.Inventory.GUI_PANE);
			inv.setItem(32, Items.Inventory.GUI_PANE);
			inv.setItem(33, Items.Inventory.GUI_PANE);
			inv.setItem(34, Items.Inventory.GUI_PANE);
			
			if (r.isCraftingRecipe()) {
				if (SMPMaterial.getByItem(item) != SMPMaterial.getByItem(r.getResult())) continue;
				if (AbilityItem.getByItem(item) != AbilityItem.getByItem(r.getResult())) continue;
				
				Map<Character, ?> ingredientsmap = r.getIngredients();
				
				List<ItemStack> ingredients = new ArrayList<>();
				
				StringBuilder chars = new StringBuilder(r.getRecipeMap().replace("_", "").replace(" ", "A"));
				
				for (int i = chars.length(); i < 9; i++) {
					chars.append("A");
				}
				
				for (char c : chars.toString().toCharArray()) {
					if (Character.toString(c).equals("A")) {
						ingredients.add(new ItemStack(Material.AIR));
						continue;
					}
					
					Object obj = ingredientsmap.get(c);
					
					if (obj instanceof ItemStack i) ingredients.add(i);
					else if (obj instanceof Material m) ingredients.add(new ItemStack(m));
				}
				
				inv.setItem(10, ingredients.get(0));
				inv.setItem(11, ingredients.get(1));
				inv.setItem(12, ingredients.get(2));
	
				inv.setItem(19, ingredients.get(3));
				inv.setItem(20, ingredients.get(4));
				inv.setItem(21, ingredients.get(5));
	
				inv.setItem(28, ingredients.get(6));
				inv.setItem(29, ingredients.get(7));
				inv.setItem(30, ingredients.get(8));
				
				inv.setItem(23, new ItemStack(Material.CRAFTING_TABLE));
				inv.setItem(24, item);
				
				inventories.add(inv);
				index++;
				invName = "Recipe Finder - Page " + (index + 1);
			}
		}
		
		for (FurnaceData d : SMPRecipe.getCookRecipes().values()) {
			if (!(Items.compareLocalization(item, d.getResult()))) continue;
			
			if (SMPMaterial.getByItem(item) != SMPMaterial.getByItem(d.getResult())) continue;
			if (AbilityItem.getByItem(item) != AbilityItem.getByItem(d.getResult())) continue;
			
			Inventory inv = Generator.genGUI(45, invName, new RecipeHolder(index));
			
			inv.setItem(13, Items.Inventory.GUI_PANE);
			inv.setItem(14, Items.Inventory.GUI_PANE);
			inv.setItem(15, Items.Inventory.GUI_PANE);
			inv.setItem(16, Items.Inventory.GUI_PANE);
			inv.setItem(22, Items.Inventory.GUI_PANE);
			inv.setItem(25, Items.Inventory.GUI_PANE);
			inv.setItem(31, Items.Inventory.GUI_PANE);
			inv.setItem(32, Items.Inventory.GUI_PANE);
			inv.setItem(33, Items.Inventory.GUI_PANE);
			inv.setItem(34, Items.Inventory.GUI_PANE);
			
			inv.setItem(10, Items.Inventory.GUI_PANE);
			inv.setItem(11, Items.Inventory.GUI_PANE);
			inv.setItem(12, Items.Inventory.GUI_PANE);
			inv.setItem(19, Items.Inventory.GUI_PANE);
			inv.setItem(21, Items.Inventory.GUI_PANE);
			inv.setItem(28, Items.Inventory.GUI_PANE);
			inv.setItem(29, Items.Inventory.GUI_PANE);
			inv.setItem(30, Items.Inventory.GUI_PANE);
			
			inv.setItem(20, d.getInput());
			
			inv.setItem(23, new ItemStack(Material.FURNACE));
			inv.setItem(24, d.getResult());
			
			ItemStack cookInfo = new ItemStack(Material.COAL);
			ItemMeta meta = cookInfo.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Cook Time: " + (d.getCookTime() / 20) + "s");
			meta.setLore(Collections.singletonList(ChatColor.AQUA + "XP Received: " + d.getExp()));
			cookInfo.setItemMeta(meta);
			
			inv.setItem(32, cookInfo);
			
			inventories.add(inv);
			index++;
			invName = "Recipe Finder - Page " + (index + 1);
		}
		

		
		for (AnvilData d : SMPRecipe.getAnvilRecipes().values()) {
			if (!(Items.compareLocalization(item, d.getResult()))) continue;
			
			if (SMPMaterial.getByItem(item) != SMPMaterial.getByItem(d.getResult())) continue;
			if (AbilityItem.getByItem(item) != AbilityItem.getByItem(d.getResult())) continue;
			
			Inventory inv = Generator.genGUI(45, invName, new RecipeHolder(index));
			
			inv.setItem(13, Items.Inventory.GUI_PANE);
			inv.setItem(14, Items.Inventory.GUI_PANE);
			inv.setItem(15, Items.Inventory.GUI_PANE);
			inv.setItem(16, Items.Inventory.GUI_PANE);
			inv.setItem(22, Items.Inventory.GUI_PANE);
			inv.setItem(25, Items.Inventory.GUI_PANE);
			inv.setItem(31, Items.Inventory.GUI_PANE);
			inv.setItem(32, Items.Inventory.GUI_PANE);
			inv.setItem(33, Items.Inventory.GUI_PANE);
			inv.setItem(34, Items.Inventory.GUI_PANE);
			
			inv.setItem(10, Items.Inventory.GUI_PANE);
			inv.setItem(11, Items.Inventory.GUI_PANE);
			inv.setItem(12, Items.Inventory.GUI_PANE);
			inv.setItem(19, Items.Inventory.GUI_PANE);
			inv.setItem(21, Items.Inventory.GUI_PANE);
			inv.setItem(28, Items.Inventory.GUI_PANE);
			inv.setItem(29, Items.Inventory.GUI_PANE);
			inv.setItem(30, Items.Inventory.GUI_PANE);
			
			inv.setItem(20, d.getInput());
			inv.setItem(22, d.getCombination());
			
			inv.setItem(23, new ItemStack(Material.ANVIL));
			inv.setItem(24, d.getResult());
			
			ItemStack anvilInfo = new ItemStack(Material.IRON_AXE);
			ItemMeta meta = anvilInfo.getItemMeta();
			meta.setDisplayName(ChatColor.AQUA + "XP Received: " + d.getExp());
			anvilInfo.setItemMeta(meta);
			
			inv.setItem(32, anvilInfo);
			
			inventories.add(inv);
			index++;
			invName = "Recipe Finder - Page " + (index + 1);
		}
		
		for (SmithingData d : SMPRecipe.getSmithingRecipes().values()) {
			if (!(Items.compareLocalization(item, d.getResult()))) continue;
			
			if (SMPMaterial.getByItem(item) != SMPMaterial.getByItem(d.getResult())) continue;
			if (AbilityItem.getByItem(item) != AbilityItem.getByItem(d.getResult())) continue;
			
			Inventory inv = Generator.genGUI(45, invName, new RecipeHolder(index));
			
			inv.setItem(13, Items.Inventory.GUI_PANE);
			inv.setItem(14, Items.Inventory.GUI_PANE);
			inv.setItem(15, Items.Inventory.GUI_PANE);
			inv.setItem(16, Items.Inventory.GUI_PANE);
			inv.setItem(22, Items.Inventory.GUI_PANE);
			inv.setItem(25, Items.Inventory.GUI_PANE);
			inv.setItem(31, Items.Inventory.GUI_PANE);
			inv.setItem(32, Items.Inventory.GUI_PANE);
			inv.setItem(33, Items.Inventory.GUI_PANE);
			inv.setItem(34, Items.Inventory.GUI_PANE);
			
			inv.setItem(10, Items.Inventory.GUI_PANE);
			inv.setItem(11, Items.Inventory.GUI_PANE);
			inv.setItem(12, Items.Inventory.GUI_PANE);
			inv.setItem(19, Items.Inventory.GUI_PANE);
			inv.setItem(21, Items.Inventory.GUI_PANE);
			inv.setItem(28, Items.Inventory.GUI_PANE);
			inv.setItem(29, Items.Inventory.GUI_PANE);
			inv.setItem(30, Items.Inventory.GUI_PANE);
			
			inv.setItem(20, d.getInput());
			inv.setItem(22, d.getAddition());
			
			inv.setItem(23, new ItemStack(Material.SMITHING_TABLE));
			inv.setItem(24, d.getResult());
			
			inventories.add(inv);
			index++;
			invName = "Recipe Finder - Page " + (index + 1);
		}
		
		// Other
		
		for (SMPMaterial ore : SMPMaterial.ORE_DROPS.keySet()) {
			if (SMPMaterial.ORE_DROPS.get(ore) != SMPMaterial.getByItem(item)) continue;
			
			Inventory inv = Generator.genGUI(45, invName, new RecipeHolder(index));
			
			for (int i = 10; i < 35; i++) inv.setItem(i, Items.Inventory.GUI_PANE);
			
			inv.setItem(21, Items.itemBuilder(Material.IRON_PICKAXE).addFlags(ItemFlag.HIDE_ATTRIBUTES).build());
			inv.setItem(22, ore.getItem());
			
			inv.setItem(24, SMPMaterial.ORE_DROPS.get(ore).getItem());
			
			inventories.add(inv);
			index++;
			invName = "Recipe Finder - Page " + (index + 1);
		}
		
		for (Class<? extends SMPBoss<? extends Mob>> bossClass : SMPBoss.CLASS_LIST) {
			if (bossClass.isAnnotationPresent(Drop.class)) {
				for (Drop d : bossClass.getDeclaredAnnotationsByType(Drop.class)) {
					if (!(Items.getLocalization(item).equalsIgnoreCase(d.drop()))) continue;
					
					if (SMPMaterial.getByLocalization(d.drop().toLowerCase()) != null) {
						if (SMPMaterial.getByLocalization(d.drop().toLowerCase()) != SMPMaterial.getByItem(item)) continue;
					}
					
					if (AbilityItem.getByLocalization(d.drop().toLowerCase()) != null) {
						if (AbilityItem.getByLocalization(d.drop().toLowerCase()) != AbilityItem.getByItem(item)) continue;
					}
					
					if (Material.getMaterial(d.drop().toUpperCase()) != null) {
						if (Material.getMaterial(d.drop().toUpperCase()) != item.getType()) continue;
					}
					
					
					Inventory inv = Generator.genGUI(45, invName, new RecipeHolder(index));
					
					for (int i = 10; i < 35; i++) inv.setItem(i, Items.Inventory.GUI_PANE);
					
					inv.setItem(21, Items.itemBuilder(Material.DIAMOND_SWORD).addFlags(ItemFlag.HIDE_ATTRIBUTES).build());
					ItemStack boss = new ItemStack(bossClass.getAnnotation(Icon.class).value());
					ItemMeta meta = boss.getItemMeta();
					DisplayName name = bossClass.getAnnotation(DisplayName.class);
					meta.setDisplayName(name.cc() + name.value());
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
					boss.setItemMeta(meta);
					
					inv.setItem(22, boss);
					
					inv.setItem(24, item);
					
					ItemStack amount = new ItemStack(Material.CHEST);
					ItemMeta ameta = amount.getItemMeta();
					ameta.setDisplayName(ChatColor.YELLOW + "Amount: " + d.amount());
					amount.setItemMeta(ameta);
					
					inv.setItem(33, amount);
					
					inventories.add(inv);
					index++;
					invName = "Recipe Finder - Page " + (index + 1);
				}
			}
			
		}

		
		if (inventories.size() > 1) {
			for (int i = 0; i < inventories.size() - 1; i++) {
				Inventory inv = inventories.get(i);
				inv.setItem(41, Items.Inventory.NEXT_ARROW);

				inventories.set(i, inv);
			}
	
			for (int i = 1; i < inventories.size(); i++) {
				Inventory inv = inventories.get(i);
				inv.setItem(39, Items.Inventory.BACK_ARROW);

				inventories.set(i, inv);
			}
		}

		return inventories;
	}

	@EventHandler
	public void onSmelt(BlockCookEvent e) {
		for (FurnaceData data : SMPRecipe.getCookRecipes().values()) {
			if (data.getInput().isSimilar(e.getSource())) {
				e.setResult(data.getResult());
				break;
			}
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player p)) return;
		InventoryView view = e.getView();
		ItemStack clickedItem = e.getCurrentItem();
		if (clickedItem == null) return;
		if (!(clickedItem.hasItemMeta())) return;
		if (!(clickedItem.getItemMeta().hasDisplayName())) return;
		String display = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).toLowerCase();
		Inventory recipeInv = view.getTopInventory();
		
		if (!(recipeInv.getHolder() instanceof RecipeHolder h)) return;
		
		ItemStack chosen = recipeInv.getItem(24);
		if (chosen == null) return;
		
		try {
			// Turn Page
			if (display.equalsIgnoreCase("next") || display.equalsIgnoreCase("back")) {
				List<Inventory> invs = getRecipeMenus(chosen);
				
				int nextInvIndex = h.index + (display.equalsIgnoreCase("next") ? 1 : -1);
				
				Inventory nextInv = invs.get(nextInvIndex);
				if (nextInv == null) return;
				
				p.openInventory(nextInv);
				p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 3F, 1F);
			}
			
		} catch (Exception err) {
			p.sendMessage(ChatColor.RED + "There was an unexpected error. Contact GamerCoder215");
		}
	}
	
	@EventHandler
	public void onCraftClick(PrepareItemCraftEvent e) {
		CraftingInventory inv = e.getInventory();
		if (!(e.getViewers().get(0) instanceof Player p)) return;
		PlayerConfig config = new PlayerConfig(p);
		
		if (inv.getResult() == null) return;
		ItemStack item = inv.getResult();
		
		if (SMPMaterial.getByItem(item) != null) {
			if (SMPMaterial.getByItem(item).getLevelUnlocked() > config.getLevel()) {
				new BukkitRunnable() {
					public void run() {
						inv.setResult(null);
					}
				}.runTask(plugin);
				return;
			}
		}
		
		if (AbilityItem.getByItem(item) != null) {
			if (AbilityItem.getByItem(item).getLevelUnlocked() > config.getLevel()) {
				new BukkitRunnable() {
					public void run() {
						inv.setResult(null);
					}
				}.runTask(plugin);
			}
		}
	}

	@EventHandler
	public void onSmith(PrepareSmithingEvent e) {
		SmithingInventory inv = e.getInventory();
		
		if (!(e.getViewers().get(0) instanceof Player p)) return;
		PlayerConfig config = new PlayerConfig(p);

		for (SmithingData data : SMPRecipe.getSmithingRecipes().values()) {
			ItemStack source = inv.getItem(0);
			ItemStack add = inv.getItem(1);
			
			ItemStack input = (data.getInput().hasItemMeta() && data.getInput().getItemMeta().hasDisplayName() ? data.getInput() : new ItemStack(data.getInput().getType()));
			ItemStack addition = (data.getAddition().hasItemMeta() && data.getAddition().getItemMeta().hasDisplayName() ? data.getAddition() : new ItemStack(data.getAddition().getType()));
			if (input.isSimilar(source) && addition.isSimilar(add)) {
				
				for (SMPRecipe r : SMPRecipe.getByResult(data.getResult())) {
					if (SMPMaterial.getByItem(r.getResult()) != null && SMPMaterial.getByItem(r.getResult()).getLevelUnlocked() > config.getLevel()) {
						e.setResult(null);
						return;
					}

					if (AbilityItem.getByItem(r.getResult()) != null && AbilityItem.getByItem(r.getResult()).getLevelUnlocked() > config.getLevel()) {
						e.setResult(null);
						return;
					}
				}

				e.setResult(data.getResult());
				break;
			}
		}
	}
}
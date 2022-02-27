package us.teaminceptus.noobysmp.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.SmithingInventory;

import net.md_5.bungee.api.ChatColor;
import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.recipes.SMPRecipe.AnvilData;
import us.teaminceptus.noobysmp.recipes.SMPRecipe.FurnaceData;
import us.teaminceptus.noobysmp.recipes.SMPRecipe.SmithingData;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

public class RecipeManager implements Listener {

	public static final String RING = "III_I I_III";

	protected SMP plugin;
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
	
	public static final HashMap<Character, ItemStack> vanillaShape(ItemStack item, String smap) {
		HashMap<Character, ItemStack> map = new HashMap<>();
		
		map.put('I', item);
		if (smap.contains("S")) map.put('S', new ItemStack(Material.STICK));
		if (smap.contains("R")) map.put('R', new ItemStack(Material.STRING));

		return map;
	}

	public static final HashMap<Character, Material> vanillaShape(Material mat, String smap) {
		HashMap<Character, Material> map = new HashMap<>();

		map.put('I', mat);
		if (smap.contains("S")) map.put('S', Material.STICK);
		if (smap.contains("R")) map.put('R', Material.STRING);

		return map;
	}

	public static final HashMap<Character, ItemStack> vanillaShape(SMPMaterial item, String smap) {
		return vanillaShape(item.getItem(), smap);
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
		// Furnace Recipes
		new SMPRecipe(SMPMaterial.RUBY_ORE.getItem(), SMPMaterial.RUBY.getItem(), 10, 100);
		new SMPRecipe(SMPMaterial.DEEPSLATE_RUBY_ORE.getItem(), SMPMaterial.RUBY.getItem(), 10, 100);
		
		new SMPRecipe(SMPMaterial.ENDERITE_ORE.getItem(), SMPMaterial.ENDER_FRAGMENT.getItem(), 20, 120);
		new SMPRecipe(SMPMaterial.ENDERITE.getItem(), SMPMaterial.ENDER_FRAGMENT.getItem(), 10, 80);
		// Smithing Recipes
		new SMPRecipe(new ItemStack(Material.ELYTRA), SMPMaterial.ENDERITE.getItem(), SMPMaterial.ENDERITE_ELYTRA.getItem());
		new SMPRecipe(SMPMaterial.EMERALD_TRIDENT.getItem(), SMPMaterial.ENDERITE.getItem(), SMPMaterial.ENDERITE_TRIDENT.getItem());
		
		new SMPRecipe(new ItemStack(Material.BEACON), SMPMaterial.NETHERITE_STAR.getItem(), SMPMaterial.NETHERITE_BEACON.getItem());
		
		new SMPRecipe(new ItemStack(Material.CROSSBOW), SMPMaterial.EMERALD_INGOT.getItem(), SMPMaterial.EMERALD_CROSSBOW.getItem());
		
		new SMPRecipe(SMPMaterial.SUPER_NETHERITE_TRIDENT.getItem(), SMPMaterial.NETHERITE_STAR.getItem(), SMPMaterial.EMERALD_TRIDENT.getItem());
		new SMPRecipe(SMPMaterial.NETHERITE_BEACON.getItem(), SMPMaterial.NETHERITE_STAR.getItem(), SMPMaterial.STAR_BEACON.getItem());
		
		new SMPRecipe(SMPMaterial.EMERALD_TRIDENT.getItem(), SMPMaterial.NETHERITE_STAR.getItem(), SMPMaterial.STAR_TRIDENT.getItem());
		
		new SMPRecipe(SMPMaterial.STAR_TRIDENT.getItem(), SMPMaterial.GRAPHENE.getItem(), SMPMaterial.GRAPHENE_TRIDENT.getItem());
		// Anvil Recipes
		// Remember to add XP after input, addition and result else it will register as a Smithing Recipe
		new SMPRecipe(new ItemStack(Material.NETHER_STAR), new ItemStack(Material.NETHERITE_INGOT), SMPMaterial.NETHERITE_STAR.getItem(), 10);
		
	}
	
	public RecipeManager(SMP plugin) {
		this.plugin = plugin;
		plugin.getLogger().info("Loading recipes...");
		createRecipes();
		plugin.getLogger().info("Successfully loaded " + Integer.toString(SMPRecipe.getRecipes().size()) + " custom recipes for NoobySMP. Loading other event recipes...");
		Bukkit.getPluginManager().registerEvents(this, plugin);
		plugin.getLogger().info("Successfully loaded events!");
	}
	
	public static class RecipeHolder extends CancelHolder {};

	public static List<Inventory> getRecipeMenus(ItemStack item) {
		List<Recipe> recipes = Bukkit.getRecipesFor(item);
		List<Inventory> inventories = new ArrayList<>();
		
		String invName = "Recipe - " + (item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name().substring(0, 1) + item.getType().name().toLowerCase().substring(1));
		
		for (Recipe r : recipes) {
			Inventory inv = Generator.genGUI(45, invName, new RecipeHolder());

			if (r instanceof ShapedRecipe sh) {
				Map<Integer, ItemStack> ingredients = new HashMap<>();

				int index = 0;
				for (String s : sh.getShape()) {
					for (char c : s.toCharArray()) {
						ItemStack it = (sh.getIngredientMap().get(c) == null ? new ItemStack(Material.AIR) : sh.getIngredientMap().get(c));
						ingredients.put(index, it);
						index++;
					}
				}
			
				inv.setItem(10, ingredients.get(0));
				inv.setItem(11, ingredients.get(1));
				inv.setItem(12, ingredients.get(2));

				inv.setItem(19, ingredients.get(3));
				inv.setItem(20, ingredients.get(4));
				inv.setItem(21, ingredients.get(5));

				inv.setItem(30, ingredients.get(6));
				inv.setItem(31, ingredients.get(7));
				inv.setItem(32, ingredients.get(8));
				
				inv.setItem(22, new ItemStack(Material.CRAFTING_TABLE));
				inv.setItem(23, item);

				inventories.add(inv);
			}
		}
		
		for (AnvilData data : SMPRecipe.getAnvilRecipes().values()) {
			if (!(data.getInput().isSimilar(item))) continue;
			
			Inventory inv = Generator.genGUI(45, invName, new RecipeHolder());
			
			inv.setItem(10, Items.Inventory.GUI_PANE);
			inv.setItem(11, Items.Inventory.GUI_PANE);
			inv.setItem(12, Items.Inventory.GUI_PANE);
			inv.setItem(20, Items.Inventory.GUI_PANE);
			inv.setItem(28, Items.Inventory.GUI_PANE);
			inv.setItem(29, Items.Inventory.GUI_PANE);
			inv.setItem(30, Items.Inventory.GUI_PANE);
			
			inv.setItem(19, data.getInput());
			inv.setItem(21, data.getCombination());
			
			inv.setItem(22, new ItemStack(Material.ANVIL));
			inv.setItem(23, item);
			
			inventories.add(inv);
		}
		
		for (SmithingData data : SMPRecipe.getSmithingRecipes().values()) {
			if (!(data.getInput().isSimilar(item))) continue;
			
			Inventory inv = Generator.genGUI(45, invName, new RecipeHolder());
			
			inv.setItem(10, Items.Inventory.GUI_PANE);
			inv.setItem(11, Items.Inventory.GUI_PANE);
			inv.setItem(12, Items.Inventory.GUI_PANE);
			inv.setItem(20, Items.Inventory.GUI_PANE);
			inv.setItem(28, Items.Inventory.GUI_PANE);
			inv.setItem(29, Items.Inventory.GUI_PANE);
			inv.setItem(30, Items.Inventory.GUI_PANE);
			
			inv.setItem(19, data.getInput());
			inv.setItem(21, data.getAddition());
			
			inv.setItem(22, new ItemStack(Material.SMITHING_TABLE));
			inv.setItem(23, item);
			
			inventories.add(inv);
		}
		
		for (FurnaceData data : SMPRecipe.getCookRecipes().values()) {
			if (!(data.getInput().isSimilar(item))) continue;
			
			Inventory inv = Generator.genGUI(45, invName, new RecipeHolder());
			
			inv.setItem(10, Items.Inventory.GUI_PANE);
			inv.setItem(11, Items.Inventory.GUI_PANE);
			inv.setItem(12, Items.Inventory.GUI_PANE);
			inv.setItem(19, Items.Inventory.GUI_PANE);
			inv.setItem(21, Items.Inventory.GUI_PANE);
			inv.setItem(28, Items.Inventory.GUI_PANE);
			inv.setItem(29, Items.Inventory.GUI_PANE);
			inv.setItem(30, Items.Inventory.GUI_PANE);
			
			inv.setItem(20, data.getInput());
			
			inv.setItem(22, new ItemStack(Material.FURNACE));
			inv.setItem(23, item);
			
			inventories.add(inv);
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
		
		if (!(recipeInv.getHolder() instanceof RecipeHolder)) return;
		
		ItemStack chosen = recipeInv.getItem(23);
		if (chosen == null) return;
		
		// Turn Page
		if (display.equalsIgnoreCase("next") || display.equalsIgnoreCase("back")) {
			List<Inventory> invs = getRecipeMenus(chosen);
			
			int nextInvIndex = invs.indexOf(recipeInv) + (display.equalsIgnoreCase("next") ? 1 : -1);
			
			Inventory nextInv = invs.get(nextInvIndex);
			if (nextInv == null) return;
			
			p.openInventory(nextInv);
			p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 3F, 1F);
		}
	}

	@EventHandler
	public void onSmith(PrepareSmithingEvent e) {
		SmithingInventory inv = e.getInventory();
		for (SmithingData data : SMPRecipe.getSmithingRecipes().values()) {
			ItemStack source = inv.getItem(0);
			ItemStack add = inv.getItem(1);

			if (data.getInput().isSimilar(source) && data.getAddition().isSimilar(add)) {
				e.setResult(data.getResult());
				break;
			}
		}
	}
}
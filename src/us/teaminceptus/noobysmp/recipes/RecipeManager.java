package us.teaminceptus.noobysmp.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import us.teaminceptus.noobysmp.recipes.SMPRecipe.FurnaceData;
import us.teaminceptus.noobysmp.recipes.SMPRecipe.SmithingData;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Items;

public class RecipeManager implements Listener {

	protected SMP plugin;

	// TODO make shape of all vanilla variations; I = Item, S = Stick, etc.
	// _ for separator in rows
	
	public static final String PICKAXE = "III_ S _ S ";
	public static final String AXE = "II _IS _ S ";
	public static final String AXE_2 = " II_ SI_ S ";
	public static final String HOE = "II _ S _ S ";
	public static final String HOE_2 = " II_ S _ S ";
	public static final String SHOVEL = " I _ S _ S ";
	public static final String SWORD = " I _ I _ S ";

	public static final String BOW = "RI _R I_RI ";

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
	
	public static final HashMap<Character, ItemStack> vanillaShape(ItemStack item) {
		HashMap<Character, ItemStack> map = new HashMap<>();
		// TODO after all vanilla variations, substitute other items

		map.put('I', item);
		map.put('S', new ItemStack(Material.STICK));
		map.put('R', new ItemStack(Material.STRING));

		return map;
	}

	public static final HashMap<Character, Material> vanillaShape(Material mat) {
		HashMap<Character, Material> map = new HashMap<>();

		map.put('I', mat);
		map.put('S', Material.STICK);
		map.put('R', Material.STRING);

		return map;
	}

	public static final HashMap<Character, ItemStack> vanillaShape(SMPMaterial item) {
		return vanillaShape(item.getItem());
	}

	private static final HashMap<String, ItemStack> getRegisterMap(SMPMaterial... items) {
		HashMap<String, ItemStack> map = new HashMap<>();
		for (SMPMaterial m : items) {
			for (String s : ALL_NAMES) if (m.name().startsWith(s)) {
				try {
					map.put((String) RecipeManager.class.getDeclaredField(s).get(s), m.getItem());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (m.name().startsWith("AXE")) {
				map.put(AXE_2, m.getItem());
			}

			if (m.name().startsWith("HOE")) {
				map.put(HOE_2, m.getItem());
			}
		}

		return map;
	}
	
	private void createRecipes() {
		// Crafting Recipes
		SMPRecipe.registerMultiple(SMPMaterial.RUBY.getItem(), getRegisterMap(SMPMaterial.RUBY_SWORD, SMPMaterial.RUBY_AXE, SMPMaterial.RUBY_PICKAXE, SMPMaterial.RUBY_SHOVEL, SMPMaterial.RUBY_HOE, SMPMaterial.RUBY_HELMET, SMPMaterial.RUBY_CHESTPLATE, SMPMaterial.RUBY_LEGGINGS, SMPMaterial.RUBY_BOOTS));
		new SMPRecipe(SMPMaterial.RUBY_BLOCK, BLOCK_9, vanillaShape(SMPMaterial.RUBY));
		
		SMPRecipe.registerMultiple(Material.COPPER_INGOT, getRegisterMap(SMPMaterial.COPPER_SWORD, SMPMaterial.COPPER_PICKAXE, SMPMaterial.COPPER_AXE, SMPMaterial.COPPER_HOE, SMPMaterial.COPPER_SHOVEL));
		
		SMPRecipe.registerMultiple(Material.BLACKSTONE, getRegisterMap(SMPMaterial.BLACKSTONE_HELMET, SMPMaterial.BLACKSTONE_CHESTPLATE, SMPMaterial.BLACKSTONE_LEGGINGS, SMPMaterial.BLACKSTONE_BOOTS));
		
		SMPRecipe.registerMultiple(Material.QUARTZ, getRegisterMap(SMPMaterial.QUARTZ_HELMET, SMPMaterial.QUARTZ_CHESTPLATE, SMPMaterial.QUARTZ_LEGGINGS, SMPMaterial.QUARTZ_BOOTS));
		// Furnace Recipes
		new SMPRecipe(SMPMaterial.RUBY_ORE.getItem(), SMPMaterial.RUBY.getItem(), 10, 100);
		new SMPRecipe(SMPMaterial.DEEPSLATE_RUBY_ORE.getItem(), SMPMaterial.RUBY.getItem(), 10, 100);
		// Smithing Recipes
	}
	
	public RecipeManager(SMP plugin) {
		this.plugin = plugin;
		plugin.getLogger().info("Loading recipes...");
		createRecipes();
		plugin.getLogger().info("Successfully loaded " + Integer.toString(SMPRecipe.getRecipes().size()) + " custom recipes for NoobySMP. Loading other event recipes...");
		Bukkit.getPluginManager().registerEvents(this, plugin);
		plugin.getLogger().info("Successfully loaded events!");
	}

	public static List<Inventory> getRecipeMenus(ItemStack item) {
		List<Recipe> recipes = Bukkit.getRecipesFor(item);
		if (recipes.size() < 1) return null;
		List<Inventory> inventories = new ArrayList<>();
		
		for (Recipe r : recipes) {
			Inventory inv = Generator.genGUI(45, "Recipe - " + (item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name().substring(0, 1) + item.getType().name().toLowerCase().substring(1)));

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
		
		// Title Checks
		if (view.getTitle().contains("Recipe - ")) {
			ItemStack chosen = recipeInv.getItem(23);
			if (chosen == null) return;
			
			// Turn Page
			if (display.equalsIgnoreCase("next") || display.equalsIgnoreCase("back")) {
				List<Inventory> invs = getRecipeMenus(chosen);
				
				int nextInvIndex = invs.indexOf(recipeInv) + (display.equalsIgnoreCase("next") ? 1 : -1);
				
				Inventory nextInv = invs.get(nextInvIndex);
				if (nextInv == null) return;
				
				p.openInventory(nextInv);
			}
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
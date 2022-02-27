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
		
		
		new SMPRecipe(SMPMaterial.QUARTZ_HELMET, HELMET, vanillaShape(Material.QUARTZ, HELMET));
		new SMPRecipe(SMPMaterial.QUARTZ_CHESTPLATE, CHESTPLATE, vanillaShape(Material.QUARTZ, CHESTPLATE));
		new SMPRecipe(SMPMaterial.QUARTZ_LEGGINGS, LEGGINGS, vanillaShape(Material.QUARTZ, LEGGINGS));
		new SMPRecipe(SMPMaterial.QUARTZ_BOOTS, BOOTS, vanillaShape(Material.QUARTZ, BOOTS));
		
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
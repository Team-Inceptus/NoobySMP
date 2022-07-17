package us.teaminceptus.noobysmp.recipes;

import static us.teaminceptus.noobysmp.recipes.RecipeManager.AXE;
import static us.teaminceptus.noobysmp.recipes.RecipeManager.AXE_2;
import static us.teaminceptus.noobysmp.recipes.RecipeManager.HOE;
import static us.teaminceptus.noobysmp.recipes.RecipeManager.HOE_2;
import static us.teaminceptus.noobysmp.recipes.RecipeManager.vanillaShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.util.Items;

public class SMPRecipe {

	private static final List<SMPRecipe> recipes = new ArrayList<>();

	private static final Map<String, FurnaceData> furnaceRecipes = new HashMap<>();
	private static final Map<String, SmithingData> smithingRecipes = new HashMap<>();
	private static final Map<String, AnvilData> anvilRecipes = new HashMap<>();
	
	private final Recipe recipe;
	private final boolean eventUsed;
	private final ItemStack result;
	private NamespacedKey key = null;
	
	private Map<Character, ?> ingredients;
	private String recipeMap;
	
	{
		recipes.add(this);
	}
	
	public SMPRecipe(ItemStack result, String recipeMap, Map<Character, ItemStack> ingredients) {
		NamespacedKey key = new NamespacedKey(JavaPlugin.getPlugin(SMP.class), "recipe" + recipes.indexOf(this));
		this.key = key;
		
		ShapedRecipe recipe = new ShapedRecipe(key, result);

		recipe.shape(recipeMap.split("_"));
		
		for (char c : ingredients.keySet()) {
			recipe.setIngredient(c, new ExactChoice(ingredients.get(c)));
		}
		
		if (recipeMap.equals(HOE)) new SMPRecipe(result, HOE_2, vanillaShape(ingredients.get('I'), HOE_2));
		if (recipeMap.equals(AXE)) new SMPRecipe(result, AXE_2, vanillaShape(ingredients.get('I'), AXE_2));
		
		this.ingredients = ingredients;
		this.recipeMap = recipeMap;
		
		Bukkit.addRecipe(recipe);
		this.recipe = recipe;
		this.eventUsed = false;
		
		this.result = result;
	}

	public SMPRecipe(SMPMaterial result, String recipeMap, Map<Character, ItemStack> ingredients) {
		this(result.getItem(), recipeMap, ingredients);
	}
	
	public SMPRecipe(AbilityItem result, String recipeMap, Map<Character, ItemStack> ingredients) {
		this(result.getItem(), recipeMap, ingredients);
	}

	public SMPRecipe(ItemStack result, String recipeMap, HashMap<Character, Material> ingredients) {
		NamespacedKey key = new NamespacedKey(JavaPlugin.getPlugin(SMP.class), "recipe" + recipes.indexOf(this));
		ShapedRecipe recipe = new ShapedRecipe(key, result);
		
		this.key = key;
		
		if (recipeMap.equals(HOE)) new SMPRecipe(result, HOE_2, vanillaShape(ingredients.get('I'), HOE_2));
		if (recipeMap.equals(AXE)) new SMPRecipe(result, AXE_2, vanillaShape(ingredients.get('I'), AXE_2));
		
		recipe.shape(recipeMap.split("_"));

		for (char c : ingredients.keySet()) {
			recipe.setIngredient(c, new MaterialChoice(ingredients.get(c)));
		}
		
		this.ingredients = ingredients;
		this.recipeMap = recipeMap;
		this.result = result;

		Bukkit.addRecipe(recipe);
		this.recipe = recipe;
		this.eventUsed = false;
	}
	
	public final Map<Character, ?> getIngredients() {
		return this.ingredients;
	}
	
	public final NamespacedKey getKey() {
		return this.key;
	}

	public SMPRecipe(SMPMaterial result, String recipeMap, HashMap<Character, Material> ingredients) {
		this(result.getItem(), recipeMap, ingredients);
	}

	public SMPRecipe(ItemStack input, ItemStack result, float exp, int cookTime) {
		this.result = result;
		this.recipe = null;
		this.eventUsed = true;

		String identifier = result.getItemMeta().getLocalizedName();

		furnaceRecipes.put(identifier, new FurnaceData(input, result, exp, cookTime));
	}

	public SMPRecipe(ItemStack input, ItemStack addition, ItemStack result) {
		this.result = result;
		this.recipe = null;
		this.eventUsed = true;

		String identifier = result.getItemMeta().getLocalizedName();

		smithingRecipes.put(identifier, new SmithingData(input, addition, result));
	}
	
	public SMPRecipe(ItemStack input, ItemStack combination, ItemStack result, float exp) {
		this.result = result;
		this.recipe = null;
		this.eventUsed = true;
		
		String identifier = result.getItemMeta().getLocalizedName();
		
		anvilRecipes.put(identifier, new AnvilData(input, combination, result, exp));
	}
	
	public static List<SMPRecipe> getByResult(ItemStack item) {
		List<SMPRecipe> recipes = new ArrayList<>();
		
		for (SMPRecipe r : getRecipes()) {
			if (r.key != null) {
				Recipe rec = Bukkit.getRecipe(r.key);
				
				if (AbilityItem.getByItem(item) != null) {
					if (AbilityItem.getByItem(item) == AbilityItem.getByItem(r.result)) recipes.add(r);

				} else if (SMPMaterial.getByItem(item) != null) {
					if (SMPMaterial.getByItem(item) == SMPMaterial.getByItem(r.result)) recipes.add(r);

				} else {
					if (Items.compareLocalization(item, rec.getResult())) recipes.add(r);

				}
			} else {
				if (getCookRecipes().containsKey(Items.getLocalization(r.result))) recipes.add(r);
				
				if (getSmithingRecipes().containsKey(Items.getLocalization(r.result))) recipes.add(r);
				
				if (getAnvilRecipes().containsKey(Items.getLocalization(r.result))) recipes.add(r);
			}
		}
		
		return recipes;
	}
	
	public final boolean isCraftingRecipe() {
		return !this.eventUsed;
	}

	public final String getRecipeMap() {
		return this.recipeMap;
	}
	
	public final Recipe getRecipe() {
		return this.recipe;
	}

	public static List<SMPRecipe> getRecipes() {
		return recipes;
	}

	public static Map<String, FurnaceData> getCookRecipes() {
		return furnaceRecipes;
	}

	public static Map<String, SmithingData> getSmithingRecipes() {
		return smithingRecipes;
	}
	
	public static Map<String, AnvilData> getAnvilRecipes() {
		return anvilRecipes;
	}
	
	public static List<SMPRecipe> getEventRecipes() {
		return getRecipes().stream().filter(r -> r.eventUsed).toList();
	}
	
	public final ItemStack getResult() {
		return this.result;
	}

	public static class FurnaceData {

		private final int cookTime;
		private final float exp;
		private final ItemStack result;
		private final ItemStack input;
		
		protected FurnaceData(ItemStack input, ItemStack result, float exp, int cookTime) {
			this.cookTime = cookTime;
			this.exp = exp;
			this.result = result;
			this.input = input;
		}

		public final int getCookTime() {
			return this.cookTime;
		}

		public final float getExp() {
			return this.exp;
		}

		public final ItemStack getResult() {
			return this.result;
		}

		public final ItemStack getInput() {
			return this.input;
		}
		
	}
	
	public static class SmithingData {

		final ItemStack input;
		final ItemStack addition;
		final ItemStack result;

		protected SmithingData(ItemStack input, ItemStack addition, ItemStack result) {
			this.input = input;
			this.addition = addition;
			this.result = result;
		}
		
		public ItemStack getInput() { return this.input; }
		public ItemStack getAddition() { return this.addition; }
		public ItemStack getResult() { return this.result; }
		
	}
	
	public static class AnvilData {
		
		final ItemStack input;
		final ItemStack combination;
		final ItemStack result;
		final float exp;
		
		protected AnvilData(ItemStack input, ItemStack combination, ItemStack result, float exp) {
			this.input = input;
			this.combination = combination;
			this.result = result;
			this.exp = exp;
		}
		
		public ItemStack getInput() { return this.input; }
		public ItemStack getCombination() { return this.combination; }
		public ItemStack getResult() { return this.result; }
		public float getExp() {	return this.exp; }
	}
	
	
}
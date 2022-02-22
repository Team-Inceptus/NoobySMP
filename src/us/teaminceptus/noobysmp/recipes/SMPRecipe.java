package us.teaminceptus.noobysmp.recipes;

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
import us.teaminceptus.noobysmp.materials.SMPMaterial;

public class SMPRecipe {

	private static List<SMPRecipe> recipes = new ArrayList<>();

	private static Map<String, FurnaceData> furnaceRecipes = new HashMap<>();
	private static Map<String, SmithingData> smithingRecipes = new HashMap<>();
	private static Map<String, AnvilData> anvilRecipes = new HashMap<>();
	
	private final Recipe recipe;
	private final boolean eventUsed;

	public SMPRecipe(ItemStack result, String recipeMap, Map<Character, ItemStack> ingredients) {
		recipes.add(this);

		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(JavaPlugin.getPlugin(SMP.class), "recipe" + Integer.toString(recipes.indexOf(this))), result);

		recipe.shape(recipeMap.split("_"));
		
		for (char c : ingredients.keySet()) {
			recipe.setIngredient(c, new ExactChoice(ingredients.get(c)));
		}
		
		Bukkit.addRecipe(recipe);
		this.recipe = recipe;
		this.eventUsed = false;
	}

	public SMPRecipe(SMPMaterial result, String recipeMap, Map<Character, ItemStack> ingredients) {
		this(result.getItem(), recipeMap, ingredients);
	}

	public SMPRecipe(ItemStack result, String recipeMap, HashMap<Character, Material> ingredients) {
		recipes.add(this);

		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(JavaPlugin.getPlugin(SMP.class), "recipe" + Integer.toString(recipes.indexOf(this))), result);

		recipe.shape(recipeMap.split("_"));

		for (char c : ingredients.keySet()) {
			recipe.setIngredient(c, new MaterialChoice(ingredients.get(c)));
		}

		Bukkit.addRecipe(recipe);
		this.recipe = recipe;
		this.eventUsed = false;
	}

	public SMPRecipe(SMPMaterial result, String recipeMap, HashMap<Character, Material> ingredients) {
		this(result.getItem(), recipeMap, ingredients);
	}

	public SMPRecipe(ItemStack input, ItemStack result, float exp, int cookTime) {
		recipes.add(this);
		this.recipe = null;
		this.eventUsed = true;

		String identifier = result.getItemMeta().getLocalizedName();

		furnaceRecipes.put(identifier, new FurnaceData(input, result, exp, cookTime));
	}

	public SMPRecipe(ItemStack input, ItemStack addition, ItemStack result) {
		recipes.add(this);
		this.recipe = null;
		this.eventUsed = true;

		String identifier = result.getItemMeta().getLocalizedName();

		smithingRecipes.put(identifier, new SmithingData(input, addition, result));
	}
	
	public SMPRecipe(ItemStack input, ItemStack combination, ItemStack result, float exp) {
		recipes.add(this);
		this.recipe = null;
		this.eventUsed = true;
		
		String identifier = result.getItemMeta().getLocalizedName();
		
		anvilRecipes.put(identifier, new AnvilData(input, combination, result, exp));
	}

	public final Recipe getRecipe() {
		return this.recipe;
	}

	public static final List<SMPRecipe> getRecipes() {
		return recipes;
	}

	public static final Map<String, FurnaceData> getCookRecipes() {
		return furnaceRecipes;
	}

	public static final Map<String, SmithingData> getSmithingRecipes() {
		return smithingRecipes;
	}
	
	public static final Map<String, AnvilData> getAnvilRecipes() {
		return anvilRecipes;
	}
	
	public static final List<SMPRecipe> getEventRecipes() {
		return getRecipes().stream().filter(r -> r.eventUsed).toList();
	}

	public static void registerMultiple(Material base, HashMap<String, ItemStack> map) {
		for (HashMap.Entry<String, ItemStack> entry : map.entrySet()) {
			ItemStack result = entry.getValue();

			new SMPRecipe(result, entry.getKey(), RecipeManager.vanillaShape(base, entry.getKey()));
		}
	}

	public static void registerMultiple(ItemStack base, Map<String, ItemStack> map) {
		for (Map.Entry<String, ItemStack> entry : map.entrySet()) {
			ItemStack result = entry.getValue();

			new SMPRecipe(result, entry.getKey(), RecipeManager.vanillaShape(base, entry.getKey()));
		}
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

		ItemStack input;
		ItemStack addition;
		ItemStack result;

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
		
		ItemStack input;
		ItemStack combination;
		ItemStack result;
		float exp;
		
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
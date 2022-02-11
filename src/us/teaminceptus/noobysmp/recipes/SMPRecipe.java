package us.teaminceptus.noobysmp.recipes;

public class SMPRecipe {

	private static List<SMPRecipe> recipes;

	private static Map<String, FurnaceData> furnaceRecipes;
	private static Map<String, SmithingData> smithingRecipes;
	
	private final Recipe recipe;
	private final boolean eventUsed;
	
	public SMPRecipe(ItemStack result, String recipeMap, Map<Character, ItemStack> ingredients) {
		recipes.add(this);

		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(JavaPlugin.getPlugin(SMP.class), "recipe" + Integer.toString(recipes.indexOf(this)), result);

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

	public SMPRecipe(ItemStack result, String recipeMap, Map<Character, Material> ingredients) {
		recipes.add(this);

		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(JavaPlugin.getPlugin(SMP.class), "recipe" + Integer.toString(recipes.indexOf(this)), result);

		recipe.shape(recipeMap.split(" "));
		for (char c : ingredients.keySet()) {
			recipe.setIngredient(c, new MaterialChoice(ingredients.get(c)));
		}

		Bukkit.addRecipe(recipe);
		this.recipe = recipe;
		this.eventUsed = false;
	}

	public SMPRecipe(SMPMaterial result, String recipeMap, Map<Character, Material> ingredients) {
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

	public static void registerMultiple(Material base, Map<String, ItemStack> map) {
		for (Map.Entry<String, ItemStack> entry : map.entrySet()) {
			ItemStack result = entry.getValue();

			new SMPRecipe(base, entry.getKey(), RecipeManager.vanillaShape(base));
		}
	}

	public static void registerMultiple(ItemStack base, Map<String, ItemStack> map) {
		for (Map.Entry<String, ItemStack> entry : map.entrySet()) {
			ItemStack result = entry.getValue();

			new SMPRecipe(base, entry.getKey(), RecipeManager.vanillaShape(base));
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

		public ItemStack getInput() {
			return this.input;
		}

		public ItemStack getAddition() {
			return this.addition;
		}

		public ItemStack getResult() {
			return this.result;
		}
		
	}
	
}
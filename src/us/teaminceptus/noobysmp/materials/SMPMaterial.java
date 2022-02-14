package us.teaminceptus.noobysmp.materials;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.google.common.collect.ImmutableMap;

import us.teaminceptus.noobysmp.materials.SMPMaterial.CleanOutput.MaterialOutput;
import us.teaminceptus.noobysmp.util.Items;

/**
 * Class used for Custom Materials inside the SMP.
 * Utility Items (i.e. tables, items with inventory) are not included.
 * 
 * 
 */
public enum SMPMaterial {
	
	RUBY(0, Material.DIAMOND, "Ruby"),
	RUBY_SWORD(0, Material.DIAMOND_SWORD, "Ruby Sword", genAttack(6.5, 0.5, 0.7)),
	RUBY_AXE(0, Material.DIAMOND_AXE, "Ruby Axe", genAttack(8, 0.6, 0.6)),
	RUBY_SHOVEL(0, Material.DIAMOND_SHOVEL, "Ruby Shovel"),
	RUBY_HOE(0, Material.DIAMOND_HOE, "Ruby Hoe"),
	RUBY_PICKAXE(0, Material.DIAMOND_PICKAXE, "Ruby Pickaxe"),
	RUBY_HELMET(0, Material.DIAMOND_HELMET, "Ruby Helmet", genArmor(2.5, 1.5, 1)),
	RUBY_CHESTPLATE(0, Material.DIAMOND_CHESTPLATE, "Ruby Chestplate", genArmor(7, 1.5, 1)),
	RUBY_LEGGINGS(0, Material.DIAMOND_LEGGINGS, "Ruby Leggings", genArmor(5, 1.5, 1)),
	RUBY_BOOTS(0, Material.DIAMOND_BOOTS, "Ruby Boots", genArmor(2, 1.5, 1)),
	RUBY_ORE(0, Material.DIAMOND_ORE, "Ruby Ore"),
	DEEPSLATE_RUBY_ORE(0, Material.DEEPSLATE_DIAMOND_ORE, "Deepslate Ruby Ore"),
	RUBY_BLOCK(0, Material.DIAMOND_BLOCK, "Ruby Block"),

	COPPER_PICKAXE(0, Material.GOLDEN_PICKAXE, "Copper Pickaxe", genTool(3, 0)),
	COPPER_SWORD(0, Material.GOLDEN_SWORD, "Copper Sword", genAttack(5, 0, 0.5), genTool(3, 0)),
	COPPER_AXE(0, Material.GOLDEN_AXE, "Copper Axe", genAttack(7, 0, 0.4), genTool(3, 0)),
	COPPER_SHOVEL(0, Material.GOLDEN_SHOVEL, "Copper Shovel", genTool(3, 0)),
	COPPER_HOE(0, Material.GOLDEN_HOE, "Copper Hoe", genTool(3, 0)),

	EMERALD_SWORD(0, Material.DIAMOND_SWORD, "Emerald Sword", genAttack(7, 0.2, 0.5)),
	EMERALD_BOW(0, Material.BOW, "Emerald Bow", genBow(4, 1, false, false)),
	EMERALD_TRIDENT(0, Material.TRIDENT, "Emerald Trident", genAttack(8, 0.3, 0.5)),

	BLACKSTONE_HELMET(0, Material.LEATHER_HELMET, "Blackstone Helmet", genArmor(3.5, 0.5, 0), genTool(3, 0), rgb("121212")),
	BLACKSTONE_CHESTPLATE(0, Material.LEATHER_CHESTPLATE, "Blackstone Chestplate", genArmor(6, 0.5, 0), genTool(3, 0), rgb("121212")),
	BLACKSTONE_LEGGINGS(0, Material.LEATHER_LEGGINGS, "Blackstone Leggings", genArmor(4.5, 0.5, 0), genTool(3, 0), rgb("121212")),
	BLACKSTONE_BOOTS(0, Material.LEATHER_BOOTS, "Blackstone Boots", genArmor(3, 0.5, 0), genTool(3, 0), rgb("121212")),
	
	QUARTZ_HELMET(0, Material.IRON_HELMET, "Quartz Helmet", genArmor(4.5, 0.5, 0.5), genTool(1, 0)),
	QUARTZ_CHESTPLATE(0, Material.IRON_CHESTPLATE, "Quartz Boots", genArmor(4.5, 0.5, 0.5), genTool(1, 0)),
	QUARTZ_LEGGINGS(0, Material.IRON_LEGGINGS, "Quartz Boots", genArmor(4.5, 0.5, 0.5), genTool(1, 0)),
	QUARTZ_BOOTS(0, Material.IRON_BOOTS, "Quartz Boots", genArmor(4.5, 0.5, 0.5), genTool(1, 0)),
	
	// Level 1
	ENDERITE_ORE(1, Material.IRON_BLOCK, "Enderite Ore"),
	ENDER_FRAGMENT(1, Material.IRON_NUGGET, "Ender Fragment"),
	ENDERITE(1, Material.IRON_INGOT, "Enderite"),
	ENDERITE_SWORD(1, Material.NETHERITE_SWORD, "Enderite Sword", genAttack(10, 0.2, 0.5)),
	ENDERITE_AXE(1, Material.NETHERITE_AXE, "Enderite Axe", genAttack(11, 0.3, 0.35), genTool(4, 1)),
	ENDERITE_PICKAXE(1, Material.NETHERITE_PICKAXE, "Enderite Pickaxe", genTool(4, 1)),
	ENDERITE_SHOVEL(1, Material.NETHERITE_SHOVEL, "Enderite Shovel", genTool(4, 1)),
	ENDERITE_HOE(1, Material.NETHERITE_HOE, "Enderite Hoe", genTool(4, 1)),
	ENDERITE_BOW(1, Material.BOW, "Enderite Bow", genBow(7, 2, true, false)),
	ENDERITE_TRIDENT(1, Material.TRIDENT, "Enderite Trident", genAttack(11, 0.25, 0.4)),
	ENDERITE_HELMET(1, Material.NETHERITE_HELMET, "Enderite Helmet", genArmor(4, 2, 2)),
	ENDERITE_CHESTPLATE(1, Material.NETHERITE_CHESTPLATE, "Enderite Chestplate", genArmor(8, 2, 2)),
	ENDERITE_LEGGINGS(1, Material.NETHERITE_LEGGINGS, "Enderite Leggings", genArmor(7, 2, 2)),
	ENDERITE_BOOTS(1, Material.NETHERITE_BOOTS, "Enderite Boots", genArmor(4, 2, 2)),
	ENDERITE_ELYTRA(1, Material.ELYTRA, "Enderite Elytra", genArmor(6, 2, 2), genTool(2, 0)),
	
	RAW_ENCHANT(1, Material.LIGHT_GRAY_DYE, "Raw Enchant", glint()),
	// Level 2 - 4
	ENCHANTED_GOLD(2, Material.GOLD_INGOT, "Enchanted Gold", glint()),
	ENCHANTED_GOLD_NUGGET(2, Material.GOLD_NUGGET, "Enchanted Gold Nugget", glint()),
	ENCHANTED_GOLD_BLOCK(2, Material.GOLD_BLOCK, "Enchanted Gold Block", glint()),
	GOLDEN_ENCHANT(2, Material.YELLOW_DYE, "Golden Enchant", glint()),
	
	ENCHANTED_DIAMOND(3, Material.DIAMOND, "Enchanted Diamond", glint()),
	ENCHANTED_DIAMOND_BLOCK(3, Material.DIAMOND_BLOCK, "Enchanted Diamond Block", glint()),
	DIAMOND_ENCHANT(3, Material.LIGHT_BLUE_DYE, "Diamond Enchant", glint()),
	
	NETHERITE_STAR(4, Material.NETHER_STAR, "Netherite Star"),
	
	AMETHYST_HELMET(4, Material.LEATHER_HELMET, "Amethyst Helmet", genArmor(6, 1, 0.5), genTool(4, 0), rgb("6909a0")),
	AMETHYST_CHESTPLATE(4, Material.LEATHER_CHESTPLATE, "Amethyst Chestplate", genArmor(9, 1, 0.5), genTool(4, 0), rgb("6909a0")),
	AMETHYST_LEGGINGS(4, Material.LEATHER_LEGGINGS, "Amethyst Leggings", genArmor(8, 1, 0.5), genTool(4, 0), rgb("6909a0")),
	AMETHYST_BOOTS(4, Material.LEATHER_BOOTS, "Amethyst Boots", genArmor(5, 1, 0.5), genTool(4, 0), rgb("6909a0")),
	
	// Level 5 - 14
	UNCHARGED_ESSENCE(5, Material.GRAY_DYE, "Uncharged Essence"),
	CHARGED_ESSENCE(5, Material.WHITE_DYE, "Charged Essence"),
	
	SUPER_NETHERITE_HELMET(6, Material.NETHERITE_HELMET, "Super Netherite Helmet", genArmor(11, 3, 3), genTool(7, 0)),
	SUPER_NETHERITE_CHESTPLATE(6, Material.NETHERITE_CHESTPLATE, "Super Netherite Chestplate", genArmor(14, 3, 3), genTool(7, 0)),
	SUPER_NETHERITE_LEGGINGS(6, Material.NETHERITE_LEGGINGS, "Super Netherite Leggings", genArmor(13, 3, 3), genTool(7, 0)),
	SUPER_NETHERITE_BOOTS(6, Material.NETHERITE_BOOTS, "Super Netherite Boots", genArmor(10, 3, 3), genTool(7, 0)),
	SUPER_NETHERITE_SWORD(6, Material.NETHERITE_SWORD, "Super Netherite Sword", genAttack(12, 0.4, 0.5), genTool(7, 0)),
	
	ENCHANTED_AMETHYST_SHARD(7, Material.AMETHYST_SHARD, "Enchanted Amethyst Shard"),
	ENCHANTED_AMETHYST_BLOCK(7, Material.AMETHYST_BLOCK, "Enchanted Amethyst Block"),
	AMETHYST_ENCHANT(7, Material.PURPLE_DYE, "Amethyst Enchant", glint()),
	
	EMERALD_INGOT(8, Material.EMERALD, "Emerald Ingot", glint()),
	EMERALD_NUGGET(8, Material.GOLD_NUGGET, "Emerald Nugget", glint()),
	
	EMERALD_HELMET(9, Material.LEATHER_HELMET, "Emerald Helmet", genArmor(20, 5, 4), genTool(10, 0), Color.LIME),
	EMERALD_CHESTPLATE(9, Material.LEATHER_CHESTPLATE, "Emerald Chestplate", genArmor(32, 5, 4), genTool(10, 0), Color.LIME),
	EMERALD_LEGGINGS(9, Material.LEATHER_LEGGINGS, "Emerald Leggings", genArmor(28, 5, 4), genTool(10, 0), Color.LIME),
	EMERALD_BOOTS(9, Material.LEATHER_BOOTS, "Emerald Boots", genArmor(16, 5, 4), genTool(10, 0), Color.LIME),
	
	EMERALD_CROSSBOW(10, Material.CROSSBOW, "Emerald Crossbow", genCrossbow(10, 3, true)),
	
	// TODO make energy core with heads using NBT constructor
	
	REDSTONE_HELMET(12, Material.LEATHER_HELMET, "Redstone Helmet", genArmor(25, 6, 4), genTool(12, 0), Color.RED),
	REDSTONE_CHESTPLATE(12, Material.LEATHER_CHESTPLATE, "Redstone Chestplate", genArmor(40, 6, 4), genTool(12, 0), Color.RED),
	REDSTONE_LEGGINGS(12, Material.LEATHER_LEGGINGS, "Redstone Leggings", genArmor(35, 6, 4), genTool(12, 0), Color.RED),
	REDSTONE_BOOTS(12, Material.LEATHER_BOOTS, "Redstone Boots", genArmor(20, 6, 4), genTool(12, 0), Color.RED),
	REDSTONE_AXE(12, Material.DIAMOND_AXE, "Redstone Axe", genAttack(15, 0.6, 0.6), genTool(12, 5)),
	REDSTONE_PICKAXE(12, Material.DIAMOND_PICKAXE, "Redstone Pickaxe", genTool(12, 5)),
	REDSTONE_SHOVEL(12, Material.DIAMOND_SHOVEL, "Redstone Shovel", genTool(12, 5)),
	REDSTONE_HOE(12, Material.DIAMOND_HOE, "Redstone Hoe", genTool(12, 5)),
	
	STAR_HELMET(14, Material.LEATHER_HELMET, "Star Helmet", genArmor(27, 6.5, 4), genTool(13, 0), Color.WHITE),
	STAR_CHESTPLATE(14, Material.LEATHER_CHESTPLATE, "Star Chestplate", genArmor(42, 6.5, 4), genTool(13, 0), Color.WHITE),
	STAR_LEGGINGS(14, Material.LEATHER_LEGGINGS, "Star Leggings", genArmor(22, 6.5, 4), genTool(13, 0), Color.WHITE),
	STAR_BOOTS(14, Material.LEATHER_BOOTS, "Star Boots", genArmor(22, 6.5, 4), genTool(13, 0), Color.WHITE),
	// Level 15 - 30
	STAR_SCYTHE(15, Material.IRON_HOE, "Star Scythe", genAttack(25, 0.8, 0.65), genTool(14, 6)),
	STAR_PICKAXE(15, Material.IRON_PICKAXE, "Star Pickaxe", genTool(14, 6)),
	STAR_AXE(15, Material.IRON_AXE, "Star Axe", genTool(14, 6)),
	STAR_SHOVEL(15, Material.IRON_SHOVEL, "Star Shovel", genTool(14, 6)),
	
	
	// Level 30+
	;
	
	private final String localization;
	
	private final String name;
	private final ItemStack item;
	private final int levelUnlocked;
	
	private final ChatColor cc;
	
	private static final HashMap<Attribute, AttributeModifier> genAttack(double attack, double knockback, double speed) {
		HashMap<Attribute, AttributeModifier> map = new HashMap<>();

		if (attack > 0) map.put(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("", attack, Operation.ADD_NUMBER));
		if (knockback > 0) map.put(Attribute.GENERIC_ATTACK_KNOCKBACK, new AttributeModifier("", knockback, Operation.ADD_NUMBER));
		if (speed > 0) map.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier("", speed, Operation.ADD_NUMBER));

		return map;
	}

	private static final Color rgb(String hex) {
		return Color.fromRGB(Integer.valueOf(hex.substring(0, 2), 16), 
												 Integer.valueOf(hex.substring(2, 4), 16), 
												 Integer.valueOf(hex.substring(4, 6), 16));
	}
	
	private static final HashMap<Enchantment, Integer> glint() {
		return new HashMap<Enchantment, Integer>(ImmutableMap.<Enchantment, Integer>builder().put(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
	}

	private static final HashMap<Enchantment, Integer> genTool(int unbreaking, int efficiency) {
		HashMap<Enchantment, Integer> map = new HashMap<>();

		if (unbreaking > 0) map.put(Enchantment.DURABILITY, unbreaking);
		if (efficiency > 0) map.put(Enchantment.DIG_SPEED, efficiency);
		
		return map;
	}

	private static final HashMap<Attribute, AttributeModifier> genArmor(double armor, double toughness, double knockback) {
		HashMap<Attribute, AttributeModifier> map = new HashMap<>();

		if (armor > 0) map.put(Attribute.GENERIC_ARMOR, new AttributeModifier("", armor, Operation.ADD_NUMBER));
		if (toughness > 0) map.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier("", toughness, Operation.ADD_NUMBER));
		if (knockback > 0) map.put(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier("", knockback, Operation.ADD_NUMBER));

		return map;
	}

	private static final HashMap<Enchantment, Integer> genBow(int power, int punch, boolean infinity, boolean flame) {
		HashMap<Enchantment, Integer> map = new HashMap<>();

		if (power > 0) map.put(Enchantment.ARROW_DAMAGE, power);
		if (punch > 0) map.put(Enchantment.ARROW_KNOCKBACK, punch);
		if (infinity) map.put(Enchantment.ARROW_INFINITE, 1);
		if (flame) map.put(Enchantment.ARROW_FIRE, 1);
		
		return map;
	}
	
	private static final HashMap<Enchantment, Integer> genCrossbow(int piercing, int quickCharge, boolean multishot) {
		HashMap<Enchantment, Integer> map = new HashMap<>();

		if (quickCharge > 0) map.put(Enchantment.ARROW_DAMAGE, quickCharge);
		if (piercing > 0) map.put(Enchantment.ARROW_KNOCKBACK, piercing);
		if (multishot) map.put(Enchantment.MULTISHOT, 1);
		
		return map;
	}
	
	private SMPMaterial(int level, Material original, String name) {
		if (level < 5) this.cc = ChatColor.WHITE;
		else if (level >= 5 && level < 15) this.cc = ChatColor.AQUA;
		else if (level >= 15 && level < 30) this.cc = ChatColor.LIGHT_PURPLE;
		else if (level > 30) this.cc = ChatColor.GOLD;
		else this.cc = ChatColor.WHITE;
		
		this.levelUnlocked = level;
		this.localization = name.toLowerCase().replace(" ", "_");
		
		ItemStack item = new ItemStack(original);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(cc + name);
		meta.setLocalizedName(name.toLowerCase().replace(" ", "_"));
		item.setItemMeta(meta);
		this.item = item;

		this.name = name;
	}

	private SMPMaterial(int level, Material original, String name, Map<Attribute, AttributeModifier> modifiers, Map<Enchantment, Integer> enchants) {
		if (level < 5) this.cc = ChatColor.WHITE;
		else if (level >= 5 && level < 15) this.cc = ChatColor.AQUA;
		else if (level >= 15 && level < 30) this.cc = ChatColor.LIGHT_PURPLE;
		else if (level > 30) this.cc = ChatColor.GOLD;
		else this.cc = ChatColor.WHITE;
		
		this.levelUnlocked = level;
		this.localization = name.toLowerCase().replace(" ", "_");
		
		ItemStack item = new ItemStack(original);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(cc + name);
		meta.setLocalizedName(name.toLowerCase().replace(" ", "_"));
		if (modifiers != null)
			for (Attribute a : modifiers.keySet()) {
				meta.addAttributeModifier(a, modifiers.get(a));
			}
		if (enchants != null)
			for (Enchantment e : enchants.keySet()) {
				meta.addEnchant(e, enchants.get(e), true);
			}
		
		if (meta.getEnchantLevel(Enchantment.DURABILITY) > 100) {
			meta.removeEnchant(Enchantment.DURABILITY);
			meta.setUnbreakable(true);
		}
		
		item.setItemMeta(meta);
		this.item = item;

		this.name = name;
	} 

	private SMPMaterial(int level, Material original, String name, Map<Attribute, AttributeModifier> modifiers, Map<Enchantment, Integer> enchants, Color clr) {
		this(level, original, name, modifiers, enchants);

		LeatherArmorMeta meta = (LeatherArmorMeta) this.item.getItemMeta();
		meta.setColor(clr);
		this.item.setItemMeta(meta);
	}

	private SMPMaterial(int level, Material original, String name, Map<Attribute, AttributeModifier> modifiers, Color clr) {
		this(level, original, name, modifiers, null, clr);
	}

	private SMPMaterial(int level, Material original, String name, HashMap<Enchantment, Integer> enchants, Color clr) {
		this(level, original, name, null, enchants, clr);
	}

	private SMPMaterial(int level, Material original, String name, Map<Attribute, AttributeModifier> modifiers) {
		this(level, original, name, modifiers, (Map<Enchantment, Integer>) null);
	}

	private SMPMaterial(int level, Material original, String name, HashMap<Enchantment, Integer> enchants) {
		this(level, original, name, null, enchants);
	}
	
	private SMPMaterial(int level, String name, String nbt) {
		if (level < 5) this.cc = ChatColor.WHITE;
		else if (level >= 5 && level < 15) this.cc = ChatColor.AQUA;
		else if (level >= 15 && level < 30) this.cc = ChatColor.LIGHT_PURPLE;
		else if (level > 30) this.cc = ChatColor.GOLD;
		else this.cc = ChatColor.WHITE;
		
		this.item = Items.fromNBT(nbt);
		this.levelUnlocked = level;
		this.localization = name.toLowerCase().replace(" ", "_");
		this.name = name;
	}

	public static final ItemStack getItem(String localization) {
		return getByLocalization(localization).getItem();
	}

	public static final SMPMaterial getByLocalization(String localization) {
		for (SMPMaterial m : values()) {
			if (m.localization.equals(localization)) return m;
		}

		return null;
	}
	
	public static interface CleanOutput {
		
		Material getMaterial();
		
		int getAmount();
		
		public static class MaterialOutput implements CleanOutput {
			
			private int amount;
			private Material m;
			
			public MaterialOutput(Material m, int amount) {
				this.amount = amount;
				this.m = m;
			}
			
			public MaterialOutput(Material m) {
				this.m = m;
				this.amount = 1;
			}
			
			@Override
			public Material getMaterial() {
				return this.m;
			}

			@Override
			public int getAmount() {
				return this.amount;
			}
			
		}
	}
	
	public static Map<Material, CleanOutput> getMaterialCleanables() {
		return ImmutableMap.<Material, CleanOutput>builder()
				.put(Material.GOLD_INGOT, new MaterialOutput(Material.RAW_GOLD))
				.put(Material.IRON_INGOT, new MaterialOutput(Material.RAW_IRON))
				.put(Material.COPPER_INGOT, new MaterialOutput(Material.RAW_COPPER))
				.put(Material.NETHERITE_INGOT, new MaterialOutput(Material.NETHERITE_SCRAP, 4))
				
				.build();
	}

	public final String getName() {
		return this.name;
	}
	
	public final ItemStack getItem() {
		return getItem(1);
	}

	public final int getLevelUnlocked() {
		return this.levelUnlocked;
	}

	public final ItemStack getItem(int amount) {
		if (amount < 1 || amount > 127) throw new IllegalArgumentException("Amount cannot be less than 1 or bigger than 127");

		ItemStack newItem = item;
		newItem.setAmount(amount);

		return newItem;
	}

	
}
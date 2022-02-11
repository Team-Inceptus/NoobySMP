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
	RUBY_ORE(0, Material.DIAMOND_ORE, "Ruby Ore", true),
	DEEPSLATE_RUBY_ORE(0, Material.DEEPSLATE_DIAMOND_ORE, "Deepslate Ruby Ore", true),
	RUBY_BLOCK(0, Material.DIAMOND_BLOCK, "Ruby Block", true),

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
	// Level 1
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
	// Level 2 - 5
	
	
	// Level 5 - 15
	// Level 15 - 30
	// Level 30+
	;
	
	private final String localization;
	
	private final String name;
	private final ItemStack item;
	private final int levelUnlocked;

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
	
	private SMPMaterial(int level, Material original, String name, boolean block) {
		this.levelUnlocked = level;
		this.localization = name.toLowerCase().replace(" ", "_");
		
		ItemStack item = new ItemStack(original);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + name);
		meta.setLocalizedName(name.toLowerCase().replace(" ", "_"));
		item.setItemMeta(meta);
		this.item = item;

		this.name = name;
	}

	private SMPMaterial(int level, Material original, String name) {
		this(level, original, name, false);
	}

	private SMPMaterial(int level, Material original, String name, Map<Attribute, AttributeModifier> modifiers, Map<Enchantment, Integer> enchants) {
		this.levelUnlocked = level;
		this.localization = name.toLowerCase().replace(" ", "_");
		
		ItemStack item = new ItemStack(original);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + name);
		meta.setLocalizedName(name.toLowerCase().replace(" ", "_"));
		if (modifiers != null)
			for (Attribute a : modifiers.keySet()) {
				meta.addAttributeModifier(a, modifiers.get(a));
			}
		if (enchants != null)
			for (Enchantment e : enchants.keySet()) {
				meta.addEnchant(e, enchants.get(e), true);
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

	public static final ItemStack getItem(String localization) {
		return getByLocalization(localization).getItem();
	}

	public static final SMPMaterial getByLocalization(String localization) {
		for (SMPMaterial m : values()) {
			if (m.localization.equals(localization)) return m;
		}

		return null;
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
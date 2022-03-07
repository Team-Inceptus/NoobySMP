package us.teaminceptus.noobysmp.materials;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * All SMP items with abilities
 * An "ability" counts as the item having a special reaction when it is used.
 */
public enum AbilityItem {
	
	INFINIBALL(0, Material.FIRE_CHARGE, "InfiniBall"),
	
	INFINITNT(3, Material.TNT, "InfiniTNT"),

	OCASSUS_BOW_2(7, Material.BOW, "Ocassus Bow 2"),

	OCASSUS_CROSSBOW(9, Material.CROSSBOW, "Ocassus Crossbow"),
	
	OCASSUS_TRIDENT(12, Material.TRIDENT, "Ocassus Trident"),

	EARTHQUAKE_WAND_1(14, Material.IRON_HOE, "Earthquake Wand I"),

	EARTHQUAKE_WAND_2(15, Material.DIAMOND_HOE, "Earthquake Wand II"),
	EARTHQUAKE_WAND_3(15, Material.DIAMOND_HOE, "Earthquake Wand III"),
	EARTHQUAKE_WAND_4(16, Material.DIAMOND_HOE, "Earthquake Wand IV"),

	EARTHQUAKE_WAND_5(16, Material.NETHERITE_HOE, "Earthquake Wand V"),
	EARTHQUAKE_WAND_6(16, Material.NETHERITE_HOE, "Earthquake Wand VI"),

	FIRE_WAND(20, Material.BLAZE_ROD, "Fire Wand", true),
	
	ENDERITE_WAND(22, Material.END_ROD, "Enderite Wand", true),
	// From Boss Drops
	// Drops will ALWAYS be 0
	BULLET_WAND(0, Material.STICK, "Bullet Wand"),

	OCASSUS_BOW_1(0, Material.BOW, "Ocassus Bow"),
	
	// Tags - Boss Drops
	SCROLL_THROWING(0, Material.MAP, "Scroll of Throwing"),
	// Tags - Craftables
	STRENGTH_MELIORATE(4, Material.RED_DYE, "Strength Meliorate"),
	
	SNOWY_ENRICHMENT(10, Material.SNOW, "Snowy Enrichment"),
	SWAMP_ENRICHMENT(10, Material.VINE, "Swamp Enrichment"),
	
	AQUATIC_ENRICHMENT(12, Material.HEART_OF_THE_SEA, "Aquatic Enrichment"),
	
	NETHER_ENRICHMENT(18, Material.MAGMA_BLOCK, "Nether Enrichment"),
	
	END_ENRICHMENT(24, Material.CHORUS_FRUIT, "End Enrichment"),
	
	TITAN_ENRICHMENT(39, Material.NETHERITE_INGOT, "Titan Enrichment")
	;
	
	private final int levelUnlocked;
	private final ItemStack item;
	private final String name;
	private final String localization;
	
	private final ChatColor cc;
	
	private AbilityItem(int levelUnlocked, Material mat, String name) {
		this(levelUnlocked, mat, name, false);
	}
	
	private AbilityItem(int levelUnlocked, Material mat, String name, boolean glint) {
		this(levelUnlocked, mat, name, glint, null);
	}
	
	private AbilityItem(int levelUnlocked, Material mat, String name, String[] lore) {
		this(levelUnlocked, mat, name, false, lore);
	}
	
	private AbilityItem(int levelUnlocked, Material mat, String name, boolean glint, String[] lore) {
		this.levelUnlocked = levelUnlocked;
		this.name = name;
		
		if (levelUnlocked < 5) this.cc = ChatColor.AQUA;
		else if (levelUnlocked >= 5 && levelUnlocked < 15) this.cc = ChatColor.GREEN;
		else if (levelUnlocked >= 15 && levelUnlocked < 35) this.cc = ChatColor.LIGHT_PURPLE;
		else if (levelUnlocked >= 35) this.cc = ChatColor.GOLD;
		else this.cc = ChatColor.AQUA;
		
		this.localization = name.toLowerCase().replace(' ', '_');
		
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.cc + name);
		meta.setLocalizedName(this.localization);
		if (lore != null) meta.setLore(Arrays.asList(lore));
		
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE);
		if (glint) {
			meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		
		item.setItemMeta(meta);
		this.item = item;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final String getLocalization() {
		return this.localization;
	}
	
	public final int getLevelUnlocked() {
		return this.levelUnlocked;
	}
	
	public final ItemStack getItem() {
		return this.item;
	}
	
	public static final AbilityItem getByItem(ItemStack item) {
		return getByLocalization(item.getItemMeta().getLocalizedName());
	}
	
	public static final AbilityItem getByLocalization(String localize) {
		for (AbilityItem aitem : values()) {
			if (aitem.localization.equals(localize)) return aitem;
		}
		
		return null;
	}
	
	public final String getDisplayName() {
		return this.cc + this.name;
	}

}

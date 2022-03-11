package us.teaminceptus.noobysmp.materials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import us.teaminceptus.noobysmp.util.Queryable;

/**
 * All SMP items with abilities
 * An "ability" counts as the item having a special reaction when it is used.
 */
public enum AbilityItem implements Queryable {
	
	@QueryDescription({"An item that can infinitely", "generate fireballs."})
	INFINIBALL(0, Material.FIRE_CHARGE, "InfiniBall"),
	
	@QueryDescription({"An item that can infinitely", "place TNT."})
	INFINITNT(3, Material.TNT, "InfiniTNT"),

	@QueryDescription({"The second tier of the Ocassus Bow"})
	OCASSUS_BOW_2(7, Material.BOW, "Ocassus Bow 2"),

	@QueryDescription({"The Crossbow version of the Ocassus Family"})
	OCASSUS_CROSSBOW(9, Material.CROSSBOW, "Ocassus Crossbow"),
	
	@QueryDescription({"The Trident version of the Trident Family"})
	OCASSUS_TRIDENT(12, Material.TRIDENT, "Ocassus Trident"),

	@QueryDescription({"A wand with many tiers that can", "create a shockwave around you."})
	EARTHQUAKE_WAND_1(14, Material.IRON_HOE, "Earthquake Wand I"),

	EARTHQUAKE_WAND_2(15, Material.DIAMOND_HOE, "Earthquake Wand II"),
	EARTHQUAKE_WAND_3(15, Material.DIAMOND_HOE, "Earthquake Wand III"),
	EARTHQUAKE_WAND_4(16, Material.DIAMOND_HOE, "Earthquake Wand IV"),

	EARTHQUAKE_WAND_5(16, Material.NETHERITE_HOE, "Earthquake Wand V"),
	EARTHQUAKE_WAND_6(16, Material.NETHERITE_HOE, "Earthquake Wand VI"),

	@QueryDescription({"A wand that shoots fire."})
	FIRE_WAND(20, Material.BLAZE_ROD, "Fire Wand", true),
	
	@QueryDescription({"A special wand that can kill", "enderman in a certain radius."})
	ENDERITE_WAND(22, Material.END_ROD, "Enderite Wand", true),
	// From Boss Drops
	// Drops will ALWAYS be 0
	@QueryDescription({"A wand that can shoot shulker bullets."})
	BULLET_WAND(0, Material.STICK, "Bullet Wand"),

	@QueryDescription({"The main part of the Ocassus Family. Can", "shoot enderman."})
	OCASSUS_BOW_1(0, Material.BOW, "Ocassus Bow"),
	
	// Tags - Boss Drops
	@QueryDescription({"A Scroll that allows Swords to be thrown", "when combined in an anvil."})
	SCROLL_THROWING(0, Material.PAPER, "Scroll of Throwing"),

	@QueryDescription({"An upgrade that allows shovels to break", "their main blocks quickly."})
	SHARP_MELIORATE(0, Material.FLINT, "Sharp Meliorate"),

	@QueryDescription({"A secret scroll that allows your chestplate", "to harness the power of dragons."})
	SCROLL_DRAGONS(0, Material.PAPER, "Scroll of Dragons"),

	@QueryDescription({"The first weapon made for the entity kings.", "Its power and damage is one of the best."})
	ARESCENT(0, Material.NETHERITE_SWORD, "Arescent"),

	// Tags - Craftables
	@QueryDescription({"An upgrade that increases weapon strength."})
	STRENGTH_MELIORATE(4, Material.RED_DYE, "Strength Meliorate"),

	@QueryDescription({"An upgrade that gives the attacker", "a state of stickiness."})
	STICKY_MELIORATE(5, Material.HONEY_BOTTLE, "Sticky Meliorate"),
	
	@QueryDescription({"An upgrade that gives weapons", "the poison touch."})
	POISON_MELIORATE(7, Material.POISONOUS_POTATO, "Poison Meliorate"),

	@QueryDescription({"An upgrade that can replant crops."})
	REPLENISH_MELIORATE(7, Material.POPPY, "Replenish Meliorate"),

	@QueryDescription({"An upgrade that can lets swords harness", "the wither."})
	WITHER_MELIORATE(9, Material.WITHER_ROSE, "Wither Meliorate"),
	
	@QueryDescription({"An upgrade that can make weapons have the", "abilities of snow."})
	SNOWY_ENRICHMENT(10, Material.SNOW, "Snowy Enrichment"),

	@QueryDescription({"An upgrade for the swampy ones out there."})
	SWAMP_ENRICHMENT(10, Material.VINE, "Swamp Enrichment"),
	
	@QueryDescription({"A scroll that can help knock down entire", "trees, big and small."})
	SCROLL_TIMBERING(12, Material.PAPER, "Scroll of Timbering"),

	@QueryDescription({"An enrichment that harness the power of the", "sea."})
	AQUATIC_ENRICHMENT(12, Material.HEART_OF_THE_SEA, "Aquatic Enrichment"),
	
	SCROLL_SATURATION(15, Material.PAPER, "Scroll of Saturation"),

	SCROLL_MULTIBREAK(17, Material.PAPER, "Scroll of MultiBreaking"),
	SCROLL_ELECTRIC(17, Material.PAPER, "Scroll of Electricity"),

	NETHER_ENRICHMENT(18, Material.MAGMA_BLOCK, "Nether Enrichment"),
	
	SOAKING_MELIORATE(21, Material.SPONGE, "Soaking Meliorate"),
	SLIMY_MELIORATE(21, Material.SLIME_BALL, "Slimy Meliorate"),
	SCROLL_EXPLOSION(21, Material.PAPER, "Scroll of Explosions"),

	BUOYANT_ENRICHMENT(23, Material.PRISMARINE_SHARD, "Buoyant Enrichment"),

	END_ENRICHMENT(24, Material.CHORUS_FRUIT, "End Enrichment"),
	
	SCROLL_HARDENING(30, Material.PAPER, "Scroll of Hardening"),

	TITAN_ENRICHMENT(39, Material.NETHERITE_INGOT, "Titan Enrichment"),
	;
	
	{
		Queryable.register(this);
	}

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

	@Override
	public QueryID queryId() {
		return new QueryID("smpability", name().toLowerCase());
	}

	public ItemStack genInfo() {
		ItemStack info = Queryable.super.genInfo();
		ItemMeta meta = info.getItemMeta();
		List<String> lore = new ArrayList<>(meta.getLore());
		if (SMPTag.getByOrigin(this) != null) {
			SMPTag<?> tag = SMPTag.getByOrigin(this);
			lore.add(" ");
			lore.add(ChatColor.YELLOW + "Attached Tag: " + ChatColor.GOLD + tag.getName());
			lore.add(ChatColor.YELLOW + "Applies To: " + ChatColor.GOLD + tag.getTarget().name().substring(0, 1) + tag.getTarget().name().toLowerCase().substring(1));
		}
		info.setItemMeta(meta);
		return info;
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

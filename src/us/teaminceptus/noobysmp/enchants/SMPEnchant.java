package us.teaminceptus.noobysmp.enchants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;

import us.teaminceptus.noobysmp.SMP;

public class SMPEnchant extends Enchantment {
	
	private static final Material[] getBySuffix(String prefix) {
		return  Arrays.asList(Material.values()).stream().filter(m -> m.name().endsWith(prefix.toUpperCase()) && !(m.name().startsWith("LEGACY"))).toList().toArray(new Material[] {});
	}
	
	private static final Material[] combineLists(Material[]... mats) {
		List<Material> matsL = new ArrayList<>();
		
		for (Material[] mA : mats) 
			for (Material m : mA) 
				matsL.add(m);
		
		return matsL.toArray(new Material[] {});
	}
	
	public static final Material[] SWORDS = getBySuffix("sword");
	public static final Material[] PICKAXES = getBySuffix("pickaxe");
	public static final Material[] AXES = getBySuffix("axe");
	public static final Material[] BOWS = getBySuffix("bow");
	public static final Material[] SHOVELS = getBySuffix("shovel");
	public static final Material[] HOES = getBySuffix("hoe");
	
	public static final Material[] HELMETS = getBySuffix("helmet");
	public static final Material[] CHESTPLATES = getBySuffix("chestplate");
	public static final Material[] LEGGINGS = getBySuffix("leggings");
	public static final Material[] BOOTS = getBySuffix("boots");
	public static final Material[] TRIDENT = new Material[] {Material.TRIDENT};
	
	public static final Material[] WEAPONS = combineLists(SWORDS, AXES, TRIDENT);
	public static final Material[] ARMOR = combineLists(HELMETS, CHESTPLATES, LEGGINGS, BOOTS);
	public static final Material[] TOOLS = combineLists(WEAPONS, SHOVELS, HOES, PICKAXES);
	public static final Material[] NO_SWORD_TOOLS = combineLists(AXES, SHOVELS, HOES, PICKAXES);
	
	/* Notes:
	 * 
	 * - Only 1 side of an enchant is required to have conflict between 2, since having both 
	 * on both sides would be an error because one is not declared yet.
	 * - Remember to add to values() so it can actually be used
	 */
	
	public static final SMPEnchant POISON = new SMPEnchant(0, 3, "Poison", WEAPONS);
	public static final SMPEnchant WITHERING = new SMPEnchant(0, 3, "Withering", WEAPONS);
	public static final SMPEnchant POTION_PROTECTION = new SMPEnchant(0, 4, "Potion Protection", ARMOR);
	
	public static final SMPEnchant ACCURACY = new SMPEnchant(1, "Accuracy", BOWS);
	public static final SMPEnchant SMELTING = new SMPEnchant(1, "Smelting", PICKAXES);
	
	public static final SMPEnchant VEIN_MINER = new SMPEnchant(2, "Vein Miner", PICKAXES, SMPEnchant.SMELTING);
	public static final SMPEnchant TIMBER = new SMPEnchant(2, "Timber", AXES);
	
	public static final SMPEnchant VELOCITY = new SMPEnchant(3, 6, "Velocity", BOWS);
	public static final SMPEnchant CLEAVE = new SMPEnchant(3, 4, "Cleave", SWORDS);
	
	public static final SMPEnchant STUN = new SMPEnchant(8, 3, "Stun", WEAPONS);
	
	public static final SMPEnchant THUNDERING = new SMPEnchant(9, 5, "Thundering", WEAPONS);
	
	public static final SMPEnchant EXPLOSION = new SMPEnchant(11, "Explosion", WEAPONS);
	
	public static final SMPEnchant DOUBLE_DAMAGE = new SMPEnchant(25, "Double Damage", WEAPONS);
	
	private final int levelUnlocked;
	private final int maxLevel;
	private final String name;
	private final boolean treasure;
	
	private final String id;
	
	private final SMPEnchant[] conflicts;
	private final Material[] targets;
	
	private final ChatColor cc;
	
	public static final SMPEnchant[] values() {
		return new SMPEnchant[] {
			POISON,
			WITHERING,
			POTION_PROTECTION,
			ACCURACY,
			SMELTING,
			VEIN_MINER,
			TIMBER,
			VELOCITY,
			CLEAVE,
			STUN,
			THUNDERING,
			EXPLOSION,
			DOUBLE_DAMAGE
		};
	}
	
	public SMPEnchant(int levelUnlocked, String name, Material[] targets) {
		this(levelUnlocked, name, targets, new SMPEnchant[] {});
	}
	
	public SMPEnchant(int levelUnlocked, String name, Material[] targets, SMPEnchant... conflicts) {
		this(levelUnlocked, name, targets, conflicts, false);
	}
	
	public SMPEnchant(int levelUnlocked, String name, Material[] targets, SMPEnchant[] conflicts, boolean treasure) {
		super(new NamespacedKey(JavaPlugin.getPlugin(SMP.class), name.toLowerCase().replace(' ', '_')));
		this.maxLevel = 1;
		this.name = name;
		this.id = name.toLowerCase().replace(' ', '_');
		this.treasure = treasure;
		this.levelUnlocked = levelUnlocked;
		this.conflicts = conflicts;
		this.targets = targets;
		
		if (levelUnlocked < 5) this.cc = ChatColor.WHITE;
		else if (levelUnlocked >= 5 && levelUnlocked < 10) this.cc = ChatColor.AQUA;
		else if (levelUnlocked >= 10 && levelUnlocked < 25) this.cc = ChatColor.LIGHT_PURPLE;
		else if (levelUnlocked >= 25) this.cc = ChatColor.GOLD;
		else this.cc = ChatColor.WHITE;
	}
	
	public SMPEnchant(int levelUnlocked, int maxLevel, String name, Material[] targets) {
		this(levelUnlocked, maxLevel, name, targets, new SMPEnchant[] {});
	}
	
	public SMPEnchant(int levelUnlocked, int maxLevel, String name, Material[] targets, SMPEnchant... conflicts) {
		this(levelUnlocked, maxLevel, name, targets, conflicts, false);
	}
	
	public SMPEnchant(int levelUnlocked, int maxLevel, String name, Material[] targets, SMPEnchant[] conflicts, boolean treasure) {
		super(new NamespacedKey(JavaPlugin.getPlugin(SMP.class), name.toLowerCase().replace(' ', '_')));
		this.maxLevel = maxLevel;
		this.name = name;
		this.id = name.toLowerCase().replace(' ', '_');
		this.treasure = treasure;
		this.levelUnlocked = levelUnlocked;
		this.conflicts = conflicts;
		this.targets = targets;
		
		if (levelUnlocked < 5) this.cc = ChatColor.WHITE;
		else if (levelUnlocked >= 5 && levelUnlocked < 10) this.cc = ChatColor.AQUA;
		else if (levelUnlocked >= 10 && levelUnlocked < 25) this.cc = ChatColor.LIGHT_PURPLE;
		else if (levelUnlocked >= 25) this.cc = ChatColor.GOLD;
		else this.cc = ChatColor.WHITE;
	}
	
	public final String getDisplayName() {
		return cc + name;
	}
	
	public final int getLevelUnlocked() {
		return this.levelUnlocked;
	}
	
	public String getId() {
		return this.id;
	}
	
	public static SMPEnchant getById(String id) {
		for (SMPEnchant enchant : values()) {
			if (enchant.id.equals(id)) return enchant;
		}
		
		return null;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getMaxLevel() {
		return this.maxLevel;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}
	
	/**
	 * Method to satisfy Bukkit API.
	 * @deprecated Do not use to get valid targets, will only return {@link EnchantmentTarget#BREAKABLE}
	 * @see SMPEnchant#getTargets
	 * @return EnchantmentTarget.BREAKABLE
	 */
	@Deprecated
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.BREAKABLE;
	}

	@Override
	public boolean isTreasure() {
		return this.treasure;
	}
	
	/**
	 * Use this in favor of {@link SMPEnchant#getItemTarget()}.
	 * @return Array of valid Materials for this Enchantment
	 */
	public Material[] getTargets() {
		return this.targets;
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		if (!(other instanceof SMPEnchant ench)) return false;
		
		List<String> names = new ArrayList<>();
		for (SMPEnchant enchant : this.conflicts) names.add(enchant.getName());
		
		return names.contains(ench.name) && !(ench.conflictsWith(this));
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
		try {
			Damageable dmg = ((Damageable) item.getItemMeta());
			dmg.getClass();
			return true;
		} catch (ClassCastException e) {
			return false;
		}
	}

}

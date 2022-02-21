package us.teaminceptus.noobysmp.ability;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import us.teaminceptus.noobysmp.enchants.SMPEnchant;
import us.teaminceptus.noobysmp.materials.AbilityItem;

/**
 * Enum used for abilities from items and some from enchants.
 * Some abilities for enchants are not included here (i.e. Protection) (they will have the same name as {@link SMPEnchant} or {@link AbilityItem}
 */
public enum SMPAbility {
	
	// Abilities for Regular Items
	INFINIBALL("InfiniBall", RIGHT_CLICK(), AbilityItem.INFINIBALL, p -> {
		Fireball fb = p.getWorld().spawn(p.getLocation(), Fireball.class);
		
		fb.setDirection(p.getLocation().getDirection());
		p.playSound(p, Sound.ITEM_FIRECHARGE_USE, 3F, 1F);
	}),
	
	
	
	// Enchants
	THUNDERING("Thundering", RIGHT_CLICK(), SMPEnchant.THUNDERING, (p, target) -> p.getWorld().strikeLightning(target.getLocation()))
	
	;
	
//	private static final Action[] combineLists(Action[]... mats) {
//		List<Action> matsL = new ArrayList<>();
//		
//		for (Action[] mA : mats) 
//			for (Action m : mA) 
//				matsL.add(m);
//		
//		return matsL.toArray(new Action[] {});
//	}
	
	private static final Action[] RIGHT_CLICK() { return new Action[] {Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK}; }
//	private static final Action[] LEFT_CLICK() { return new Action[] {Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK}; }
	// private static final Action[] CLICK() { return combineLists(LEFT_CLICK(), RIGHT_CLICK()); }
	
	private final String name;
	private final Action[] actions;
	
	private final AbilityItem item;
	private final SMPEnchant enchantment;
	
	private final Consumer<Player> output;
	private final BiConsumer<Player, LivingEntity> bioutput;
	
	private SMPAbility(String name, Action[] action, AbilityItem item, Consumer<Player> output) {
		this.name = name;
		this.actions = action;
		this.item = item;
		this.enchantment = null;
		this.output = output;
		this.bioutput = null;
	}
	
	private SMPAbility(String name, Action[] action, SMPEnchant enchantment, Consumer<Player> output) {
		this.name = name;
		this.actions = action;
		this.enchantment = enchantment;
		this.item = null;
		this.output = output;
		this.bioutput = null;
	}
	
	private SMPAbility(String name, Action[] action, AbilityItem item, BiConsumer<Player, LivingEntity> output) {
		this.name = name;
		this.actions = action;
		this.item = item;
		this.enchantment = null;
		this.output = null;
		this.bioutput = output;
	}
	
	private SMPAbility(String name, Action[] action, SMPEnchant enchantment, BiConsumer<Player, LivingEntity> output) {
		this.name = name;
		this.actions = action;
		this.enchantment = enchantment;
		this.item = null;
		this.output = null;
		this.bioutput = output;
	}
	
	public static final SMPAbility getByItem(AbilityItem item) {
		for (SMPAbility ability : values()) {
			if (ability.item == item) return ability;
		}
		
		return null;
	}
	
	public static final SMPAbility getByEnchant(SMPEnchant enchant) {
		for (SMPAbility ability : values()) {
			if (ability.enchantment == null) continue;
			if (ability.enchantment.getId().equals(enchant.getId())) return ability;
		}
		
		return null;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final Action[] getActions() {
		return this.actions;
	}
	
	public final boolean isAbility() {
		return this.item != null;
	}
	
	public final boolean isEnchant() {
		return !(isAbility());
	}
	
	/**
	 * Can be null if ability is from an enchant. Check {@link SMPAbility#isAbility()} before calling this.
	 * @return AbilityItem
	 */
	public final AbilityItem getItem() {
		return this.item;
	}
	
	/**
	 * Can be null if ability is for an item. Check {@link SMPAbility#isEnchant()} before calling this.
	 * @return
	 */
	public final SMPEnchant getEnchantment() {
		return this.enchantment;
	}
	
	/**
	 * Some events (i.e. damaging an entity) use a BiConsumer. This may be null.
	 * @return Consumer for abilities only needing one parameter
	 */
	public final Consumer<Player> getConsumer() {
		return this.output;
	}
	
	/**
	 * Some events (i.e. Poison Protection) use a Consumer. This may be null.
	 * @return BiConsumer for abilities needing two parameters
	 */
	public final BiConsumer<Player, LivingEntity> getBiConsumer() {
		return this.bioutput;
	}
	
	/**
	 * This will only work if Consumer is not null and BiConsumer is null
	 * @param p Player to use
	 */
	public void init(Player p) {
		output.accept(p);
	}
	
	/**
	 * This will only work if BiConsumer is not null and Consumer is null
	 * @param p Player to use
	 * @param target LivingEntity target
	 */
	public void init(Player p, LivingEntity target) {
		bioutput.accept(p, target);
	}
}

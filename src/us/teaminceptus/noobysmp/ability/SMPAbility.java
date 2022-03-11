package us.teaminceptus.noobysmp.ability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.util.PlayerConfig;

/**
 * Enum used for abilities from items and some from enchants.
 * Some abilities for enchants are not included here (i.e. Protection) (they will have the same name as {@link SMPEnchant} or {@link AbilityItem}
 */
public enum SMPAbility {
	
	// Abilities for Regular Items
	INFINIBALL("InfiniBall", RIGHT_CLICK(), 0, AbilityItem.INFINIBALL, p -> {
		Fireball fb = p.getWorld().spawn(p.getLocation(), Fireball.class);
		
		fb.setDirection(p.getLocation().getDirection());
		p.playSound(p, Sound.ITEM_FIRECHARGE_USE, 3F, 1F);
	}),
	
	BULLET_WAND("Bullet Wand", RIGHT_CLICK(), 40, AbilityItem.BULLET_WAND, p -> {
		ShulkerBullet b = p.getWorld().spawn(p.getLocation(), ShulkerBullet.class);

		Entity target = p.getNearbyEntities(15, 15, 15).stream().filter(e -> e instanceof LivingEntity en && !(e.getUniqueId().equals(p.getUniqueId()))).toList().get(0);
		if (target != null) b.setTarget(target);
		else b.setTarget(null);
	}),
	
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
	private final long cooldown;
	
	private final AbilityItem item;

	public static final Map<SMPAbility, List<UUID>> cooldowns = new HashMap<>();
	
	private final Consumer<Player> output;
	private final BiConsumer<Player, LivingEntity> bioutput;
	
	private SMPAbility(String name, Action[] action, long cooldown, AbilityItem item, Consumer<Player> output) {
		this.name = name;
		this.actions = action;
		this.item = item;
		this.output = output;
		this.cooldown = cooldown;
		this.bioutput = null;
	}
	
	private SMPAbility(String name, Action[] action, long cooldown, AbilityItem item, BiConsumer<Player, LivingEntity> output) {
		this.name = name;
		this.actions = action;
		this.item = item;
		this.output = null;
		this.bioutput = output;

		this.cooldown = cooldown;
	}

	static {
		for (SMPAbility a : values()) cooldowns.put(a, new ArrayList<>());
	}
	
	public static final SMPAbility getByItem(AbilityItem item) {
		for (SMPAbility ability : values()) {
			if (ability.item == item) return ability;
		}
		
		return null;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final Action[] getActions() {
		return this.actions;
	}
	
	public final AbilityItem getItem() {
		return this.item;
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
		if (cooldowns.get(this).contains(p.getUniqueId())) {
			new PlayerConfig(p).sendNotification(ChatColor.RED + "Please wait before using this again!");
			return;
		}
		output.accept(p);
		if (this.cooldown > 0) initCooldown(p);
	}
	
	/**
	 * This will only work if BiConsumer is not null and Consumer is null
	 * @param p Player to use
	 * @param target LivingEntity target
	 */
	public void init(Player p, LivingEntity target) {
		if (cooldowns.get(this).contains(p.getUniqueId())) {
			new PlayerConfig(p).sendNotification(ChatColor.RED + "Please wait before using this again!");
			return;
		}
		bioutput.accept(p, target);
		if (this.cooldown > 0) initCooldown(p);
	}

	public void initCooldown(Player p) {
		cooldowns.get(this).add(p.getUniqueId());
		SMPAbility a = this;
		new BukkitRunnable() {
			public void run() {
				cooldowns.get(a).remove(p.getUniqueId());
				new PlayerConfig(p).sendNotification(ChatColor.GREEN + "You can use " + ChatColor.GOLD + a.name + ChatColor.GREEN + " again!");
			}
		}.runTaskLater(JavaPlugin.getPlugin(SMP.class), this.cooldown);
	}
}

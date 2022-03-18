package us.teaminceptus.noobysmp.ability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Enderman;
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
	
	BULLET_WAND("Bullets", RIGHT_CLICK(), 40, AbilityItem.BULLET_WAND, p -> {
		ShulkerBullet b = p.getWorld().spawn(p.getLocation(), ShulkerBullet.class);

		Entity target = p.getNearbyEntities(15, 15, 15).stream().filter(e -> e instanceof LivingEntity en && !(e.getUniqueId().equals(p.getUniqueId()))).toList().get(0);
		if (target != null) b.setTarget(target);
		else {
			b.setTarget(null);
			b.teleport(b.getLocation().setDirection(p.getLocation().getDirection()));
		}
	}),

	EARTHQUAKE_WAND_1("Earthquake", RIGHT_CLICK(), 20, AbilityItem.EARTHQUAKE_WAND_1, p -> {
		for (Entity en : p.getNearbyEntities(3, 0, 3).stream().filter(e -> e instanceof LivingEntity && !(e.getUniqueId().equals(p.getUniqueId()))).toList()) 
		((LivingEntity) en).damage(r().nextInt(5) + 5);
	}),

	EARTHQUAKE_WAND_2("Earthquake", RIGHT_CLICK(), 20, AbilityItem.EARTHQUAKE_WAND_2, p -> {
		for (Entity en : p.getNearbyEntities(3, 0, 3).stream().filter(e -> e instanceof LivingEntity && !(e.getUniqueId().equals(p.getUniqueId()))).toList()) 
		((LivingEntity) en).damage(r().nextInt(10) + 5);
	}),

	EARTHQUAKE_WAND_3("Earthquake", RIGHT_CLICK(), 15, AbilityItem.EARTHQUAKE_WAND_3, p -> {
		for (Entity en : p.getNearbyEntities(4, 0, 4).stream().filter(e -> e instanceof LivingEntity && !(e.getUniqueId().equals(p.getUniqueId()))).toList()) 
		((LivingEntity) en).damage(r().nextInt(10) + 5);
	}),

	EARTHQUAKE_WAND_4("Earthquake", RIGHT_CLICK(), 20, AbilityItem.EARTHQUAKE_WAND_4, p -> {
		for (Entity en : p.getNearbyEntities(4, 0, 4).stream().filter(e -> e instanceof LivingEntity && !(e.getUniqueId().equals(p.getUniqueId()))).toList()) 
		((LivingEntity) en).damage(r().nextInt(10) + 10);
	}),

	EARTHQUAKE_WAND_5("Earthquake", RIGHT_CLICK(), 20, AbilityItem.EARTHQUAKE_WAND_5, p -> {
		for (Entity en : p.getNearbyEntities(4, 0, 4).stream().filter(e -> e instanceof LivingEntity && !(e.getUniqueId().equals(p.getUniqueId()))).toList()) 
		((LivingEntity) en).damage(r().nextInt(15) + 10);
	}),

	EARTHQUAKE_WAND_6("Earthquake", RIGHT_CLICK(), 20, AbilityItem.EARTHQUAKE_WAND_6, p -> {
		for (Entity en : p.getNearbyEntities(5, 0, 5).stream().filter(e -> e instanceof LivingEntity && !(e.getUniqueId().equals(p.getUniqueId()))).toList()) 
		((LivingEntity) en).damage(r().nextInt(15) + 15);
	}),

	FIRE_WAND("Fire Shoot", RIGHT_CLICK(), 20, AbilityItem.FIRE_WAND, p -> {
		p.playSound(p, Sound.ITEM_FIRECHARGE_USE, 3F, 1F);
		for (double i = 0.5; i < 5; i += 0.5) {
			Location target = p.getLocation().add(p.getLocation().getDirection().multiply(1 + i));
			
			p.getWorld().spawnParticle(Particle.FLAME, target, 2);
			p.getWorld().getNearbyEntities(target, 1, 1, 1).forEach(e -> e.setFireTicks(20 * (r().nextInt(10) + 2 + (int) Math.floor(new PlayerConfig(p).getLevel() / 3))));	
		}
	}),

	ENDERITE_WAND("Enderman Killer", LEFT_CLICK(), 0, AbilityItem.ENDERITE_WAND, p -> {
		double damage = r().nextDouble() * (new PlayerConfig(p).getLevel() + 10);
		double radius = new PlayerConfig(p).getLevel() < 25 ? 1 : 2;

		for (Entity enderman : p.getNearbyEntities(radius, 1, radius).stream().filter(en -> en instanceof Enderman).toList()) ((Enderman) enderman).damage(damage, p);
	}),

	;

	private static Random r() { return new Random(); };
	
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
	private static final Action[] LEFT_CLICK() { return new Action[] {Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK }; }
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

	public static String parseAction(Action[] action) {
		List<Action> actions = Arrays.asList(action);

		if (actions.contains(Action.RIGHT_CLICK_AIR) && actions.contains(Action.RIGHT_CLICK_BLOCK)) {
			return "Right Click";
		} else if (actions.contains(Action.LEFT_CLICK_AIR) && actions.contains(Action.LEFT_CLICK_BLOCK)) {
			return "Left Click";
		} else return "Unknown";
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

	public final long getCooldown() {
		return this.cooldown;
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

package us.teaminceptus.noobysmp.materials;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.entities.bosses.BossManager;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.events.PlayerTickEvent;

public class SMPTag<T extends Event> {
	
	private static Random r = new Random();
	
	public static enum TagTarget {
		
		SWORDS(getEndingIn("sword")),
		AXES(getEndingIn("axe")),
		SHOVELS(getEndingIn("shovel")),
		HOES(getEndingIn("hoe")),
		PICKAXES(getEndingIn("pickaxe")),
		
		HELMETS(getEndingIn("helmet")),
		CHESTPLATES(getEndingIn("chestplate")),
		LEGGINGS(getEndingIn("leggings")),
		BOOTS(getEndingIn("boots")),
		
		BOWS(Material.BOW, Material.CROSSBOW),
		WEAPONS(getEndingIn("sword"), getEndingIn("axe"), BOWS.getTargets()),
		MINING_TOOLS(getEndingIn("axe"), getEndingIn("shovel"), getEndingIn("hoe"), getEndingIn("pickaxe")),
		TOOLS(getEndingIn("axe"), getEndingIn("shovel"), getEndingIn("hoe"), getEndingIn("pickaxe"), getEndingIn("sword")),
		
		ARMOR(getEndingIn("helmet"), getEndingIn("chestplate"), getEndingIn("leggings"), getEndingIn("boots"))
		;
		
		@SafeVarargs
		private <U> List<U> combineLists(List<U>... lists) {
			List<U> list = new ArrayList<>();
			
			for (List<U> l : lists) {
				list.addAll(l);
			}
			
			return list;
		}
		
		public static List<Material> getEndingIn(String name) {
			List<Material> list = new ArrayList<>();
			
			for (Material m : Material.values()) if (m.name().endsWith(name.toUpperCase())) list.add(m);
			
			return list;
		}
		
		private final List<Material> targets;
		
		private TagTarget(List<Material> targets) {
			this.targets = targets;
		}
		
		@SafeVarargs
		private TagTarget(List<Material>... targets) {
			this.targets = combineLists(targets);
		}

		private TagTarget(Material... targets) {
			this.targets = Arrays.asList(targets);
		}

		public boolean matches(ItemStack stack) {
			return matches(stack.getType());
		}

		public boolean matches(Material m) {
			return targets.contains(m);
		}
		
		public List<Material> getTargets() {
			return this.targets;
		}
		
	}
	
	private static final Class<PlayerInteractEvent> inter = PlayerInteractEvent.class;
	private static final Class<EntityDamageByEntityEvent> damage = EntityDamageByEntityEvent.class;
	private static final Class<PlayerTickEvent> tick = PlayerTickEvent.class;
	private static final Class<BlockBreakEvent> bbreak = BlockBreakEvent.class;
	
	// Scrolls
	
	public static final SMPTag<PlayerInteractEvent> THROWABLE = new SMPTag<>(inter, "Throwable", AbilityItem.SCROLL_THROWING, TagTarget.WEAPONS, e -> {
		if (e.getAction() != Action.RIGHT_CLICK_AIR) return;
		Player p = e.getPlayer();
		PlayerConfig config = new PlayerConfig(p);
		ItemStack target = e.getItem();
		
		new BukkitRunnable() {
			public void run() {
				double damage = 0;
				
				if (target.hasItemMeta() && target.getItemMeta().hasAttributeModifiers()) {
					for (AttributeModifier m : target.getItemMeta().getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE)) damage += m.getAmount();
				}
				
				BossManager.throwItem(p.getEyeLocation(), r.nextInt(5) + 7, target, r.nextDouble() * (damage + config.getLevel()));
			}
		}.runTask(JavaPlugin.getPlugin(SMP.class));
	});

	public static final SMPTag<EntityDamageByEntityEvent> ELECTRIC = new SMPTag<>(damage, "Electric", AbilityItem.SCROLL_ELECTRIC, TagTarget.SWORDS, e -> {
		if (!(e.getDamager() instanceof Player p)) return;

		if (r.nextInt(100) < 25) p.getWorld().strikeLightning(e.getEntity().getLocation());
	});

	public static final SMPTag<EntityShootBowEvent> EXPLOSIVE = new SMPTag<>(EntityShootBowEvent.class, "Explosive", AbilityItem.SCROLL_EXPLOSION, TagTarget.BOWS, e -> {
		Player p = (Player) e.getEntity();

		new BukkitRunnable() {
			public void run() {
				if (e.getProjectile().isDead()) cancel();

				e.getProjectile().getWorld().createExplosion(e.getProjectile().getLocation(), 3F, false, false, p);
			}
		}.runTaskTimer(JavaPlugin.getPlugin(SMP.class), 10, 10);
	});

	public static final SMPTag<EntityDamageByEntityEvent> HARDEN = new SMPTag<>(damage, "Harden", AbilityItem.SCROLL_HARDENING, TagTarget.CHESTPLATES, e -> {
		if (!(e.getEntity() instanceof Player p)) return;
		
		if (r.nextInt(100) < 10) {
			e.setCancelled(true);
			p.playSound(p, Sound.ITEM_SHIELD_BLOCK, 3F, 1F);
			new PlayerConfig(p).sendNotification(ChatColor.GREEN + "Your " + ChatColor.GOLD + "Scroll of Hardening" + ChatColor.GREEN + " has cancelled the damage!");
			if (e.getDamager() instanceof Player damager) {
				new PlayerConfig(damager).sendNotification(ChatColor.RED + "The target's " + ChatColor.GOLD + "Scroll of Hardening" + ChatColor.GREEN + " has cancelled the damage.");
			}
		}
	});
	
	private static final Material[] SHARP_INSTABREAK = {
		Material.GRAVEL, Material.SAND, Material.DIRT, Material.GRASS_BLOCK, Material.COARSE_DIRT,
		Material.PODZOL, Material.MYCELIUM
	};

	// Meliorates
	public static final SMPTag<BlockDamageEvent> SHARP = new SMPTag<>(BlockDamageEvent.class, "Sharp", AbilityItem.SHARP_MELIORATE, TagTarget.SHOVELS, e -> {
		Block b = e.getBlock();
		
		if (Arrays.asList(SHARP_INSTABREAK).contains(b.getType())) {
			e.setInstaBreak(true);
		}	
	});
	
	public static final SMPTag<BlockBreakEvent> REPLENISHING = new SMPTag<>(bbreak, "Replenishing", AbilityItem.REPLENISH_MELIORATE, TagTarget.HOES, e -> {
		Block b = e.getBlock();
		if (b.getType() == Material.FIRE) return;
		if (!(b.getBlockData() instanceof Ageable a)) return;
		
		Ageable clone = (Ageable) a.clone();
		clone.setAge(0);

		b.setBlockData(clone);
	});

	public static final SMPTag<PlayerTickEvent> SATURATION = new SMPTag<>(tick, "Saturating", AbilityItem.SCROLL_SATURATION, TagTarget.CHESTPLATES, e -> {
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2, 0));
	});

	

	public static final SMPTag<EntityDamageByEntityEvent> STRONG = new SMPTag<>(damage, "Strong", AbilityItem.STRENGTH_MELIORATE, TagTarget.WEAPONS, e -> {
		if (!(e.getDamager() instanceof Player p)) return;
		e.setDamage(e.getDamage() * ((r.nextDouble() / 2) + 1));
	});

	public static final SMPTag<EntityDamageByEntityEvent> POISON = new SMPTag<>(damage, "Poisoned", AbilityItem.POISON_MELIORATE, TagTarget.WEAPONS, e -> {
		if (!(e.getEntity() instanceof LivingEntity en)) return;
		en.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * (r.nextInt(10) + 5), (r.nextInt(2) + 1)));
	});

	public static final SMPTag<EntityDamageByEntityEvent> WITHERING = new SMPTag<>(damage, "Withering", AbilityItem.WITHER_MELIORATE, TagTarget.SWORDS, e -> {
		if (!(e.getEntity() instanceof LivingEntity en)) return;
		en.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * (r.nextInt(10) + 5), (r.nextInt(3) + 1)));
	});
	
	public static final SMPTag<EntityDamageByEntityEvent> STICKY = new SMPTag<>(damage, "Sticky", AbilityItem.STICKY_MELIORATE, TagTarget.WEAPONS, e -> {
		if (!(e.getDamager() instanceof Player p)) return;
		if (!(e.getEntity() instanceof LivingEntity en)) return;

		en.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * (r.nextInt(5) + 5), 1, false));
		en.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * (r.nextInt(5) + 5), 1, false));
	});

	public static final SMPTag<EntityDamageEvent> SLIMY = new SMPTag<>(EntityDamageEvent.class, "Slimy", AbilityItem.SLIMY_MELIORATE, TagTarget.BOOTS, e -> {
		if (!(e.getCause() != DamageCause.FALL)) return;
		Player p = (Player) e.getEntity();
		e.setCancelled(true);
		p.setVelocity(p.getVelocity().setY(Math.min(p.getFallDistance() / 10, 2)));
	});

	public static final SMPTag<PlayerTickEvent> SOAKING = new SMPTag<>(tick, "Soaking", AbilityItem.SOAKING_MELIORATE, TagTarget.HELMETS, e -> {
		Player p = e.getPlayer();
		
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				for (int z = 0; z < 3; z++) {
					Location target = p.getLocation().add(x, y, z);
					if (p.getWorld().getBlockAt(target).isLiquid()) {
						target.getBlock().setType(Material.AIR);
					}
					
					Location target2 = p.getLocation().add(-x, -y, -z);
					if (p.getWorld().getBlockAt(target2).isLiquid()) {
						target2.getBlock().setType(Material.AIR);
					}
				}
			}
		}
	});

	// Enrichments

	public static final SMPTag<PlayerTickEvent> BUOYANT = new SMPTag<>(tick, "Buoyant", AbilityItem.BUOYANT_ENRICHMENT, TagTarget.LEGGINGS, e -> {
		Player p = e.getPlayer();
		if (!(p.getEyeLocation().getBlock().isLiquid())) return;

		p.setVelocity(p.getVelocity().setY(0.1));
	});

	public static final SMPTag<EntityDamageByEntityEvent> COLD = new SMPTag<>(damage, "Cold", AbilityItem.SNOWY_ENRICHMENT, TagTarget.WEAPONS, e -> {
		if (!(e.getDamager() instanceof Player p)) return;
		Entity en = e.getEntity();
		en.setFreezeTicks(en.getFreezeTicks() + (20 * (r.nextInt(3) + 1)));
	});
	
	public static final SMPTag<PlayerTickEvent> AQUATIC = new SMPTag<>(tick, "Aquatic", AbilityItem.AQUATIC_ENRICHMENT, TagTarget.ARMOR, e -> {
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 2, 1, true));
	});
	
	public static final SMPTag<EntityDamageByEntityEvent> HOT = new SMPTag<>(damage, "Hot", AbilityItem.NETHER_ENRICHMENT, TagTarget.SWORDS, e -> {
		if (!(e.getDamager() instanceof Player p)) return;
		Entity en = e.getEntity();
		en.setFireTicks(en.getFireTicks() + (20 * (r.nextInt(2) + 1)));
	});

	private static final BlockFace[] DOWN = new BlockFace[] {
		BlockFace.NORTH, BlockFace.SOUTH, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST,
		BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.EAST, BlockFace.WEST 
	};

	public static final SMPTag<BlockBreakEvent> MULTIBREAK = new SMPTag<>(bbreak, "MultiBreaking", AbilityItem.SCROLL_MULTIBREAK, TagTarget.PICKAXES, e -> {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		ItemStack tool = p.getInventory().getItemInMainHand();
		float pitch = Math.abs(p.getLocation().getPitch());
		if (pitch >= 45) {
			// Use Down
			for (BlockFace f : DOWN) b.getRelative(f).breakNaturally(tool);
		} else {
			// Use Side, Calculate math
			for (BlockFace f : Arrays.asList(BlockFace.UP, BlockFace.DOWN)) b.getRelative(f).breakNaturally(tool);
			float yaw = (p.getLocation().getYaw() < 0 ? p.getLocation().getYaw() + 360 : p.getLocation().getYaw());

			if ((yaw >= 45 && yaw < 135) || (yaw >= 225 && yaw < 315)) {
				// Use North South
				b.getRelative(BlockFace.NORTH).breakNaturally(tool);
				b.getRelative(BlockFace.SOUTH).breakNaturally(tool);

				b.getRelative(0, 1, -1).breakNaturally(tool);
				b.getRelative(0, 1, 1).breakNaturally(tool);
				b.getRelative(0, -1, -1).breakNaturally(tool);
				b.getRelative(0, -1, 1).breakNaturally(tool);
			} else {
				// Use East West
				b.getRelative(BlockFace.EAST).breakNaturally(tool);
				b.getRelative(BlockFace.WEST).breakNaturally(tool);

				b.getRelative(-1, 1, 0).breakNaturally(tool);
				b.getRelative(1, 1, 0).breakNaturally(tool);
				b.getRelative(-1, -1, 0).breakNaturally(tool);
				b.getRelative(1, -1, 0).breakNaturally(tool);
			}
		}
	});

	public static final SMPTag<PlayerInteractEvent> ENDER = new SMPTag<>(inter, "Ender", AbilityItem.END_ENRICHMENT, TagTarget.SWORDS, e -> {
		if (e.getAction() != Action.RIGHT_CLICK_AIR) return;
		
		Player p = e.getPlayer();
		
		Location target = p.getLocation();
		
		for (int i = 0; i < 8; i++) {
			target.add(p.getLocation().getDirection());
			if (!(target.getBlock().isPassable())) {
				if (target.distance(p.getLocation()) < 2) {
					new PlayerConfig(p).sendNotification(ChatColor.RED + "That location is obstructed!");
				} else {
					p.teleport(target, TeleportCause.PLUGIN);
					p.playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, 3F, 1.5F);
				}
				return;
			}
		}
		
		p.teleport(target, TeleportCause.PLUGIN);
		p.playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, 3F, 1.5F);
	});
	
	
	public static List<SMPTag<?>> values() {
		List<SMPTag<?>> tags = new ArrayList<>();
	
		try {
			for (Field f : SMPTag.class.getDeclaredFields()) {
				try {
					f.getDeclaringClass().asSubclass(SMPTag.class);
					
					if (!(Modifier.isFinal(f.getModifiers()))) continue;
					if (!(Modifier.isStatic(f.getModifiers()))) continue;
					if (!(Modifier.isPublic(f.getModifiers()))) continue;
					
					tags.add((SMPTag<?>) f.get(null));
				} catch (ClassCastException err) {
					continue;
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
		
		return tags;
	}
	
	private final String name;
	private final Consumer<T> func;
	private final TagTarget target;
	private final AbilityItem origin;
	private final Class<T> clazz;
	
	private SMPTag(Class<T> clazz, String name, AbilityItem origin, TagTarget target, Consumer<T> func) {
		this.name = name;
		this.func = func;
		this.target = target;
		this.origin = origin;
		this.clazz = clazz;
	}

	public final String getName() {
		return this.name;
	}
	
	public final Class<? extends T> getTypeClass() {
		return this.clazz;
	}
	
	@SuppressWarnings("unchecked")
	public static final <U extends Event> List<SMPTag<U>> getByType(Class<U> type) {
		List<SMPTag<U>> list = new ArrayList<>();
		
		for (SMPTag<?> tag : values()) {
			if (tag.getTypeClass().equals(type)) list.add((SMPTag<U>) tag);
		}
		
		return list;
	}
	
	/**
	 * Will return Items.Tags.TOO_MANY_TAGS if tag length is bigger than 3
	 * @param tag Tag to use
	 * @param item Item to apply
	 * @return Newly Created Item
	 */
	public static ItemStack addTag(SMPTag<?> tag, ItemStack item) {
		SMP plugin = JavaPlugin.getPlugin(SMP.class);
		
		if (tagCount(item) >= 3) return Items.Tags.TOO_MANY_TAGS;
		
		ItemStack newItem = item.clone();
		ItemMeta meta = newItem.getItemMeta();
		meta.getPersistentDataContainer().set(new NamespacedKey(plugin, tag.name.toLowerCase()), PersistentDataType.STRING, "true");
		meta.setDisplayName(ChatColor.DARK_AQUA + tag.getName() + " " + ChatColor.RESET + (meta.hasDisplayName() ? meta.getDisplayName() : WordUtils.capitalizeFully(item.getType().name().replace('_', ' '))));
		newItem.setItemMeta(meta);
		
		return newItem;
	}
	
	public static final SMPTag<?> getByOrigin(AbilityItem origin) {
		
		for (SMPTag<?> tag : values()) if (tag.origin == origin) return tag;
		
		return null;
	}
	
	public final TagTarget getTarget() {
		return this.target;
	}
	
	/**
	 * The origin is the item that will be combined with the targeted item to create an ability
	 * @return AbilityItem that is used to create the item
	 */
	public final AbilityItem getOrigin() {
		return this.origin;
	}
	
	public static int tagCount(ItemStack item) {
		return getTags(item).size();
	}
	
	public static List<SMPTag<?>> getTags(ItemStack item) {
		List<SMPTag<?>> tags = new ArrayList<>();
		
		if (!(item.hasItemMeta())) return tags;
		
		for (SMPTag<?> tag : values()) {
			if (tag.hasTag(item)) tags.add(tag);
		}
		
		return tags;
	}
	
	public static boolean hasTag(SMPTag<?> tag, ItemStack item) {
		SMP plugin = JavaPlugin.getPlugin(SMP.class);
		if (item == null) return false;
		if (!(item.hasItemMeta())) return false;
		
		return item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, tag.name.toLowerCase()), PersistentDataType.STRING);	
	}
	
	public void run(T e) {
		this.func.accept(e);
	}
	
	public ItemStack addTag(ItemStack item) {
		return addTag(this, item);
	}
	
	public boolean hasTag(ItemStack item) {
		return hasTag(this, item);
	}
	
}

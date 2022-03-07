package us.teaminceptus.noobysmp.materials;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
		
		WEAPONS(getEndingIn("sword"), getEndingIn("axe")),
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
		
		public List<Material> getTargets() {
			return this.targets;
		}
		
	}
	
	private static final Class<PlayerInteractEvent> inter = PlayerInteractEvent.class;
	private static final Class<EntityDamageByEntityEvent> damage = EntityDamageByEntityEvent.class;
	private static final Class<PlayerTickEvent> tick = PlayerTickEvent.class;
	
	// Scrolls
	
	public static final SMPTag<PlayerInteractEvent> THROWABLE = new SMPTag<>(inter, "Throwable", AbilityItem.SCROLL_THROWING, TagTarget.WEAPONS, e -> {
		if (e.getAction() != Action.RIGHT_CLICK_AIR) return;
		Player p = e.getPlayer();
		PlayerConfig config = new PlayerConfig(p);
		ItemStack target = e.getItem();
		
		double damage = 0;
		
		if (target.hasItemMeta() && target.getItemMeta().hasAttributeModifiers()) {
			for (AttributeModifier m : target.getItemMeta().getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE)) damage += m.getAmount();
		}
		
		BossManager.throwItem(p.getEyeLocation(), r.nextInt(5) + 7, target, r.nextDouble() * (damage + config.getLevel()), p);
	});
	
	// Meliorates
	public static final SMPTag<EntityDamageByEntityEvent> STRONG = new SMPTag<>(damage, "Strong", AbilityItem.STRENGTH_MELIORATE, TagTarget.WEAPONS, e -> {
		e.setDamage(e.getDamage() * ((r.nextDouble() / 2) + 1));
	});
	
	
	// Enrichments
	
	public static final SMPTag<EntityDamageByEntityEvent> COLD = new SMPTag<>(damage, "Cold", AbilityItem.SNOWY_ENRICHMENT, TagTarget.WEAPONS, e -> {
		Entity en = e.getEntity();
		en.setFreezeTicks(en.getFreezeTicks() + (20 * (r.nextInt(3) + 1)));
	});
	
	public static final SMPTag<PlayerTickEvent> AQUATIC = new SMPTag<>(tick, "Aquatic", AbilityItem.AQUATIC_ENRICHMENT, TagTarget.ARMOR, e -> {
		e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 2, 1, true));
	});
	
	public static final SMPTag<EntityDamageByEntityEvent> HOT = new SMPTag<>(damage, "Hot", AbilityItem.NETHER_ENRICHMENT, TagTarget.SWORDS, e -> {
		Entity en = e.getEntity();
		en.setFireTicks(en.getFireTicks() + (20 * (r.nextInt(2) + 1)));
	});
	
	public static final SMPTag<PlayerInteractEvent> ENDER = new SMPTag<>(inter, "Ender", AbilityItem.END_ENRICHMENT, TagTarget.SWORDS, e -> {
		if (e.getAction() != Action.RIGHT_CLICK_AIR) return;
		
		Player p = e.getPlayer();
		
		Location target = p.getLocation();
		
		for (int i = 0; i < 8; i++) {
			target.add(p.getLocation().getDirection());
			if (!(target.getBlock().isPassable())) {
				p.sendMessage(ChatColor.RED + "That location is obstructed!");
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
	
	public final Class<? extends T> getTypeClass() {
		return this.clazz;
	}
	
	@SuppressWarnings("unchecked")
	public static final <U extends Event> List<SMPTag<U>> getByType(Class<U> type) {
		List<SMPTag<U>> list = new ArrayList<>();
		
		for (SMPTag<?> tag : values()) if (tag.getTypeClass().equals(type)) list.add((SMPTag<U>) tag);
		
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
		
		ItemStack newItem = item;
		ItemMeta meta = newItem.getItemMeta();
		meta.getPersistentDataContainer().set(new NamespacedKey(plugin, tag.name.toLowerCase()), PersistentDataType.STRING, "true");
		meta.setDisplayName(ChatColor.DARK_AQUA + " " + meta.getDisplayName());
		newItem.setItemMeta(meta);
		
		return newItem;
	}
	
	public static final List<SMPTag<?>> getByOrigin(AbilityItem origin) {
		List<SMPTag<?>> tags = new ArrayList<>();
		
		for (SMPTag<?> tag : values()) if (tag.origin == origin) tags.add(tag);
		
		return tags;
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
		if (!(item.hasItemMeta())) return false;
		
		return item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, tag.name.toLowerCase()), PersistentDataType.STRING);	
	}
	
	@SuppressWarnings("unchecked")
	public void run(Event e) {
		this.func.accept((T) e);
	}
	
	public ItemStack addTag(ItemStack item) {
		return addTag(this, item);
	}
	
	public boolean hasTag(ItemStack item) {
		return hasTag(this, item);
	}
	
}

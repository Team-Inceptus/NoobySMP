package us.teaminceptus.noobysmp.entities.bosses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftMob;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import us.teaminceptus.noobysmp.entities.SMPEntity;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;

public abstract class SMPBoss<T extends Mob> extends SMPEntity<T> {
	
	private Map<Integer, Goal> goals;
	private final Map<Attribute, Double> attributes;
	
	protected final Map<ItemStack, Integer> drops;
	
	public static List<UUID> bossList = new ArrayList<>();
	
	public static List<String> BOSS_NAME_LIST = new ArrayList<>();
	
	public final String NAMETAG = ChatColor.DARK_RED + "[" + this.customName + ChatColor.DARK_RED + "] " + ChatColor.RED;

	public static List<Class<? extends SMPBoss<? extends Mob>>> CLASS_LIST = ImmutableList.<Class<? extends SMPBoss<? extends Mob>>>builder()
	.add(SuperSniper.class)
	.add(NetheritePiglin.class)
	.add(RedstoneGolem.class)
	.add(AmethystSkeleton.class)
	.add(BlazeKing.class)
	.add(DrownedPrince.class)
	.add(EmeraldThief.class)
	.add(EnchantedEnderman.class)
	.add(Graphenefish.class)
	.build();

	public SMPBoss(Class<T> clazz, Location loc, double maxHealth, String name, Map<ItemStack, Integer> drops, Map<Attribute, Double> attributes) {
		this(clazz, loc, maxHealth, name, drops, attributes, null);
	}
	
	public SMPBoss(Class<T> clazz, Location loc, double maxHealth, String name, Map<ItemStack, Integer> drops, Map<Attribute, Double> attributes, Map<Integer, Goal> goals) {
		super(clazz, loc, maxHealth, name, drops.keySet().stream().toList());
		BOSS_NAME_LIST.add(getCustomName());
		this.goals = goals;
		this.attributes = attributes;
		this.drops = drops;
		
		Mob en = this.entity;
		bossList.add(en.getUniqueId());
		
		for (Attribute a : attributes.keySet()) {
			en.getAttribute(a).setBaseValue(attributes.get(a));
		}
		
		if (goals != null) {
			net.minecraft.world.entity.Mob mob = (net.minecraft.world.entity.Mob) ((CraftEntity) this.getEntity()).getHandle();
			mob.goalSelector.removeAllGoals();
			
			for (int i : goals.keySet()) {
				mob.goalSelector.addGoal(i, goals.get(i));
			}
		}
		
		if (this.getClass().isAnnotationPresent(DisplayName.class)) {
			DisplayName display = this.getClass().getAnnotation(DisplayName.class);
			
			this.entity.setCustomName(display.cc() + display.value());
		}
	}
	public static Class<? extends SMPBoss<?>> getByIcon(Material icon) {
		for (Class<? extends SMPBoss<?>> bossClass : CLASS_LIST) {
			if (icon == bossClass.getAnnotation(Icon.class).value()) return bossClass;
		}

		return null;
	}
	
	public Map<ItemStack, Integer> getBossDrops() {
		return this.drops;
	}

	public void setGoals(Map<Integer, Goal> goals) {
		this.goals = goals;

		net.minecraft.world.entity.Mob mob = (net.minecraft.world.entity.Mob) ((CraftEntity) this.getEntity()).getHandle();
		mob.goalSelector.removeAllGoals();
		
		for (int i : goals.keySet()) {
			mob.goalSelector.addGoal(i, goals.get(i));
		}	
	}

	public static Map<Attribute, Double> attributes(double maxHealth) {
		return ImmutableMap.<Attribute, Double>builder()
		.put(Attribute.GENERIC_ARMOR, Math.min(maxHealth / 200, 500))
		.put(Attribute.GENERIC_KNOCKBACK_RESISTANCE, maxHealth / 2000)
		.put(Attribute.GENERIC_ARMOR_TOUGHNESS, Math.min(maxHealth / 400, 400))
		.put(Attribute.GENERIC_MOVEMENT_SPEED, Math.min(maxHealth / 1000, 1))
		.put(Attribute.GENERIC_ATTACK_DAMAGE, Math.min(maxHealth / 300, 400))
		.build();
	}

	/**
	 * If no Attack Attribute & Attack Knockback attributes are present, this method will add them.
	 * @param entity Entity to use
	 * @return Goals for passing to constructor
	 */
	public static Map<Integer, Goal> attackGoal(Mob entity) {
		PathfinderMob mob = (PathfinderMob) ((CraftMob) entity).getHandle();

		mob.getAttributes().getDirtyAttributes().add(new AttributeInstance(Attributes.ATTACK_DAMAGE, a -> {a.setBaseValue(attributes(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()).get(Attribute.GENERIC_ATTACK_DAMAGE));}));
		mob.getAttributes().getDirtyAttributes().add(new AttributeInstance(Attributes.ATTACK_KNOCKBACK, a -> {a.setBaseValue(attributes(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()).get(Attribute.GENERIC_ATTACK_KNOCKBACK));}));
		Map<Integer, Goal> goals = new HashMap<>();
		goals.put(0, new FloatGoal(mob));
		goals.put(1, new RandomLookAroundGoal(mob));
		goals.put(2, new WaterAvoidingRandomStrollGoal(mob, WaterAvoidingRandomStrollGoal.PROBABILITY));
		goals.put(3, new LookAtPlayerGoal(mob, Player.class, LookAtPlayerGoal.DEFAULT_PROBABILITY));
		goals.put(4, new MoveTowardsTargetGoal(mob, 1, 1));
		goals.put(5, new MeleeAttackGoal(mob, 2, true));
		goals.put(6, new NearestAttackableTargetGoal<Player>(mob, Player.class, true));

		return goals;
	}
	
	public Map<Integer, Goal> getPathfinderGoals() {
		return this.goals;
	}
	
	public Map<Attribute, Double> getAttributeMap() {
		return this.attributes;
	}

	public static SMPBoss<?> getByUUID(UUID uid) {
		for (SMPEntity<?> entity : entityList) {
			if (!(entity instanceof SMPBoss<?> b)) continue;
			if (b.getUniqueId().toString().equals(uid.toString())) return b;
		}

		return null;
	}

	public static List<UUID> getBossList() {
		return bossList;
	}

}

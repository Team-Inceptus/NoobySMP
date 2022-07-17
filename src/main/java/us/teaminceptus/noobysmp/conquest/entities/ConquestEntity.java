package us.teaminceptus.noobysmp.conquest.entities;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import us.teaminceptus.noobysmp.entities.bosses.SMPBoss;

public abstract class ConquestEntity<T extends LivingEntity> {
	
	protected static final Random r = new Random();
	
	protected final T entity;
	
	public final List<ItemStack> drops;
	
	public static final List<Class<? extends ConquestEntity<?>>> CLASS_LIST = ImmutableList.<Class<? extends ConquestEntity<?>>>builder()
	.add(RubySheep.class)
	.add(DiamondZombie.class)
	.add(BlackstonePig.class)
	.add(QuartzCow.class)
	.add(EnderiteEnderman.class)
	.build();

	private static final EntityType[] NON_MELEE_ENTITIES = {
		EntityType.SHEEP,
		EntityType.COW,
		EntityType.PIG,
		EntityType.AXOLOTL,
		EntityType.FOX,
		EntityType.PANDA,
		EntityType.CHICKEN,
		EntityType.CAT,
		EntityType.OCELOT,
		EntityType.LLAMA,
		EntityType.BAT,
		EntityType.CREEPER,
		EntityType.HORSE,
		EntityType.DONKEY,
		EntityType.MULE,
		EntityType.SNOWMAN
	};

	public ConquestEntity(Location loc, Class<T> clazz, double health, List<ItemStack> drops) {
		World cWorld = Bukkit.getWorld("world_conquest");
		this.entity = cWorld.spawn(loc, clazz);
		
		this.drops = drops;

		Map<Attribute, Double> modifiers = SMPBoss.attributes(health);

		for (Attribute a : modifiers.keySet()) {
			this.entity.getAttribute(a).setBaseValue(modifiers.get(a));
		}
		
		this.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
		this.entity.setHealth(health);
		
		// Behavior
		PathfinderMob nmsEntity = (PathfinderMob) ((CraftEntity) entity).getHandle();
		
		nmsEntity.goalSelector.removeAllGoals();

		nmsEntity.goalSelector.addGoal(0, new FloatGoal(nmsEntity));
		if (!(Arrays.asList(NON_MELEE_ENTITIES)).contains(this.entity.getType())) {
			nmsEntity.goalSelector.addGoal(2, new HurtByTargetGoal(nmsEntity));
			nmsEntity.goalSelector.addGoal(1, new MeleeAttackGoal(nmsEntity, 1, true));
		}

		nmsEntity.goalSelector.addGoal(3, new AvoidEntityGoal<>(nmsEntity, Player.class, 5, 1, 1.5));
		nmsEntity.goalSelector.addGoal(4, new RandomStrollGoal(nmsEntity, 1));
		nmsEntity.goalSelector.addGoal(5, new RandomLookAroundGoal(nmsEntity));
	}
	
	public final T getEntity() {
		return this.entity;
	}

	public static Map<Attribute, Double> attributes(double health) {
		return SMPBoss.attributes(health);
	}
	
}

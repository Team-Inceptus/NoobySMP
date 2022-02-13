package us.teaminceptus.noobysmp.util.entities;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

public class ConquestEntity<T extends LivingEntity> {
	
	private static final Random r = new Random();
	
	private final T entity;
	
	public final List<ItemStack> drops;
	
	public ConquestEntity(Location loc, Class<T> clazz, double health, Map<Attribute, Integer> modifiers, List<ItemStack> drops) {
		World cWorld = Bukkit.getWorld("world_conquest");
		this.entity = cWorld.spawn(loc, clazz);
		
		this.drops = drops;
		
		for (Attribute a : modifiers.keySet()) {
			this.entity.getAttribute(a).setBaseValue(modifiers.get(a));
		}
		
		this.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
		this.entity.setHealth(health);
		
		// Behavior
		PathfinderMob nmsEntity = (PathfinderMob) ((CraftEntity) entity).getHandle();
		
		nmsEntity.goalSelector.removeAllGoals();
		nmsEntity.goalSelector.addGoal(0, new FloatGoal(nmsEntity));
		nmsEntity.goalSelector.addGoal(2, new HurtByTargetGoal(nmsEntity, new Class[0]));
		nmsEntity.goalSelector.addGoal(1, new MeleeAttackGoal(nmsEntity, 1, true));
		nmsEntity.goalSelector.addGoal(3, new AvoidEntityGoal<Player>(nmsEntity, Player.class, 5, 1, 1.5));
		nmsEntity.goalSelector.addGoal(4, new RandomStrollGoal(nmsEntity, 1));
		nmsEntity.goalSelector.addGoal(5, new RandomLookAroundGoal(nmsEntity));
	}
	
	public final T getEntity() {
		return this.entity;
	}
	
	public static ConquestEntity<Sheep> spawnRubySheep(Location loc) {
		return new ConquestEntity<Sheep>(loc, Sheep.class, 100D, null, Arrays.asList(SMPMaterial.RUBY.getItem(r.nextInt(3) + 1)));
	}
	
}

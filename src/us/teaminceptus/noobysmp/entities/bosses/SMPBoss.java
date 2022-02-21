package us.teaminceptus.noobysmp.entities.bosses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;

import net.minecraft.world.entity.ai.goal.Goal;
import us.teaminceptus.noobysmp.entities.SMPEntity;

public abstract class SMPBoss<T extends Mob> extends SMPEntity<T> {
	
	private final Map<Integer, Goal> goals;
	private final Map<Attribute, Double> attributes;
	
	public static List<String> BOSS_NAME_LIST = new ArrayList<>();
	
	public SMPBoss(Class<T> clazz, Location loc, double maxHealth, String name, List<ItemStack> drops, Map<Attribute, Double> attributes) {
		this(clazz, loc, maxHealth, name, drops, attributes, null);
	}
	
	public SMPBoss(Class<T> clazz, Location loc, double maxHealth, String name, List<ItemStack> drops, Map<Attribute, Double> attributes, Map<Integer, Goal> goals) {
		super(clazz, loc, maxHealth, name, drops);
		BOSS_NAME_LIST.add(getCustomName());
		this.goals = goals;
		this.attributes = attributes;
		
		Mob en = this.entity;
		
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
	}
	
	public Map<Integer, Goal> getPathfinderGoals() {
		return this.goals;
	}
	
	public Map<Attribute, Double> getAttributeMap() {
		return this.attributes;
	}

}

package us.teaminceptus.noobysmp.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableList;

public abstract class SMPEntity<T extends LivingEntity> {
	
	private static final List<SMPEntity<?>> entityList = new ArrayList<>(); 
	
	private final double maxHealth;
	private final String customName;
	private final List<ItemStack> drops;
	
	private final Class<T> clazz;
	
	// Make sure to update this as more are added!
	public static List<Class<? extends SMPEntity<?>>> CLASS_LIST = ImmutableList.<Class<? extends SMPEntity<?>>>builder()
			.add(JebSheep.class)
			.build();
	
	protected final static Random r = new Random();
	
	protected final T entity;
	
	public SMPEntity(Class<T> clazz, Location loc, double maxHealth, String customName, List<ItemStack> drops) {
		entityList.add(this);
		
		this.maxHealth = maxHealth;
		this.customName = customName;
		this.drops = drops;
		this.clazz = clazz;
		
		this.entity = loc.getWorld().spawn(loc, clazz);
		
		entity.setCustomName(customName);
		entity.setCustomNameVisible(true);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
	}
	
	public static SMPEntity<?> getFromList(LivingEntity en) {
		for (SMPEntity<?> ent : entityList) {
			if (ent.entity.getUniqueId().equals(en.getUniqueId())) return ent;
		}
		
		return null;
	}
	
	public Class<T> getEntityClass() {
		return this.clazz;
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof SMPEntity<?> ent)) return false;
		if (!(ent.getEntityClass().equals(this.getEntityClass()))) return false;
		
		try {
			return ((LivingEntity) ent.getEntity()).getUniqueId().equals(this.getEntity().getUniqueId());
		} catch (Exception e) {
			return false;
		}
	}
	
	public EntityType getType() {
		return entity.getType();
	}
	
	public List<ItemStack> getDrops() {
		return this.drops;
	}
	
	public T getEntity() {
		return this.entity;
	}
	
	public double getMaxHealth() {
		return this.maxHealth;
	}
	
	public String getCustomName() {
		return this.customName;
	}
	
	public void setMaxHealth(double hp) {
		this.entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
	}
	
	public void setCustomName(String name) {
		this.entity.setCustomName(name);
	}
	
	public void setCustomNameVisible(boolean visible) {
		this.entity.setCustomNameVisible(visible);
	}

	public static List<SMPEntity<?>> getEntityList() {
		return entityList;
	}

}

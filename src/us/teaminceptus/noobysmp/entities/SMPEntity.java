package us.teaminceptus.noobysmp.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.ImmutableList;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.entities.titan.AmberSkeleton;
import us.teaminceptus.noobysmp.entities.titan.JadeZombie;
import us.teaminceptus.noobysmp.entities.titan.QarditeWitch;
import us.teaminceptus.noobysmp.entities.titan.RubyPig;
import us.teaminceptus.noobysmp.entities.titan.Titanmen;
import us.teaminceptus.noobysmp.entities.titan.TopazChicken;

public abstract class SMPEntity<T extends LivingEntity> {
	
	protected static final List<SMPEntity<?>> entityList = new ArrayList<>(); 
	
	protected final double maxHealth;
	protected final String customName;
	protected final List<ItemStack> drops;
	
	protected SMP plugin;

	private final Class<T> clazz;
	
	// Make sure to update these as more are added!
	public static List<Class<? extends SMPEntity<?>>> CLASS_LIST = ImmutableList.<Class<? extends SMPEntity<?>>>builder()
			.add(JebSheep.class)
			.add(BlackstoneSkeleton.class)
			.add(RubySkeleton.class)
			.build();
	
	public static List<Class<? extends SMPEntity<?>>> TITAN_CLASS_LIST = ImmutableList.<Class<? extends SMPEntity<?>>>builder()
			.add(JadeZombie.class)
			.add(AmberSkeleton.class)
			.add(TopazChicken.class)
			.add(Titanmen.class)
			.add(RubyPig.class)
			.add(QarditeWitch.class)
			.build();
	
	protected final static Random r = new Random();
	
	protected final T entity;
	
	public SMPEntity(Class<T> clazz, Location loc, double maxHealth, String customName, List<ItemStack> drops) {
		entityList.add(this);
		
		this.maxHealth = maxHealth;
		this.customName = customName;
		this.drops = drops;
		this.clazz = clazz;
		this.plugin = JavaPlugin.getPlugin(SMP.class);
		
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

	public UUID getUniqueId() {
		return this.entity.getUniqueId();
	}

	public static SMPEntity<?> getByUUID(UUID uid) {
		for (SMPEntity<?> entity : entityList) {
			if (entity.getUniqueId().toString().equals(uid.toString())) return entity;
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

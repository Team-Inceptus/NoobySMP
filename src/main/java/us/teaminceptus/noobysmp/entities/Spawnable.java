package us.teaminceptus.noobysmp.entities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.entity.EntityType;

/**
 * Put this annotation above classes extending {@link SMPEntity} to allow to be naturally spawned.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Spawnable {
	
	EntityType type();
	
	int spawnChance();
}

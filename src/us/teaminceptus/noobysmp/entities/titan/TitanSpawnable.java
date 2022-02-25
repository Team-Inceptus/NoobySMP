package us.teaminceptus.noobysmp.entities.titan;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.entity.EntityType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TitanSpawnable {
    EntityType replace();
}

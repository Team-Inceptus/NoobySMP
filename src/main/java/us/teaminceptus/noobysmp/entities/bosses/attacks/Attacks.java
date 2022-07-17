package us.teaminceptus.noobysmp.entities.bosses.attacks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.entity.EntityType;

public interface Attacks {
    
    /**
     * Put this above a method to execute when the entity attacks (projectile or physical)
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Offensive {

        int chance() default 100;

    }

    /**
     * Put this above a method to execute when the entity is damaged.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Defensive {

        int chance() default 100;

    }

    /**
     * Put this above a method to repeat an attack.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Repeated {

        long value();

    }

    // Constructor

    /**
     * Put this one in a constructor to automatically spawn minions when damaged.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.CONSTRUCTOR)
    @Repeatable(MinionSpawns.class)
    @interface MinionSpawn {

        int chance();

        EntityType type();

    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.CONSTRUCTOR)
    @interface MinionSpawns {

        MinionSpawn[] value();

    }
    
    /**
     * Put over contructor to have a chance of blocking the attack.
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.CONSTRUCTOR)
    @interface CancelChance {

        int value();

    }

}

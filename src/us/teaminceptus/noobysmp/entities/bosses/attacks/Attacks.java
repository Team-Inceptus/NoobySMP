package us.teaminceptus.noobysmp.entities.bosses.attacks;

import java.lang.annotation.ElementType;
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
    public static @interface Offensive {

        int chance();

    }

    /**
     * Put this above a method to execute when the entity is damaged.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Defensive {

        int chance();

    }

    /**
     * Put this above a method to repeat an attack.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Repeated {

        long interval();

    }

    // Constructor

    /**
     * Put this one in a constructor to automatically spawn minions when damaged.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.CONSTRUCTOR)
    public static @interface MinionSpawn {

        int chance();

        EntityType type();

    }

}

package us.teaminceptus.noobysmp.entities.bosses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.ChatColor;
import org.bukkit.Material;

/**
 * Put these annotations above a class to generate an item in the Boss GUI.
 * <br></br>
 * Required:
 * - Tier
 * - Description
 * - Spawn Cost
 * - Display Name
 */
public interface BossSetup {

    /**
     * Use this one to not use this boss in the GUI.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface NotGeneratabele {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface DisplayName {
        String value();
        ChatColor cc();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface Tier {

        int value();

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface Description {

        String[] value();

    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface HP {

        double value();

    }

    /**
     * SpawnCost of the entity. <br></br>Separate with ID from count.<br></br> 
     * Example: <code>@SpawnCost({"bow:3", "emerald:2", "ruby_sword:1"})</code> 
     * 
    */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface SpawnCost {

        String[] value();

    }

    /**
     * <strong>Icons must be unique to the entity.</strong><br></br>
     * The class uses Materials to spawn the selected boss.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface Icon {

        Material value();

    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.CONSTRUCTOR)
    public static @interface Experience {

        int value() default 5;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Repeatable(Drops.class)
    public static @interface Drop {

        String drop();
        String amount() default "1";
        int chance() default 100;

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface Drops {
        Drop[] value();
    }
    
    
}

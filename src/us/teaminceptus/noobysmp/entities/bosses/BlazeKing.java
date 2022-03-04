package us.teaminceptus.noobysmp.entities.bosses;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.ability.cosmetics.SMPCosmetic;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Description;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Experience;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.HP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.SpawnCost;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Tier;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Defensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.MinionSpawn;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Repeated;

@Tier(2)
@Description({"The king is back! He's here", "with a fiery purpose."})
@SpawnCost("blaze_rod:32")
@HP(10000)
@Icon(Material.BLAZE_POWDER)
@DisplayName(value = "Blaze King", cc = ChatColor.GOLD)
public class BlazeKing extends SMPBoss<Blaze> {
    
    @Experience(100)
    @MinionSpawn(type = EntityType.BLAZE, chance = 75)
    public BlazeKing(Location loc) {
        super(Blaze.class, loc, 10000, "Blaze King",
        ImmutableMap.<ItemStack, Integer>builder()
        
        .build(), attributes(10000));
    }

    @Repeated(1)
    public void drawRing() {
        if (entity.getHealth() > 3000)
        SMPCosmetic.createCircle(Particle.FLAME, 2).accept(entity.getLocation().add(0, 0.5, 0));
        else SMPCosmetic.createCircle(Particle.SOUL_FIRE_FLAME, 2).accept(entity.getLocation().add(0, 0.5, 0));
    }

    @Repeated(10)
    public void superfireball() {
        if (!(entity.getHealth() <= 3000)) return;

        Location loc = entity.getEyeLocation();

        SmallFireball f1 = entity.getWorld().spawn(loc, SmallFireball.class);
        f1.setRotation(loc.getYaw(), loc.getPitch());

        SmallFireball f2 = entity.getWorld().spawn(loc, SmallFireball.class);
        f2.setRotation(loc.getYaw() + 90, loc.getPitch());

        SmallFireball f3 = entity.getWorld().spawn(loc, SmallFireball.class);
        f3.setRotation(loc.getYaw() + 180, loc.getPitch());

        SmallFireball f4 = entity.getWorld().spawn(loc, SmallFireball.class);
        f4.setRotation(loc.getYaw() + 270, loc.getPitch());

        if (r.nextInt(100) < 3) {
            DragonFireball f5 = entity.getWorld().spawn(loc, DragonFireball.class);
            f5.setDirection(loc.getDirection());
            entity.getWorld().playSound(entity, Sound.ENTITY_ENDER_DRAGON_GROWL, 6F, 1F);
        }
    }
    
    @Defensive(chance = 90)
    public void fire(Player target) {
        target.setFireTicks((r.nextInt(5) + 5) * 20);
    }



}

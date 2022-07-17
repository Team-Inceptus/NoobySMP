package us.teaminceptus.noobysmp.entities.bosses;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableMap;

import us.teaminceptus.noobysmp.ability.cosmetics.SMPCosmetic;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Description;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Drop;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Experience;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.HP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.SpawnCost;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Tier;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Defensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.MinionSpawn;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Repeated;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@Tier(2)
@Description({"The king is back! He's here", "with a fiery purpose."})
@SpawnCost("blaze_rod:32")
@HP(10000)
@Icon(Material.BLAZE_POWDER)
@DisplayName(value = "Blaze King", cc = ChatColor.GOLD)
@Drop(drop = "blaze_rod", amount = "32-64")
@Drop(drop = "blazesaber", chance = 10)
@Drop(drop = "blaze_powder", amount = "32-112", chance = 50)
public class BlazeKing extends SMPBoss<Blaze> {
    
    @Experience(100)
    @MinionSpawn(type = EntityType.BLAZE, chance = 75)
    public BlazeKing(Location loc) {
        super(Blaze.class, loc, 10000, "Blaze King",
        ImmutableMap.<ItemStack, Integer>builder()
        .put(new ItemStack(Material.BLAZE_ROD, r.nextInt(32) + 32), 100)
        .put(new ItemStack(Material.BLAZE_POWDER, 64), 50)
        .put(new ItemStack(Material.BLAZE_POWDER, r.nextInt(16) + 32), 50)
        .put(SMPMaterial.BLAZESABER.getItem(), 10)
        .build(), attributes(10000));
    }

    @Repeated(2)
    public void drawRing() {
        if (entity.getHealth() > 3000) {
        	SMPCosmetic.createCircle(Particle.FLAME, 2).accept(entity.getLocation().add(0, 0.5, 0));
        }
        else {
        	SMPCosmetic.createCircle(Particle.SOUL_FIRE_FLAME, 2).accept(entity.getLocation().add(0, 0.5, 0));
        }
    }
    
    @Defensive(chance = 90)
    public void fire(Player target) {
        target.setFireTicks((r.nextInt(5) + 5) * 20);
    }



}

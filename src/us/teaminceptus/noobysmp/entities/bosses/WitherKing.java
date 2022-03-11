package us.teaminceptus.noobysmp.entities.bosses;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Wither;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Description;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Drop;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Experience;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.HP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.SpawnCost;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Tier;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.MinionSpawn;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Repeated;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@Tier(4)
@Description({"The king of the withers, do not", "bother him in the slightest."})
@SpawnCost({"wither_skeleton_skull:5", "netherite_star:1"})
@HP(3000000)
@Icon(Material.WITHER_ROSE)
@DisplayName(value = "Wither King", cc = ChatColor.DARK_GRAY)
@Drop(drop = "bedrock_ingot", amount = "32-64")
@Drop(drop = "arescent", chance = 50)
public class WitherKing extends SMPBoss<Wither> {
    
    @Experience(30000)
    @MinionSpawn(type = EntityType.WITHER_SKELETON, chance = 90)
    public WitherKing(Location loc) {
        super(Wither.class, loc, 3000000, "Wither King", ImmutableMap.<ItemStack, Integer>builder()
        .put(SMPMaterial.BEDROCK_INGOT.getItem(r.nextInt(32) + 32), 100)
        .put(AbilityItem.ARESCENT.getItem(), 50)
        .build(), attributes(3000000));
    }

    @Repeated(40)
    public void shootFireball() {
        Fireball f = entity.getWorld().spawn(entity.getLocation(), Fireball.class);

        f.setDirection(entity.getLocation().getDirection());
        f.setYield(r.nextInt(4) + 2);
    }

}

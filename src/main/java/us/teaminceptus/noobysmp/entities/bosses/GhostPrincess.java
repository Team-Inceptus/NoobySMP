package us.teaminceptus.noobysmp.entities.bosses;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Stray;
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

@Tier(2)
@Description({"A princess was killed by the ancients", "for an unknown crime. She creeps", "around, seeking revenge."})
@HP(10000)
@SpawnCost("bone:16")
@Icon(Material.BONE)
@DisplayName(value = "Ghost Princess", cc = ChatColor.DARK_GRAY)
@Drop(drop = "bone", amount = "32-64")
public class GhostPrincess extends SMPBoss<Stray> {
    
    @Experience(60)
    @MinionSpawn(type = EntityType.SKELETON, chance = 10)
    public GhostPrincess(Location loc) {
        super(Stray.class, loc, 10000, "Ghost Princess",
        ImmutableMap.<ItemStack, Integer>builder()
        .put(new ItemStack(Material.BONE, r.nextInt(32) + 32), 100)
        
        .build(), attributes(10000));
    }

}

package us.teaminceptus.noobysmp.entities.bosses.npc;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

@Tier(1)
@Description({"Many drowned on the M.S.S. Jerva, and", "this thing emerged."})
@HP(200)
@SpawnCost({"gunpowder:16", "seagrass:8"})
@Icon(Material.SEAGRASS)
@DisplayName(value = "Drowned Creeper", cc = ChatColor.GREEN)
@Drop(drop = "tnt", amount = "8-24", chance = 50)
public class DrownedCreeper extends NPCBoss {
    
    @Experience(10)
    @MinionSpawn(chance = 10, type = EntityType.DROWNED)
    public DrownedCreeper(Location loc) {
        super("Drowned Creeper", loc, 200, 
        ImmutableMap.<ItemStack, Integer>builder()
        .put(new ItemStack(Material.TNT, r.nextInt(16) + 8), 50)
        .build(), NPCSkin.DROWNED_CREEPER);

        spawn();
    }

    @Defensive(chance = 10)
    public void explode(Player target) {
        target.getWorld().createExplosion(target.getLocation(), r.nextInt(3) + 2, false, false, npc.getEntity());
    }

}

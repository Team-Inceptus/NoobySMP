package us.teaminceptus.noobysmp.entities.bosses;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableMap;

import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Description;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Experience;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.HP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.SpawnCost;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Tier;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Defensive;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@Tier(1)
@Description({"A rogue villager raids neighboring villages,", "needing emeralds to serve the evil pillagers.", "Bring justice to those he did wrong!"})
@SpawnCost("emerald:32")
@Icon(Material.EMERALD)
@HP(4000)
@DisplayName(value = "Emerald Thief", cc = ChatColor.GREEN)
public class EmeraldThief extends SMPBoss<Villager> {
    
    @Experience(40)
    public EmeraldThief(Location loc) {
        super(Villager.class, loc, 4000, "Emerald Thief",
        ImmutableMap.<ItemStack, Integer>builder()
        .put(new ItemStack(Material.EMERALD_BLOCK, r.nextInt(8) + 8), 100)
        .build(), attributes(4000));

        this.setGoals(attackGoal(entity));

        entity.getEquipment().setHelmet(SMPMaterial.THIEF_HELMET.getItem());
    }

    @Defensive(chance = 50)
    public void dropEmerald(Player target) {
        target.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.EMERALD));
    }

}

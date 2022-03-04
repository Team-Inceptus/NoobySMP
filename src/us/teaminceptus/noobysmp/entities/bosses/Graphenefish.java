package us.teaminceptus.noobysmp.entities.bosses;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Silverfish;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Description;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.HP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.SpawnCost;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Tier;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@Tier(2)
@Description({"Silverfish dug into a chunk of graphite, and", "this thing was born."})
@HP(10000)
@SpawnCost("graphene:12")
@Icon(Material.GRAY_DYE)
@DisplayName(value = "Graphenefish", cc = ChatColor.RED)
public class Graphenefish extends SMPBoss<Silverfish> {
    
    public Graphenefish(Location loc) {
        super(Silverfish.class, loc, 10000, "Graphenefish",
        ImmutableMap.<ItemStack, Integer>builder()
        .put(SMPMaterial.GRAPHENE.getItem(r.nextInt(2) + 1), 50)
        .put(SMPMaterial.GRAPHENE_SWORD.getItem(), 20)
        .put(SMPMaterial.GRAPHENE_AXE.getItem(), 15)
        .build(), attributes(10000));
    }

}

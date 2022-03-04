package us.teaminceptus.noobysmp.entities.bosses;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Illusioner;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.ability.cosmetics.SMPCosmetic;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Description;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Experience;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.HP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.SpawnCost;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Tier;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.MinionSpawn;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Repeated;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@Tier(1)
@Description({"A lost member of the Illager family, he", "keeps training in hopes to rejoin."})
@HP(900)
@SpawnCost("emerald:8")
@Icon(Material.BOW)
@DisplayName(value = "Magician", cc = ChatColor.RED)
public class Magician extends SMPBoss<Illusioner> {
    
    @Experience(40)
    @MinionSpawn(type = EntityType.VEX, chance = 10)
    public Magician(Location loc) {
        super(Illusioner.class, loc, 900, "Magician",
        ImmutableMap.<ItemStack, Integer>builder()
        .put(SMPMaterial.MAGIC_DUST.getItem(), 100)
        .put(new ItemStack(Material.GUNPOWDER, r.nextInt(32) + 16), 100)
        .build(), attributes(900));
    }

    @Repeated(1)
    public void aura() {
        SMPCosmetic.createCircle(Particle.ENCHANTMENT_TABLE).accept(entity.getLocation().add(0, 0.25, 0));
    }

    

}

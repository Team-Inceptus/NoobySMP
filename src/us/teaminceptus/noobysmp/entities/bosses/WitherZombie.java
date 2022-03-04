package us.teaminceptus.noobysmp.entities.bosses;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
@Description({"Wither has been around for millenia. Who knows", "what new species are popping up..."})
@SpawnCost("rotten_flesh:32")
@HP(950)
@Icon(Material.WITHER_ROSE)
@DisplayName(value = "Wither Zombie", cc = ChatColor.GRAY)
public class WitherZombie extends SMPBoss<Zombie> {
    
    @Experience(25)
    public WitherZombie(Location loc) {
        super(Zombie.class, loc, 950, "Wither Zombie",
        ImmutableMap.<ItemStack, Integer>builder()
        .put(new ItemStack(Material.WITHER_ROSE, r.nextInt(8) + 4), 100)
        .put(new ItemStack(Material.NETHER_STAR, r.nextInt(2) + 2), 50)
        .build(), attributes(950));

        entity.getEquipment().setBoots(SMPMaterial.DAMAGED_WITHERING_BOOTS.getItem());
        entity.getEquipment().setBootsDropChance(0.35F);
    }

    @Defensive
    public void addWither(Player target) {
        target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * (r.nextInt(10) + 10), 1));
    }

}

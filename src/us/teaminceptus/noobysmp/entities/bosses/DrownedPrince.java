package us.teaminceptus.noobysmp.entities.bosses;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import us.teaminceptus.noobysmp.ability.cosmetics.SMPCosmetic;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Description;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Drop;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Experience;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.HP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.SpawnCost;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Tier;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.CancelChance;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.MinionSpawn;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Offensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Repeated;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@Tier(2)
@Description({"The Prince rises for his people, and", "defends them with passion. Be careful around", "this one."})
@SpawnCost({"lilypad:16", "nautilus_shell:8"})
@HP(15500)
@Icon(Material.NAUTILUS_SHELL)
@DisplayName(value = "Drowned Prince", cc = ChatColor.DARK_AQUA)
@Drop(drop = "aquatic_core", amount = "4-16")
public class DrownedPrince extends SMPBoss<Drowned> {
    
    @CancelChance(10)
    @Experience(50)
    @MinionSpawn(type = EntityType.DROWNED, chance = 75)
    public DrownedPrince(Location loc) {
        super(Drowned.class, loc, 15500, "Drowned Prince",
        ImmutableMap.<ItemStack, Integer>builder()
        .put(SMPMaterial.AQUATIC_CORE.getItem(r.nextInt(12) + 4), 100)
        .build(), attributes(15500));

        entity.getEquipment().setHelmet(SMPMaterial.AQUATIC_CROWN.getItem());
        entity.getEquipment().setHelmetDropChance(0.5F);
    }

    @Repeated(1)
    public void aura() {
        SMPCosmetic.createCircle(Particle.WATER_BUBBLE, 1.5).accept(entity.getEyeLocation().add(0, 0.5, 0));
    }
    
    @Offensive(chance = 25)
    public void stun(Player target) {
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (r.nextInt(2) + 3), 14));
        target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (r.nextInt(2) + 3), 2));
    }    

}

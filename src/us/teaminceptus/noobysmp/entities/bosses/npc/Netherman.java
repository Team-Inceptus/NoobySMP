package us.teaminceptus.noobysmp.entities.bosses.npc;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import us.teaminceptus.noobysmp.ability.cosmetics.SMPCosmetic;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Description;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Experience;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.SpawnCost;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Tier;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Defensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Offensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Repeated;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@Tier(1)
@Description({"An endangered species of Nether Realm, they", "were exterminated for a very good reason."})
@SpawnCost({"nether_core:4"})
@Icon(Material.BLAZE_POWDER)
@DisplayName(value = "Netherman", cc = ChatColor.RED)
public class Netherman extends NPCBoss {

    @Experience(10)
    public Netherman(Location loc) {
        super("Netherman", loc, 100, 
        ImmutableMap.<ItemStack, Integer>builder()
        .put(SMPMaterial.NETHER_CORE.getItem(r.nextInt(8) + 8), 100)
        .build(), NPCSkin.NETHERMAN);

        npc.getEntity().setVisualFire(true);
        ((LivingEntity) npc.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));

        spawn();
    }

    @Repeated(1)
    public void aura() {
        SMPCosmetic.createCircle(Particle.LAVA, 2).accept(npc.getEntity().getLocation().add(0, 0.5, 0));
        SMPCosmetic.createCircle(Particle.DRIP_LAVA, 2.5).accept(npc.getEntity().getLocation().add(0, 0.5, 0));
        SMPCosmetic.createCircle(Particle.FLAME, 0.5).accept(npc.getEntity().getLocation().add(0, 2.5, 0));
    }

    @Offensive
    public void setFire(Player target) {
        target.setFireTicks(200);
    }

    @Defensive(chance = 50)
    public void setFireDefensive(Player target) {
        target.setFireTicks(100);
    }
    
    @Repeated(100)
    public void shootFireball() {
        npc.getEntity().getWorld().spawn(npc.getEntity().getLocation().add(0, 1, 0), Fireball.class);
    }

    
}

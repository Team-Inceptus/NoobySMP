package us.teaminceptus.noobysmp.entities.bosses;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

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

@Tier(2)
@Description({"Captain of a Guardian Fleet, he's one of", "the strongest foes you'll meet."})
@SpawnCost({"prismarine_shard:32", "prismarine:8"})
@HP(40000)
@Icon(Material.SEA_LANTERN)
@DisplayName(value = "Captain Guardian", cc = ChatColor.AQUA)
public class CaptainGuardian extends SMPBoss<ElderGuardian> {
	
    @Experience(50)
    @MinionSpawn(type = EntityType.GUARDIAN, chance = 15)
    @MinionSpawn(type = EntityType.ELDER_GUARDIAN, chance = 5)
    public CaptainGuardian(Location loc) {
        super(ElderGuardian.class, loc, 40000, "Captain Guardian",
        ImmutableMap.<ItemStack, Integer>builder()
        .put(SMPMaterial.GUARDIAN_TRIDENT.getItem(), 20)
        .put(new ItemStack(Material.SPONGE, r.nextInt(12) + 12), 100)
        .put(new ItemStack(Material.SEA_LANTERN, r.nextInt(32) + 32), 100)
        .put(new ItemStack(Material.COD, r.nextInt(24) + 24), 100)
        .build(), attributes(6000));

        EnderCrystal superLazer = entity.getWorld().spawn(entity.getLocation(), EnderCrystal.class);
        superLazer.getPersistentDataContainer().set(new NamespacedKey(plugin, "bosscrystal"), PersistentDataType.STRING, "true");
        superLazer.setShowingBottom(false);
        superLazer.setCustomName(ChatColor.DARK_AQUA + "Captain Lazer");

        entity.addPassenger(superLazer);
    }

    @Repeated(5)
    public void beamDrain() {
        if (entity.getTarget() == null) return;
        if (!(entity.getTarget() instanceof Player target)) return;
        EnderCrystal superLazer = (EnderCrystal) entity.getPassengers().stream().filter(en -> en instanceof EnderCrystal c && c.getPersistentDataContainer().has(new NamespacedKey(plugin, "bosscrystal"), PersistentDataType.STRING)).toList().get(0);
         
        superLazer.setBeamTarget(target.getLocation());
        if (target.getEquipment().getHelmet() != null && SMPMaterial.getByItem(target.getEquipment().getHelmet()) == SMPMaterial.AQUATIC_CROWN) return;
        else target.damage(r.nextInt(2) + 1, superLazer);
    }

    

}

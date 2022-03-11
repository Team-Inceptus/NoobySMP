package us.teaminceptus.noobysmp.entities.bosses;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
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
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Repeated;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@Tier(1)
@Description({"Amethyst Zombie's daughter, she has one of", "the fieriest auras I have ever", "encountered."})
@HP(1600)
@SpawnCost("amethyst_shard:24")
@Icon(Material.AMETHYST_SHARD)
@DisplayName(value = "Amethyst Skeleton", cc = ChatColor.LIGHT_PURPLE)
@Drop(drop = "amethyst_shard", amount = "16-48")
@Drop(drop = "sharp_meliorate", chance = 10)
public class AmethystSkeleton extends SMPBoss<Skeleton> {
    
    @Experience(20)
    @MinionSpawn(type = EntityType.STRAY, chance = 20)
    @MinionSpawn(type = EntityType.SKELETON, chance = 30)
    public AmethystSkeleton(Location loc) {
        super(Skeleton.class, loc, 1600, "Amethyst Skeleton",
        ImmutableMap.<ItemStack, Integer>builder()
        .put(new ItemStack(Material.AMETHYST_SHARD, r.nextInt(32) + 16), 100)
        .put(AbilityItem.SHARP_MELIORATE.getItem(), 10)
        .build(), attributes(1600));

        entity.getEquipment().setHelmet(SMPMaterial.AMETHYST_HELMET.getItem());
        entity.getEquipment().setHelmetDropChance(0.2F);

        entity.getEquipment().setChestplate(SMPMaterial.AMETHYST_CHESTPLATE.getItem());
        entity.getEquipment().setChestplateDropChance(0.1F);

        entity.getEquipment().setLeggings(SMPMaterial.AMETHYST_LEGGINGS.getItem());
        entity.getEquipment().setLeggingsDropChance(0.13F);

        entity.getEquipment().setBoots(SMPMaterial.AMETHYST_BOOTS.getItem());
        entity.getEquipment().setBootsDropChance(0.25F);
    }

    @Defensive(chance = 20)
    public void dropItems(Player target) {
        target.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.AMETHYST_SHARD, r.nextInt(3) + 1));
    }
    

    @Repeated(10)
    public void amethystTrail() {
        Location loc = entity.getLocation();
        if (loc.getBlock().getType() == Material.SMALL_AMETHYST_BUD || loc.getBlock().getType() == Material.MEDIUM_AMETHYST_BUD) return;
        if (!(loc.subtract(0, 1, 0).getBlock().getType().isAir())) return;

        if (r.nextInt(100) < 80) {
            loc.getWorld().setType(loc, Material.SMALL_AMETHYST_BUD);
        } else {
            loc.getWorld().setType(loc, Material.MEDIUM_AMETHYST_BUD);
        } 
    }

}

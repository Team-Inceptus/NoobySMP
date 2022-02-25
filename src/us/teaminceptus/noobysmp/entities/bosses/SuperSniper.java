package us.teaminceptus.noobysmp.entities.bosses;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Description;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.SpawnCost;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Tier;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.MinionSpawn;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Offensive;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@Tier(1)
@Description({"An advanced sniper of many battles,", "he is one of the best snipers ever."})
@SpawnCost("arrow:16")
@Icon(Material.ARROW)
@DisplayName(value = "Super Sniper", cc = ChatColor.GREEN)
public class SuperSniper extends SMPBoss<Skeleton> {

    @MinionSpawn(chance = 30, type = EntityType.SKELETON)
    public SuperSniper(Location loc) {
        super(Skeleton.class, loc, 300, "Super Sniper", ImmutableMap.<ItemStack, Integer>builder()
        .put(SMPMaterial.EMERALD_BOW.getItem(), 100)
        .build(),
        ImmutableMap.<Attribute, Double>builder()
        .put(Attribute.GENERIC_KNOCKBACK_RESISTANCE, 5D)
        .put(Attribute.GENERIC_ARMOR, 15D)
        .put(Attribute.GENERIC_ARMOR_TOUGHNESS, 10D)
        .build());

        Skeleton entity = this.getEntity();

        entity.getEquipment().setHelmet(SMPMaterial.EMERALD_HELMET.getItem());
        entity.getEquipment().setBoots(SMPMaterial.EMERALD_BOOTS.getItem());
    }

    @Offensive(chance = 20)
    public void blind(Player target) {
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * r.nextInt(10) + 5,  1, true));
    }

}
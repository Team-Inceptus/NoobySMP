package us.teaminceptus.noobysmp.entities.bosses;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@Tier(1)
@Description({"A mysterious player left a special enchanting table", "on an end island, and this species was born."})
@HP(1000)
@SpawnCost("ender_pearl:16")
@Icon(Material.ENDER_PEARL)
@DisplayName(value = "Enchanted Enderman", cc = ChatColor.LIGHT_PURPLE)
@Drop(drop = "end_core", amount = "16-24")
@Drop(drop = "enchanted_pearl", amount = "8-16", chance = 50)
@Drop(drop = "enchanted_ender_eye", chance = 10)
@Drop(drop = "ocassus_bow_1", chance = 10)
public class EnchantedEnderman extends SMPBoss<Enderman> {
    
    @CancelChance(20)
    @Experience(20)
    @MinionSpawn(chance = 7, type = EntityType.ENDERMAN)
    public EnchantedEnderman(Location loc) {
        super(Enderman.class, loc, 1000, "Enchanted Enderman", 
        ImmutableMap.<ItemStack, Integer>builder()
        .put(SMPMaterial.END_CORE.getItem(r.nextInt(8) + 16), 100)
        .put(SMPMaterial.ENCHANTED_PEARL.getItem(r.nextInt(8) + 8), 50)
        .put(SMPMaterial.ENCHANTED_ENDER_EYE.getItem(r.nextInt(2) + 2), 10)
        .put(AbilityItem.OCASSUS_BOW_1.getItem(), 10)
        .build(), attributes(1000));

        this.setGoals(attackGoal(entity));
    }

    @Repeated(1)
    public void aura() {
        SMPCosmetic.createCircle(Particle.END_ROD, 0.5).accept(entity.getEyeLocation().add(0, 0.5, 0));
    }

    @Offensive(chance = 10)
    public void knockback(Player target) {
        target.setVelocity(entity.getLocation().getDirection().multiply(r.nextInt(2) + 4));
        target.sendMessage(ChatColor.RED + "Whoosh!");

        entity.teleport(target.getLocation().subtract(target.getLocation().getDirection().multiply(2)));
    }

    

}

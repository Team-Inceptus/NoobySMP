package us.teaminceptus.noobysmp.entities.bosses;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Husk;
import org.bukkit.entity.IronGolem;
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
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Defensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Offensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Repeated;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.util.Items;

@Tier(3)
@HP(180000)
@Description({"The ancients discovered the usefulness of", "redstone many years ago, and have built", "protective measures to defend themselves.", "These remnants of a dark past were hidden for a", "very good reason."})
@SpawnCost({"redstone_block:16", "redstone_dust:8"})
@Icon(Material.REDSTONE_BLOCK)
@DisplayName(value = "Redstone Golem", cc = ChatColor.RED)
@Drop(drop = "redstone_chestplate", chance = 20)
@Drop(drop = "redstone_leggings", chance = 30)
@Drop(drop = "redstone_helmet", chance = 50)
@Drop(drop = "redsone_boots", chance = 60)
@Drop(drop = "redstone_block", amount = "16-48")
@Drop(drop = "redstone_bow", chance = 25)
@Drop(drop = "redstone_axe", chance = 25)
@Drop(drop = "redstone_crossbow", chance = 10)
public class RedstoneGolem extends SMPBoss<IronGolem> {
    
    @CancelChance(35)
    @Experience(150)
    public RedstoneGolem(Location loc) {
        super(IronGolem.class, loc, 180000, "Redstone Golem",
        ImmutableMap.<ItemStack, Integer>builder()
        .put(SMPMaterial.REDSTONE_CHESTPLATE.getItem(), 20)
        .put(SMPMaterial.REDSTONE_LEGGINGS.getItem(), 30)
        .put(SMPMaterial.REDSTONE_HELMET.getItem(), 50)
        .put(SMPMaterial.REDSTONE_BOOTS.getItem(), 60)
        .put(new ItemStack(Material.REDSTONE_BLOCK, r.nextInt(32) + 16), 100)
        .put(SMPMaterial.REDSTONE_BOW.getItem(), 25)
        .put(SMPMaterial.REDSTONE_AXE.getItem(), 25)
        .put(SMPMaterial.REDSTONE_CROSSBOW.getItem(), 10)
        .build(), attributes(180000));

        this.setGoals(attackGoal(entity));
    }

    @Repeated(1)
    public void aura() {
        SMPCosmetic.createCircle(Particle.REDSTONE, new DustOptions(Color.RED, 1)).accept(entity.getEyeLocation().add(0, 0.5, 0));
    }

    @Defensive(chance = 50)
    public void minionSpawn(Player target) {
        Husk redstoneHusk = target.getWorld().spawn(entity.getLocation(), Husk.class);
        
        redstoneHusk.getEquipment().setHelmet(Items.itemBuilder(Material.REDSTONE_BLOCK).addGlint().build());
        redstoneHusk.getEquipment().setHelmetDropChance(0F);

        redstoneHusk.getEquipment().setChestplate(SMPMaterial.POWERED_CHESTPLATE.getItem());
        redstoneHusk.getEquipment().setChestplateDropChance(0.25F);

        redstoneHusk.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5F);
    }

    @Offensive(chance = 20)
    public void strikeLightning(Player target) {
        target.getWorld().strikeLightning(target.getLocation());
    }

    @Offensive(chance = 35)
    public void knockback(Player target) {
        target.setVelocity(entity.getLocation().getDirection().multiply((5 * r.nextDouble()) + 1));
    }
}

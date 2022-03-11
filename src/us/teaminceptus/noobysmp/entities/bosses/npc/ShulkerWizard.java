package us.teaminceptus.noobysmp.entities.bosses.npc;

import java.util.Arrays;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
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
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Defensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.MinionSpawn;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Offensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Repeated;
import us.teaminceptus.noobysmp.materials.AbilityItem;

@Tier(3)
@Description({"A shulker masters the art of wizardry from", "his evoker friends. He's looking to test them out."})
@HP(300000)
@SpawnCost({"shulker_shell:16", "blaze_rod:32", "ruby:8"})
@Icon(Material.BLUE_SHULKER_BOX)
@DisplayName(value = "Shulker Wizard", cc = ChatColor.BLUE)
@Drop(drop = "bullet_wand", chance = 20)
@Drop(drop = "shulker_shell", amount = "16-32")
public class ShulkerWizard extends NPCBoss {
    
    @Experience(300)
    @MinionSpawn(type = EntityType.VEX, chance = 25)
    public ShulkerWizard(Location loc) {
        super("Shulker Wizard", loc, 300000, 
        ImmutableMap.<ItemStack, Integer>builder()
        .put(AbilityItem.BULLET_WAND.getItem(), 20)
        .put(new ItemStack(Material.SHULKER_SHELL, r.nextInt(16) + 16), 100)
        .build(), NPCSkin.SHULKER_WIZARD);

        ((LivingEntity) npc.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, true, false));

        spawn();
    }

    @Repeated(1)
    public void aura() {
        SMPCosmetic.createCircle(Particle.SOUL, 1).accept(npc.getEntity().getLocation().add(0, 2, 0));
    }

    @Repeated(4)
    public void fireTrail() {
        if (!(npc.getEntity().getLocation().getBlock().isPassable())) return;

        npc.getEntity().getWorld().setType(npc.getEntity().getLocation(), Material.SOUL_FIRE);
    }

    @Repeated(60)
    public void shootBullet() {
        ShulkerBullet b = npc.getEntity().getWorld().spawn(npc.getEntity().getLocation().add(0, 1, 0), ShulkerBullet.class);

        b.setTarget(npc.getEntity().getNearbyEntities(15, 15, 15).stream().filter(e -> e instanceof Player).toList().get(0));
    }

    @Defensive(chance = 30)
    public void fangs(Player target) {
        EvokerFangs f = target.getWorld().spawn(target.getLocation(), EvokerFangs.class);
        f.setOwner((LivingEntity) npc.getEntity());
    }

    @Offensive(chance = 80)
    public void addEffects(Player target) {
        target.addPotionEffects(Arrays.asList(new PotionEffect(PotionEffectType.BLINDNESS, 20 * (r.nextInt(3) + 1), 1, true), new PotionEffect(PotionEffectType.SLOW, 20 * (r.nextInt(8) + 12), 1, true)));
    }



}

package us.teaminceptus.noobysmp.entities.bosses;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.ImmutableMap;

import us.teaminceptus.noobysmp.ability.cosmetics.SMPCosmetic;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Description;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.HP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.SpawnCost;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Tier;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.CancelChance;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Defensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Repeated;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@Tier(4)
@Description({"Son of the Golden Shulker, he's ready to", "kick some butt, with style."})
@SpawnCost({"enchanted_diamond:2", "diamond_block:8"})
@HP(1500000)
@Icon(Material.DIAMOND)
@DisplayName(value = "Diamond Shulker", cc = ChatColor.AQUA)
public class DiamondShulker extends SMPBoss<Shulker>{
	
	@CancelChance(50)
	public DiamondShulker(Location loc) {
		super(Shulker.class, loc, 1500000, "Diamond Shulker",
				ImmutableMap.<ItemStack, Integer>builder()
				.put(AbilityItem.BULLET_WAND.getItem(), 80)
				.put(SMPMaterial.ENCHANTED_DIAMOND.getItem(r.nextInt(16) + 16), 100)
				.build(), attributes(1500000));
		
		entity.setColor(DyeColor.LIGHT_BLUE);
	}
	
	@Repeated(1)
	public void aura() {
		SMPCosmetic.createCircle(Particle.SMOKE_LARGE).accept(entity.getLocation().add(0, 0.5, 0));
	}
	
	@Defensive(chance = 20)
	public void weaken(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * (r.nextInt(10) + 10), r.nextInt(3) + 2));
	}
	
	@Repeated(100)
	public void shootFireball() {
		Location loc = entity.getLocation().add(0, 1, 0);
		entity.getWorld().spawn(loc, DragonFireball.class);
		
	}
	
	@Defensive(chance = 35)
	public void explode(Player p) {
		p.getWorld().createExplosion(p.getLocation(), r.nextInt(3) + 4, false, false, entity);
	}
	

}

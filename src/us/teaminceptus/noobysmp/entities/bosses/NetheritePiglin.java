package us.teaminceptus.noobysmp.entities.bosses;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Description;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Experience;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.HP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.SpawnCost;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Tier;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.CancelChance;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.MinionSpawn;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Offensive;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@Tier(1)
@Description({"A subspecies of piglins that discovered", "the riches the ancients left for them."})
@SpawnCost("ancient_debris:2")
@HP(350)
@Icon(Material.NETHERITE_INGOT)
@DisplayName(value = "Netherite Piglin", cc = ChatColor.GRAY)
public class NetheritePiglin extends SMPBoss<Piglin> {
	
	@Experience(15)
	@MinionSpawn(chance = 10, type = EntityType.PIGLIN_BRUTE)
	@MinionSpawn(chance = 20, type = EntityType.PIGLIN)
	@CancelChance(5)
	public NetheritePiglin(Location loc) {
		super(Piglin.class, loc, 350, "Netherite Piglin", ImmutableMap.<ItemStack, Integer>builder()
				.put(new ItemStack(Material.NETHERITE_INGOT), 100)
				.build(), attributes(350));
		
		entity.setImmuneToZombification(true);
		entity.getEquipment().setHelmet(new ItemStack(Material.NETHERITE_HELMET));
		entity.getEquipment().setHelmetDropChance(1F);
		
		entity.getEquipment().setChestplate(SMPMaterial.SUPER_NETHERITE_CHESTPLATE.getItem());
		entity.getEquipment().setChestplateDropChance(0.005F);
		
		entity.getEquipment().setLeggings(SMPMaterial.SUPER_NETHERITE_LEGGINGS.getItem());
		entity.getEquipment().setLeggingsDropChance(0.05F);
		
		entity.getEquipment().setBoots(new ItemStack(Material.NETHERITE_BOOTS));
		entity.getEquipment().setBootsDropChance(1F);
	}
	
	@Offensive(chance = 20)
	public void knockback(Player target) {
		target.setVelocity(entity.getLocation().getDirection().multiply(r.nextInt(3) + 1));
		target.sendMessage(NAMETAG + "Goodbye!");
	}

}

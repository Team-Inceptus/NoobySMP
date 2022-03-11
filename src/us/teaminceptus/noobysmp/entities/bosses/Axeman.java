package us.teaminceptus.noobysmp.entities.bosses;

import com.google.common.collect.ImmutableMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vindicator;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Description;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Drop;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Experience;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.HP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.SpawnCost;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Tier;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.MinionSpawn;
import us.teaminceptus.noobysmp.materials.AbilityItem;

@Tier(3)
@Description({"One of the leaders of the Illager Movement,", "he's a skilled axeman. He loved it", "so much, he changed his name."})
@HP(125000)
@SpawnCost({"diamond:8", "iron_ingot:48", "ruby:16"})
@Icon(Material.IRON_AXE)
@DisplayName(value = "Axeman", cc = ChatColor.WHITE)
@Drop(drop = "scroll_throwing", chance = 50)
@Drop(drop = "netherite_axe")
public class Axeman extends SMPBoss<Vindicator> {

	@Experience(200)
	@MinionSpawn(type = EntityType.VINDICATOR, chance = 75)
	public Axeman(Location loc) {
		super(Vindicator.class, loc, 125000, "Axeman", 
				ImmutableMap.<ItemStack, Integer>builder()
				.put(AbilityItem.SCROLL_THROWING.getItem(), 50)
				.put(new ItemStack(Material.NETHERITE_AXE), 100)
				.build(), attributes(125000));
	}

}

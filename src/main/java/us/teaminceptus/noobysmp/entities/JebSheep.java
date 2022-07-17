package us.teaminceptus.noobysmp.entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableList;

@Spawnable(type = EntityType.SHEEP, spawnChance = 10)
public class JebSheep extends SMPEntity<Sheep> {
	
	private static final List<Material> woolList = new ArrayList<>(Tag.WOOL.getValues());
	
	public JebSheep(Location loc) {
		super(Sheep.class, loc, 40, "Jeb Sheep", ImmutableList.<ItemStack>builder()
				.add(new ItemStack(woolList.get(r.nextInt(woolList.size())), r.nextInt(4) + 3))
				.build());
		
		entity.setCustomName("jeb_");
		entity.setCustomNameVisible(false);
	}

}

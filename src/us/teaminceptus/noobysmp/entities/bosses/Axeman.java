package us.teaminceptus.noobysmp.entities.bosses;

import org.bukkit.Location;
import org.bukkit.entity.Vindicator;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableMap;

public class Axeman extends SMPBoss<Vindicator> {

	public Axeman(Location loc) {
		super(Vindicator.class, loc, 125000, "Axeman", 
				ImmutableMap.<ItemStack, Integer>builder()
				
				.build(), attributes(125000));
	}

}

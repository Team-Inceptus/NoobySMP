package us.teaminceptus.noobysmp.entities.titan;

import com.google.common.collect.ImmutableList;

import org.bukkit.Location;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.entities.SMPEntity;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@TitanSpawnable(EntityType.ENDERMAN)
public class Titanmen extends SMPEntity<Enderman> {
    
    public Titanmen(Location loc) {
        super(Enderman.class, loc, 300, "Titanmen",
        ImmutableList.<ItemStack>builder()
        .add(SMPMaterial.ENCHANTED_PEARL.getItem(r.nextInt(3) + 1))
        .build());
    }

}

package us.teaminceptus.noobysmp.entities.titan;

import com.google.common.collect.ImmutableList;

import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.entities.SMPEntity;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@TitanSpawnable(EntityType.CHICKEN)
public class TopazChicken extends SMPEntity<Chicken> {
    
    public TopazChicken(Location loc) {
        super(Chicken.class, loc, 225, "Topaz Chicken",
        ImmutableList.<ItemStack>builder()
        .add(SMPMaterial.TOPAZ.getItem(r.nextInt(2) + 1))
        .build());
    }

}

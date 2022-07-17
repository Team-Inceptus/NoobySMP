package us.teaminceptus.noobysmp.entities.titan;

import com.google.common.collect.ImmutableList;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.entities.SMPEntity;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@TitanSpawnable(EntityType.PIG)
public class RubyPig extends SMPEntity<Pig> {
    
    public RubyPig(Location loc) {
        super(Pig.class, loc, 125, "Ruby Pig",
        ImmutableList.<ItemStack>builder()
        .add(SMPMaterial.RUBY.getItem(r.nextInt(4) + 4))
        .build());
    }

}

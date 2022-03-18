package us.teaminceptus.noobysmp.conquest.entities;

import com.google.common.collect.ImmutableList;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.conquest.ConquestSpawnable;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@ConquestSpawnable(EntityType.SHEEP)
public class RubySheep extends ConquestEntity<Sheep> {
    
    public RubySheep(Location loc) {
        super(loc, Sheep.class, 100, 
        ImmutableList.<ItemStack>builder()
        .add(SMPMaterial.RUBY.getItem(r.nextInt(8) + 8))
        .add(SMPMaterial.RUBY_BLOCK.getItem())
        .build());
    }

}

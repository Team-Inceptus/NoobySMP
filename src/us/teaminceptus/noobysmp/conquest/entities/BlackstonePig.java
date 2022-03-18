package us.teaminceptus.noobysmp.conquest.entities;

import com.google.common.collect.ImmutableList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.conquest.ConquestSpawnable;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@ConquestSpawnable(EntityType.PIG)
public class BlackstonePig extends ConquestEntity<Pig> {
    
    public BlackstonePig(Location loc) {
        super(loc, Pig.class, 120,
        ImmutableList.<ItemStack>builder()
        .add(new ItemStack(Material.BLACKSTONE, r.nextInt(32) + 32))
        .build());

        entity.getEquipment().setHelmet(SMPMaterial.BLACKSTONE_HELMET.getItem());
        entity.getEquipment().setHelmetDropChance(0.5F);

        entity.getEquipment().setBoots(SMPMaterial.BLACKSTONE_BOOTS.getItem());
        entity.getEquipment().setBootsDropChance(0.5F);
    }
}

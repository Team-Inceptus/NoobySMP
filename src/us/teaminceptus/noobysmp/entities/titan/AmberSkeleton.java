package us.teaminceptus.noobysmp.entities.titan;

import com.google.common.collect.ImmutableList;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.entities.SMPEntity;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@TitanSpawnable(EntityType.SKELETON)
public class AmberSkeleton extends SMPEntity<Skeleton> {
    
    public AmberSkeleton(Location loc) {
        super(Skeleton.class, loc, 350, "Amber Skeleton",
        ImmutableList.<ItemStack>builder()
        .add(SMPMaterial.AMBER.getItem(r.nextInt(8) + 4))
        .add(SMPMaterial.CUT_AMBER.getItem(r.nextInt(2) + 2))
        .build());

        entity.getEquipment().setItemInMainHand(SMPMaterial.AMBER_BOW.getItem());
        entity.getEquipment().setItemInMainHandDropChance(0.125F);
    }

}

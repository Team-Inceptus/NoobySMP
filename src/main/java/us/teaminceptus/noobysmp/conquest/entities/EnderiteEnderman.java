package us.teaminceptus.noobysmp.conquest.entities;

import com.google.common.collect.ImmutableList;

import org.bukkit.Location;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.conquest.ConquestSpawnable;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@ConquestSpawnable(EntityType.ENDERMAN)
public class EnderiteEnderman extends ConquestEntity<Enderman> {
    
    public EnderiteEnderman(Location loc) {
        super(loc, Enderman.class, 325, 
        ImmutableList.<ItemStack>builder()
        .add(SMPMaterial.ENDERITE.getItem(r.nextInt(4) + 4))
        .add(SMPMaterial.ENCHANTED_PEARL.getItem(r.nextInt(6) + 1))
        .build());

        entity.getEquipment().setItemInMainHand(SMPMaterial.ENDERITE_SWORD.getItem());
        entity.getEquipment().setItemInMainHandDropChance(0.15F);

        entity.getEquipment().setHelmet(SMPMaterial.ENDERITE_HELMET.getItem());
        entity.getEquipment().setHelmetDropChance(0.15F);
    }

}

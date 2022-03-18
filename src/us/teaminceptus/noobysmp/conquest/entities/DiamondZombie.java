package us.teaminceptus.noobysmp.conquest.entities;

import com.google.common.collect.ImmutableList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.materials.SMPMaterial;

public class DiamondZombie extends ConquestEntity<Zombie> {
    
    public DiamondZombie(Location loc) {
        super(loc, Zombie.class, 175, 
        ImmutableList.<ItemStack>builder()
        .add(SMPMaterial.ENCHANTED_DIAMOND.getItem(r.nextInt(2) + 2))
        .add(new ItemStack(Material.DIAMOND, r.nextInt(16) + 16))
        .build());

        entity.getEquipment().setHelmet(SMPMaterial.ENCHANTED_DIAMOND_BLOCK.getItem());
        entity.getEquipment().setHelmetDropChance(0F);

        entity.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        entity.getEquipment().setChestplateDropChance(1F);

        entity.getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_AXE));
        entity.getEquipment().setItemInMainHandDropChance(1F);
    }

}

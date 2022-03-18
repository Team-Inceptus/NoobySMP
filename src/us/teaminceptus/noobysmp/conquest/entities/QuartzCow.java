package us.teaminceptus.noobysmp.conquest.entities;

import com.google.common.collect.ImmutableList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.conquest.ConquestSpawnable;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@ConquestSpawnable(EntityType.COW)
public class QuartzCow extends ConquestEntity<Cow> {
    
    public QuartzCow(Location loc) {
        super(loc, Cow.class, 200,
        ImmutableList.<ItemStack>builder()
        .add(new ItemStack(Material.QUARTZ, r.nextInt(16) + 24))
        .build());

        entity.getEquipment().setChestplate(SMPMaterial.QUARTZ_CHESTPLATE.getItem());
        entity.getEquipment().setChestplateDropChance(0.25F);
    }

}

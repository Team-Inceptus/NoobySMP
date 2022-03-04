package us.teaminceptus.noobysmp.entities;

import com.google.common.collect.ImmutableList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.materials.SMPMaterial;

@Spawnable(type = EntityType.BLAZE, spawnChance = 5)
public class EnchantedBlaze extends SMPEntity<Blaze> {
    
    public EnchantedBlaze(Location loc) {
        super(Blaze.class, loc, 50, "Enchanted Blaze",
        ImmutableList.<ItemStack>builder()
        .add(SMPMaterial.NETHER_CORE.getItem(r.nextInt(4) + 4))
        .add(new ItemStack(Material.BLAZE_ROD, r.nextInt(8) + 4))
        .build());
    }

}

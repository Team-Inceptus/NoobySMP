package us.teaminceptus.noobysmp.entities;

import com.google.common.collect.ImmutableList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.util.Items;

@Spawnable(type = EntityType.HUSK, spawnChance = 10)
public class LapisHusk extends SMPEntity<Husk> {
    
    public LapisHusk(Location loc) {
        super(Husk.class, loc, 75, "Lapis Husk",
        ImmutableList.<ItemStack>builder()
        .add(new ItemStack(Material.LAPIS_LAZULI, r.nextInt(12) + 12))
        .build());

        entity.getEquipment().setHelmet(Items.itemBuilder(Material.LAPIS_BLOCK).addGlint().build());
        entity.getEquipment().setHelmetDropChance(0F);
    }

}

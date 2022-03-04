package us.teaminceptus.noobysmp.entities.titan;

import com.google.common.collect.ImmutableList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.entities.SMPEntity;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@TitanSpawnable(replace = EntityType.ZOMBIE)
public class JadeZombie extends SMPEntity<Zombie> {
    
    public JadeZombie(Location loc) {
        super(Zombie.class, loc, 200, ChatColor.BLUE + "Jade Zombie", ImmutableList.<ItemStack>builder()
        .add(SMPMaterial.JADE.getItem(r.nextInt(10) + 4))
        .build());

        entity.getEquipment().setItemInMainHand(SMPMaterial.TOPAZ_JADE_AXE.getItem());

        entity.getEquipment().setHelmet(SMPMaterial.JADE_BLOCK.getItem(r.nextInt(3) + 1));
        entity.getEquipment().setChestplate(SMPMaterial.JADE_CHESTPlATE.getItem());
        entity.getEquipment().setLeggings(SMPMaterial.JADE_LEGGINGS.getItem());
        entity.getEquipment().setBoots(SMPMaterial.JADE_BOOTS.getItem());
    }

}

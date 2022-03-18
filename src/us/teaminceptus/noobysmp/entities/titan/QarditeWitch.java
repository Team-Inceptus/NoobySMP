package us.teaminceptus.noobysmp.entities.titan;

import com.google.common.collect.ImmutableList;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Witch;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.entities.SMPEntity;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

@TitanSpawnable(EntityType.WITCH)
public class QarditeWitch extends SMPEntity<Witch> {
    
    public QarditeWitch(Location loc) {
        super(Witch.class, loc, 550, "Qardite Witch",
        ImmutableList.<ItemStack>builder()
        .add(SMPMaterial.QARDITE.getItem(r.nextInt(4) + 4))
        .add(SMPMaterial.QARDITE_BOW.getItem())
        .build());
    }

}

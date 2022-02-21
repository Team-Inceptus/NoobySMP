package us.teaminceptus.noobysmp.entities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.materials.SMPMaterial;

public class BlackstoneSkeleton extends SMPEntity<WitherSkeleton> {

	public BlackstoneSkeleton(Location loc) {
		super(WitherSkeleton.class, loc, 60, "Blackstone Skeleton", null);
		
		WitherSkeleton entity = this.getEntity();
		
		entity.getEquipment().setHelmet(SMPMaterial.BLACKSTONE_HELMET.getItem());
		entity.getEquipment().setChestplate(SMPMaterial.BLACKSTONE_CHESTPLATE.getItem());
		entity.getEquipment().setLeggings(SMPMaterial.BLACKSTONE_LEGGINGS.getItem());
		entity.getEquipment().setBoots(SMPMaterial.BLACKSTONE_BOOTS.getItem());
		
		entity.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD));
	}

}

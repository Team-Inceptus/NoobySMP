package us.teaminceptus.noobysmp.entities;

import com.google.common.collect.ImmutableList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.materials.SMPMaterial;

@Spawnable(type = EntityType.SKELETON, spawnChance = 5)
public class RubySkeleton extends SMPEntity<Skeleton> {
    
    public RubySkeleton(Location loc) {
        super(Skeleton.class, loc, 50, ChatColor.RED + "Ruby Skeleton", ImmutableList.<ItemStack>builder()
        .add(SMPMaterial.RUBY.getItem(r.nextInt(4) + 2))
        .build());

        Skeleton entity = this.getEntity();

        entity.getEquipment().setItemInMainHand(SMPMaterial.RUBY_SWORD.getItem());

        entity.getEquipment().setHelmet(SMPMaterial.RUBY_HELMET.getItem());
        entity.getEquipment().setChestplate(SMPMaterial.RUBY_CHESTPLATE.getItem());
        entity.getEquipment().setLeggings(SMPMaterial.RUBY_LEGGINGS.getItem());
        entity.getEquipment().setBoots(SMPMaterial.RUBY_BOOTS.getItem());
    }
}

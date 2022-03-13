package us.teaminceptus.noobysmp.ability;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.CartographyInventory;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.StonecutterInventory;
import org.bukkit.metadata.FixedMetadataValue;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.Messages;

public class AbilityManager implements Listener {

	protected SMP plugin;
	
	public AbilityManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	// Prevent ability items being used in non-ability ways
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();

		if (AbilityItem.getByItem(e.getItem()) != null) {
			e.setCancelled(true);
			p.sendMessage(Messages.CANNOT_CONSUME);
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) return;
		ItemStack item = e.getCurrentItem();
		if (!(item.hasItemMeta())) return;
		if (AbilityItem.getByItem(item) == null) return;

		Inventory inv = e.getClickedInventory();
		if (inv instanceof FurnaceInventory) e.setCancelled(true);
		if (inv instanceof EnchantingInventory) e.setCancelled(true);
		if (inv instanceof CartographyInventory) e.setCancelled(true);
		if (inv instanceof BeaconInventory) e.setCancelled(true);
		if (inv instanceof StonecutterInventory) e.setCancelled(true);
		if (inv instanceof GrindstoneInventory) e.setCancelled(true);
	}

	/*
	 * Name-specific abilities (i.e. Ocassus) 
	 */

	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		if (!(e.getEntity() instanceof Player p)) return;

		if (AbilityItem.getByLocalization(Items.getLocalization(e.getConsumable())) != null) {
			e.setCancelled(true);
			return;
		}
		
		e.getProjectile().setMetadata("bow", new FixedMetadataValue(plugin, e.getBow()));
		
	}

	private final Random r = new Random();

	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if (e.getHitEntity() == null) return;
		if (!(e.getHitEntity() instanceof Mob mob)) return;
		Projectile proj = e.getEntity();
		if (!(proj.getShooter() instanceof Player p)) return;

		try {
			ItemStack bow = (ItemStack) proj.getMetadata("bow").stream().filter(m -> m.getOwningPlugin() instanceof SMP).toList().get(0).value();
		
			if (AbilityItem.getByItem(bow) != null) {
				AbilityItem item = AbilityItem.getByItem(bow);

				if (item.name().toLowerCase().startsWith("occasus") && mob instanceof Enderman) {
					e.setCancelled(true);
					proj.remove();
					double multiplier = Double.parseDouble(item.name().substring(item.name().length() - 1));

					mob.damage(multiplier * (100 * r.nextDouble()), p);
				}
			}
		} catch (NullPointerException err) {
			// do nothing
		}
	}

	/*
	 * Used for Armor (Consumer)
	 */
	@EventHandler
	public void onDamageAbility(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player p)) return;
		
		for (ItemStack armor : p.getInventory().getArmorContents()) {
			if (armor == null) continue;
			
			if (AbilityItem.getByItem(armor) != null) {
				AbilityItem item = AbilityItem.getByItem(armor);
				
				if (SMPAbility.getByItem(item) != null) {
					SMPAbility ability = SMPAbility.getByItem(item);
					
					if (ability.getItem() == item && ability.getConsumer() != null && Arrays.asList(ability.getActions()).contains(Action.PHYSICAL)) {
						ability.init(p);
					}
				}
			}
		}
	}
	
	/*
	 * Used for Clicks (Consumer)
	 */
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getItem() == null) return;
		
		ItemStack itemStack = e.getItem();
		if (AbilityItem.getByItem(itemStack) != null) {
			AbilityItem item = AbilityItem.getByItem(itemStack);
			
			if (SMPAbility.getByItem(item) != null) {
				SMPAbility ability = SMPAbility.getByItem(item);
				if (ability.getItem() == item && ability.getConsumer() != null && Arrays.asList(ability.getActions()).contains(e.getAction())) {
					ability.init(p);
				}
			}
		}
	
	}

	/*
	 * Item-Specific Abilities
	 */

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player p)) return;

		if (p.getEquipment().getBoots() != null) {
			ItemStack item = p.getEquipment().getBoots();

			if (SMPMaterial.getByItem(item) != null) {
				SMPMaterial m = SMPMaterial.getByItem(item);

				if (m.name().toLowerCase().contains("withering_boots") && e.getCause() == DamageCause.WITHER) {
					e.setCancelled(true);
				}
			}	

		}
	}

}

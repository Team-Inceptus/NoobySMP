package us.teaminceptus.noobysmp.ability;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.enchants.SMPEnchant;
import us.teaminceptus.noobysmp.materials.AbilityItem;

public class AbilityManager implements Listener {

	protected SMP plugin;
	
	public AbilityManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	/*
	 * Used for Armor (Consumer)
	 */
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
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
		} else {
			for (SMPEnchant ench : SMPEnchant.values()) {
				if (itemStack.containsEnchantment(ench) && SMPAbility.getByEnchant(ench) != null) {
					SMPAbility ability = SMPAbility.getByEnchant(ench);
					
					if (ability.getConsumer() != null && Arrays.asList(ability.getActions()).contains(e.getAction())) {
						ability.init(p);
					}
				}
			}
		}
	
	}

}

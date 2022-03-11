package us.teaminceptus.noobysmp.materials;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.commands.Settings;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.SMPUtil;

public class TagsManager implements Listener {
	
	protected SMP plugin;
	
	public TagsManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	// Adding Tags
	@EventHandler
	public void onAnvil(PrepareAnvilEvent e) {
		AnvilInventory inv = e.getInventory();
		if (inv.getItem(SMPUtil.ANVIL_ITEM_SLOT) == null) return;
		if (inv.getItem(SMPUtil.ANVIL_ADDITION_SLOT) == null) return;
		ItemStack item = inv.getItem(SMPUtil.ANVIL_ITEM_SLOT);
		ItemStack addition = inv.getItem(SMPUtil.ANVIL_ADDITION_SLOT);
		if (AbilityItem.getByItem(addition) == null) return;
		AbilityItem aitem = AbilityItem.getByItem(addition);
		if (SMPTag.getByOrigin(aitem) == null) return;
		SMPTag<?> tag = SMPTag.getByOrigin(aitem);

		if (!(tag.getTarget().matches(item.getType()))) {
			e.setResult(Items.Tags.NOT_COMPATIBLE);
			return;
		}

		if (SMPTag.getTags(item).size() >= 3) {
			e.setResult(Items.Tags.TOO_MANY_TAGS);
			return;
		}

		ItemStack newItem = tag.addTag(item);
		e.setResult(newItem);
		inv.setRepairCost(Math.min(aitem.getLevelUnlocked(), 35));
	}

	// Init Tags

	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		if (!(e.getEntity() instanceof Player p)) return;

		for (SMPTag<EntityShootBowEvent> tag : SMPTag.getByType(EntityShootBowEvent.class)) {
			if (tag.hasTag(e.getBow())) {
				tag.run(e);
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		for (SMPTag<BlockBreakEvent> tag : SMPTag.getByType(BlockBreakEvent.class)) {
			if (new PlayerConfig(p).getSetting(Settings.TAG_ABILITIES) && p.getEquipment().getItemInMainHand() != null && tag.hasTag(p.getEquipment().getItemInMainHand())) {
				tag.run(e);
			}
		}
	}
	
	@EventHandler
	public void onDamageAttack(EntityDamageByEntityEvent e) {
		for (SMPTag<EntityDamageByEntityEvent> tag : SMPTag.getByType(EntityDamageByEntityEvent.class)) {
			if (e.getDamager() instanceof Player p && new PlayerConfig(p).getSetting(Settings.TAG_ABILITIES) && p.getEquipment().getItemInMainHand() != null && tag.hasTag(p.getEquipment().getItemInMainHand())) {
				tag.run(e);
			}

			if (e.getDamager() instanceof Projectile proj && proj.getShooter() instanceof Player p && new PlayerConfig(p).getSetting(Settings.TAG_ABILITIES) && p.getEquipment().getItemInMainHand() != null && tag.hasTag(p.getEquipment().getItemInMainHand())) {
				tag.run(e);
			}

			if (e.getEntity() instanceof Player target && new PlayerConfig(target).getSetting(Settings.TAG_ABILITIES)) {
				for (ItemStack item : target.getEquipment().getArmorContents()) {
					if (item == null) continue;
					tag.run(e);
				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player p)) return;
		if (!(new PlayerConfig(p).getSetting(Settings.TAG_ABILITIES))) return;
		for (SMPTag<EntityDamageEvent> tag : SMPTag.getByType(EntityDamageEvent.class)) {
			tag.run(e);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getItem() == null) return;
		ItemStack item = e.getItem();
		if (!(item.hasItemMeta())) return;
		if (!(new PlayerConfig(e.getPlayer()).getSetting(Settings.TAG_ABILITIES))) return;
		for (SMPTag<PlayerInteractEvent> tag : SMPTag.getByType(PlayerInteractEvent.class)) {
			if (tag.hasTag(item)) {
				tag.run(e);
			}
		}
	}
	

}

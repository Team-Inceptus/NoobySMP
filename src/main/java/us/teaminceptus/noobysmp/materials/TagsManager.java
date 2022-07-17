package us.teaminceptus.noobysmp.materials;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.commands.Settings;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.SMPUtil;
import us.teaminceptus.noobysmp.util.events.PlayerTickEvent;

public class TagsManager implements Listener {
	
	protected final SMP plugin;
	
	public TagsManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	// Adding Tags
	@EventHandler
	public void onAnvil(PrepareAnvilEvent e) {
		AnvilInventory inv = e.getInventory();
		if (!(e.getViewers().get(0) instanceof Player p)) return;
		if (inv.getItem(SMPUtil.ANVIL_ITEM_SLOT) == null) return;
		if (inv.getItem(SMPUtil.ANVIL_ADDITION_SLOT) == null) return;
		ItemStack item = inv.getItem(SMPUtil.ANVIL_ITEM_SLOT);
		ItemStack addition = inv.getItem(SMPUtil.ANVIL_ADDITION_SLOT);
		if (AbilityItem.getByItem(addition) == null) return;
		AbilityItem aitem = AbilityItem.getByItem(addition);
		if (SMPTag.getByOrigin(aitem) == null) return;
		SMPTag<?> tag = SMPTag.getByOrigin(aitem);
		
		if (tag.hasTag(item)) return;
		
		if (!(tag.getTarget().matches(item.getType()))) {
			e.setResult(Items.Tags.NOT_COMPATIBLE);
			return;
		}

		PlayerConfig config = new PlayerConfig(p);

		if (SMPTag.getTags(item).size() >= config.getTagLimit()) {
			e.setResult(Items.Tags.TOO_MANY_TAGS);
			return;
		}

		ItemStack newItem = tag.addTag(item);
		
		new BukkitRunnable() {
			public void run() {
				inv.setRepairCost(Math.min(Math.max(aitem.getLevelUnlocked(), 1), 35));
				p.setWindowProperty(Property.REPAIR_COST, Math.min(Math.max(aitem.getLevelUnlocked(), 1), 35));
				p.updateInventory();
				
				e.setResult(newItem);
				inv.setItem(2, newItem);
			}
		}.runTask(plugin);
		
	}

	// Init Tags

	@EventHandler
	public void onTick(PlayerTickEvent e) {
		Player p = e.getPlayer();
		
		if (!(new PlayerConfig(p).getSetting(Settings.TAG_ABILITIES))) return;
		for (SMPTag<PlayerTickEvent> tag : SMPTag.getByType(PlayerTickEvent.class)) {
			for (ItemStack armor : p.getInventory().getArmorContents()) {
				if (tag.hasTag(armor) && tag.getTarget().matches(armor.getType())) {
					tag.run(e);
				}
			}
		}
	}
	
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
	public void onBlockDamage(BlockDamageEvent e) {
		Player p = e.getPlayer();
		for (SMPTag<BlockDamageEvent> tag : SMPTag.getByType(BlockDamageEvent.class)) {
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
			for (ItemStack armor : p.getInventory().getArmorContents()) {
				if (tag.hasTag(armor) && tag.getTarget().matches(armor.getType())) {
					tag.run(e);
				}
			}
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

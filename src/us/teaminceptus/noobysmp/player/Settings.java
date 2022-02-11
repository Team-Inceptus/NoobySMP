package us.teaminceptus.noobysmp.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Messages;
import us.teaminceptus.noobysmp.util.PlayerConfig;

public class Settings implements Listener, CommandExecutor {

	protected SMP plugin;

	public Settings(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("settings").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public static final String NOTIFICATIONS = "Notifications";

	public static final String[] SETTINGS = {
		NOTIFICATIONS,
	};
	
	public static Inventory getSettings(Player p) {
		Inventory settings = Generator.genGUI(36, p.getDisplayName() + ChatColor.DARK_GRAY + "'s Settings");
		PlayerConfig config = new PlayerConfig(p);

		for (String s : SETTINGS) {
			boolean on = config.getSetting(s);
			ItemStack setting = new ItemStack(on ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
			ItemMeta meta = setting.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + s + ": " + (on ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
			if (on) {
				meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			setting.setItemMeta(meta);

			settings.addItem(setting);
		}
		
		return settings;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player p)) return;
		InventoryView view = e.getView();
		if (!(view.getTitle().contains(p.getDisplayName())) && !(view.getTitle().contains("Settings"))) return;
		e.setCancelled(true);
		
		if (e.getCurrentItem() == null) return;
		ItemStack i = e.getCurrentItem();
		if (!(i.hasItemMeta())) return;
		if (!(i.getItemMeta().hasDisplayName())) return;

		PlayerConfig config = new PlayerConfig(p);

		String settingName = ChatColor.stripColor(i.getItemMeta().getDisplayName()).split(" ")[0].replace(":", "");
		boolean setting = config.getSetting(settingName);

		ItemStack settingItem = new ItemStack(setting ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
		ItemMeta meta = settingItem.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + settingName + ": " + (setting ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
		if (setting) {
			meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		settingItem.setItemMeta(meta);
		
		config.setSetting(settingName, !setting);
		view.setItem(e.getRawSlot(), settingItem);
		p.playSound(p, Sound.ENTITY_ARROW_SHOOT, 3F, 0F);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player p)) {
			sender.sendMessage(Messages.PLAYER_ONLY_CMD);
			return true;
		}

		p.openInventory(getSettings(p));
		return true;
	}
	
}
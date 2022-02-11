package us.teaminceptus.noobysmp.player;

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
	}
	
	public static Inventory getSettings(Player p) {
		Inventory settings = Generater.genGUI(36, p.getDisplayName() + ChatColor.DARK_GRAY + "'s Settings");
		PlayerConfig config = new PlayerConfig(p);

		for (String s : SETTINGS) {
			boolean on = config.getSetting(s);
			ItemStack setting = new ItemStack(on ? Material.GREEN_CONCRETE : Material.RED_CONCRETE);
			ItemMeta meta = setting.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + s + ": " + (on ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
			if (on) {
				meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
				meta.addItemFlag(ItemFlag.HIDE_ENCHANTS);
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
			meta.addItemFlag(ItemFlag.HIDE_ENCHANTS);
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
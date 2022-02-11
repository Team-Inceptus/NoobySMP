package us.teaminceptus.noobysmp.inventory;

public class PlayerMenu implements Listener {

	protected SMP plugin;

	public PlayerMenu(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public static Inventory getMenu(Player p) {
		PlayerConfig config = new PlayerConfig(p);

		if (config.getLevel() < 1) return null;

		Inventory menu = Generator.genGUI(45, p.getDisplayName());

		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		meta.setOwningPlayer(p);
		meta.setDisplayName(p.getDisplayName());
		meta.setLore(Arrays.asList(ChatColor.GREEN + "Level: " + ChatColor.GOLD + config.getLevel(),
															 ChatColor.GREEN + "Expereince: " + ChatColor.GOLD + Double.toString(Math.floor(config.getExperience() * 100) / 100),
															 ChatColor.GREEN + "XP to Next Level: " + ChatColor.GOLD + Double.toString(Math.floor(config.getExpToNextLevel() * 100) / 100)));
		head.setItemMeta(meta);
		inv.setItem(head, 13);

		ItemStack settings = new ItemStack(Material.BEDROCK);
		ItemMeta meta = settings.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_PURPLE + "Settings");
		settings.setItemMeta(meta);
		inv.setItem(settings, 20);

		
		return menu;
	}
	
}
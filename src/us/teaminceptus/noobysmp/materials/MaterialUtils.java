package us.teaminceptus.noobysmp.materials;

public class MaterialUtils implements Listener {

	protected SMP plugin;
	
	public MaterialUtils(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);

		plugin.getLogger().info("Loaded utilities for " + Integer.toString(SMPMaterial.values().length) + " custom materials");
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		ItemStack item = e.getItemInHand();

		if (!(item.hasItemMeta())) return;
		if (!(item.getItemMeta().hasDisplayName())) return;
	}
	
}
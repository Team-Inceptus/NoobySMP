package us.teaminceptus.noobysmp.player;

public class ServerManager implements Listener {

	protected SMP plugin;

	public ServerManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		plugin.getLogger().info("Loaded Server Manager Listener");
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		PlayerConfig config = new PlayerConfig(p);

		if (config.isMember()) {
			ChatColor namePrefix = (config.getLevel() < 10 ? ChatColor.GRAY : ChatColor.WHITE);
			ChatColor levelPrefix = (config.getLevel() > 200 ? ChatColor.GOLD : Ranks.LEVEL_COLOR.get(config.getLevel()));
			p.setDisplayName(levelPrefix + (config.getLevel() > 100 ? ChatColor.BOLD + "" : "") + "[" + config.getLevel() + "] " + ChatColor.RESET + namePrefix + p.getName());
			p.setPlayerListName(levelPrefix + p.getName());
		} else {
			
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		PlayerConfig config = new PlayerConfig(p);

		e.setFormat("%s " + ChatColor.GRAY + "⇒ " + ChatColor.RESET + "%s");
	}
	
}
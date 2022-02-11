package us.teaminceptus.noobysmp.player;

public class Ranks implements TabExecutor, Listener {

	protected SMP plugin;

	// Admin
	public static final String OWNER_TAB = ChatColor.DARK_RED + "" + ChatColor.BOLD + "[OWNER] " + ChatColor.GOLD;
	public static final String OWNER_CHAT = ChatColor.DARK_RED + "" + ChatColor.BOLD + "Owner " + ChatColor.GOLD;

	public static final Map<Integer, ChatColor> LEVEL_COLOR = new HashMap<>();

	static {
		// Level Color
		for (int i = 0; i < 5; i++) LEVEL_COLOR.put(i, ChatColor.GRAY);
		for (int i = 5; i < 20; i++) LEVEL_COLOR.put(i, ChatColor.WHITE);
		for (int i = 20; i < 40; i++) LEVEL_COLOR.put(i, ChatColor.YELLOW);
		for (int i = 40; i < 75; i++) LEVEL_COLOR.put(i, ChatColor.GREEN);
		for (int i = 75; i < 100; i++) LEVEL_COLOR.put(i, ChatColor.AQUA);
		for (int i = 100; i < 155; i++) LEVEL_COLOR.put(i, ChatColor.BOLD + "" + ChatColor.DARK_PURPLE);
		for (int i = 155; i < 200; i++) LEVEL_COLOR.put(i, ChatColor.BOLD + ""+ ChatColor.LIGHT_PURPLE);
		LEVEL_COLOR.put(200, ChatColor.BOLD + "" + ChatColor.GOLD);
	}
	
	public Ranks(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		plugin.getCommand("setrank").setExecutor(this);
	}

	public static void setRank(Player p, String chat, String tab) {
		p.setDisplayName(chat + p.getName());
		p.setPlayerListName(tab + p.getName());
		new PlayerConfig(p).setRank(ChatColor.stripColor(chat).toLowerCase().replace(" ", ""));
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender.hasPermission("smp.admin.setrank"))) {
			sender.sendMessage(Messages.NO_PERMISSION_CMD);
			return true;
		}
		
		
		return true;
	}
	
}
package us.teaminceptus.noobysmp.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.commands.admin.Ranks;
import us.teaminceptus.noobysmp.commands.admin.Ranks.RankData;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

public class ServerManager implements Listener {

	protected SMP plugin;

	public ServerManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		plugin.getLogger().info("Loaded Server Manager Listener");
	}
	
	public static final String IP = "smp.teaminceptus.us";
	
	public static final String TOP_PLAYER_LIST = "\n&6Hello, &a<player>!\n\n&6Welcome to NoobySMP!\n  &6You are playing on &a" + IP + "  \n";

	public static final String BOTTOM_PLAYER_LIST = "\n&6<count> / <total> Players Online\n";
	
	private static final String formatList(Player p, String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg.replace("<player>", p.getName()).replace("<count>", Integer.toString(p.getServer().getOnlinePlayers().size())).replace("<total>", Integer.toString(p.getServer().getMaxPlayers()) ));
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		plugin.loadFiles();
		PlayerConfig config = new PlayerConfig(p);

		if (config.isMember()) config.updateRank();
		else {
			RankData data = Ranks.RANK_MAP.get(config.getRank());
			Ranks.setRank(p, data.getChat(), data.getTab());
		}
		
		if (p.hasPlayedBefore()) {
			e.setJoinMessage(p.getDisplayName() + ChatColor.GREEN + ", welcome back to NoobySMP!");
		} else {
			e.setJoinMessage(p.getDisplayName() + ChatColor.GREEN + ", welcome to NoobySMP!");
		}
		
		p.setPlayerListHeaderFooter(formatList(p, TOP_PLAYER_LIST), formatList(p, BOTTOM_PLAYER_LIST));
		
		new BukkitRunnable() {
			public void run() {
				config.updateItemsInConfig();
			}
		}.runTaskLater(plugin, 3);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setFormat("%s " + ChatColor.GRAY + ">" + ChatColor.RESET + " %s");
	}

	// Global Managers

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		InventoryView view = e.getView();

		if (view.getTopInventory().getHolder() instanceof CancelHolder && !(e.getClickedInventory() instanceof PlayerInventory)) e.setCancelled(true);
	}
	
}
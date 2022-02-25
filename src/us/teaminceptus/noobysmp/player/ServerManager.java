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

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.commands.admin.Ranks;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

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
		e.setFormat("%s " + ChatColor.GRAY + ">" + ChatColor.RESET + "%s");
	}

	// Global Managers

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		InventoryView view = e.getView();

		if (view.getTopInventory().getHolder() instanceof CancelHolder) e.setCancelled(true);
	}
	
}
package us.teaminceptus.noobysmp.player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.commands.admin.Ranks;
import us.teaminceptus.noobysmp.commands.admin.Ranks.RankData;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

public class ServerManager implements Listener {

	protected SMP plugin;
	public static Scoreboard pList;
	public static Scoreboard sidebar;
	public static ScoreboardManager manager;

	private static void registerScoreboard() {
		ScoreboardManager m = Bukkit.getScoreboardManager();
		
		Scoreboard sidebar = m.getNewScoreboard();
		Objective side = sidebar.registerNewObjective("sidebar", "dummy",
		ChatColor.DARK_GREEN + "NoobySMP");
		side.setDisplaySlot(DisplaySlot.SIDEBAR);

		Scoreboard pList = m.getNewScoreboard();

		Objective pO = pList.registerNewObjective("playersort", "dummy", "Player List");
		pO.setDisplaySlot(DisplaySlot.PLAYER_LIST);

		for (Map.Entry<String, RankData> entry : Ranks.RANK_MAP.entrySet()) {
			String name = entry.getKey();
			String weight = "" + entry.getValue().getWeight();
			
			pList.registerNewTeam(weight + name);
		}

		ServerManager.pList = pList;
		ServerManager.sidebar = sidebar;
		manager = m;
	}

	public ServerManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		plugin.getLogger().info("Registering Scoreboard...");
		registerScoreboard();
		plugin.getLogger().info("Successfully registered NoobySMP Scoreboards");
	}
	
	public static final String IP = "smp.teaminceptus.us";
	
	public static final String TOP_PLAYER_LIST = "\n&6Hello, &a<player>!\n\n&6Welcome to NoobySMP!\n  &6You are playing on &a" + IP + "  \n";

	public static final String BOTTOM_PLAYER_LIST = "\n&6<count> / <total> Players Online\n";
	
	private static final String formatList(Player p, String msg, boolean quit) {
		return ChatColor.translateAlternateColorCodes('&', msg.replace("<player>", p.getName()).replace("<count>", Integer.toString(p.getServer().getOnlinePlayers().size() - (quit ? 1 : 0))).replace("<total>", Integer.toString(p.getServer().getMaxPlayers()) ));
	}

	private static final String formatList(Player p, String msg) {
		return formatList(p, msg, false);
	}

	public static void setScoreboard(Player p) {
		if (pList == null || manager == null || sidebar == null) registerScoreboard();

		PlayerConfig config = new PlayerConfig(p);
		RankData data = RankData.from(p);
		
		Team pTeam = pList.getTeam(data.getWeight() + config.getRank().toLowerCase());
		
		pTeam.addEntry(p.getName());
		config.updateRank();

		// Set Scores
		Objective side = sidebar.getObjective(DisplaySlot.SIDEBAR);
		
		Score kills = side.getScore(ChatColor.GREEN + "Kills: " + p.getStatistic(Statistic.PLAYER_KILLS));
		kills.setScore(0);

		String date = new SimpleDateFormat("hh.mm aa").format(new Date()).toString();
		Score time = side.getScore(ChatColor.YELLOW + date);
		time.setScore(1);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		PlayerConfig config = new PlayerConfig(p);
		e.setQuitMessage(p.getDisplayName() + ChatColor.GREEN + " has left NoobySMP.");
		
		for (Player online : Bukkit.getOnlinePlayers()) {
			online.setPlayerListFooter(formatList(online, BOTTOM_PLAYER_LIST, true));
		}

		new BukkitRunnable() {
			public void run() {
				config.updateItemsInConfig();
			}
		}.runTaskLater(plugin, 3);
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

		setScoreboard(p);
		
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

	@EventHandler
	public void onCraft(PrepareItemCraftEvent e) {
		if (e.isRepair()) return;
		if (e.getRecipe() != null) return;
		
		if (e.getInventory().getMatrix().length < 9) return;

		List<ItemStack> newRecipe = new ArrayList<>();

		for (ItemStack item : e.getInventory().getMatrix()) {
			if (item != null && !(item.getItemMeta().hasDisplayName())) {
				newRecipe.add(new ItemStack(item.getType()));
			} else newRecipe.add(item);
		}

		Recipe r = Bukkit.getCraftingRecipe(newRecipe.toArray(new ItemStack[0]), Bukkit.getWorld("world"));
		if (r == null) return;
		e.getInventory().setResult(r.getResult());
	}
	
}
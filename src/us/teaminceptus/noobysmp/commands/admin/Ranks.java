package us.teaminceptus.noobysmp.commands.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.google.common.collect.ImmutableMap;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Messages;
import us.teaminceptus.noobysmp.util.PlayerConfig;

public class Ranks implements TabExecutor, Listener {

	protected SMP plugin;
	
	// Admin
	public static final RankData OWNER = new RankData(
		ChatColor.DARK_RED + "" + ChatColor.BOLD + "[OWNER] " + ChatColor.GOLD,
		ChatColor.DARK_RED + "" + ChatColor.BOLD + "Owner " + ChatColor.GOLD, 0	
	);

	public static final RankData ADMIN = new RankData(
		ChatColor.RED + "" + ChatColor.BOLD + "[ADMIN] " + ChatColor.YELLOW,
		ChatColor.RED + "" + ChatColor.BOLD + "Admin " + ChatColor.YELLOW, 1
	);

	public static final RankData MOD = new RankData(
		ChatColor.AQUA + "[MOD] " + ChatColor.DARK_AQUA,
		ChatColor.AQUA + "Mod " + ChatColor.DARK_AQUA, 2
	);

	public static final RankData JRMOD = new RankData(
		ChatColor.BLUE + "[JRMOD] " + ChatColor.DARK_BLUE,
		ChatColor.BLUE + "JrMod " + ChatColor.DARK_BLUE, 3
	);

	public static final RankData TRIALMOD = new RankData(
		ChatColor.RED + "[TMOD] " + ChatColor.LIGHT_PURPLE,
		ChatColor.RED + "Trial Mod " + ChatColor.LIGHT_PURPLE, 4
	);

	public static final Map<String, RankData> RANK_MAP = ImmutableMap.<String, RankData>builder()
	.put("owner", OWNER)
	.put("admin", ADMIN)
	.put("mod", MOD)
	.put("jrmod", JRMOD)
	.put("trialmod", TRIALMOD)
	.put("member", RankData.member())
	.build();

	public static final class RankData {
		private final String tab;
		private final String chat;
		private final int weight;
		
		public RankData(String tab, String chat, int weight) {
			this.tab = tab;
			this.chat = chat;
			this.weight = weight;
		}

		public final int getWeight() {
			return this.weight;
		}
		
		public static RankData member() {
			return new RankData("MEMBER", "MEMBER", 5);
		}

		public static RankData from(String rank) {
			return RANK_MAP.get(rank.toLowerCase());
		}

		public static RankData from(Player p) {
			return from(new PlayerConfig(p).getRank());
		}
		
		public boolean isMember() {
			return this.chat.equals("MEMBER") && this.tab.equals("MEMBER");
		}

		public String getTab() {
			return this.tab;
		}

		public String getChat() {
			return this.chat;
		}
	}

	public static final Map<Integer, ChatColor> LEVEL_COLOR = new HashMap<>();

	static {
		// Level Color
		for (int i = 0; i < 5; i++) LEVEL_COLOR.put(i, ChatColor.GRAY);
		for (int i = 5; i < 20; i++) LEVEL_COLOR.put(i, ChatColor.WHITE);
		for (int i = 20; i < 40; i++) LEVEL_COLOR.put(i, ChatColor.YELLOW);
		for (int i = 40; i < 75; i++) LEVEL_COLOR.put(i, ChatColor.GREEN);
		for (int i = 75; i < 100; i++) LEVEL_COLOR.put(i, ChatColor.AQUA);
		for (int i = 100; i < 155; i++) LEVEL_COLOR.put(i, ChatColor.DARK_PURPLE);
		for (int i = 155; i < 200; i++) LEVEL_COLOR.put(i, ChatColor.LIGHT_PURPLE);
		LEVEL_COLOR.put(200, ChatColor.GOLD);
	}
	
	public Ranks(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		plugin.getCommand("setrank").setExecutor(this);
		plugin.getCommand("setrank").setTabCompleter(this);
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

		if (args.length < 1) {
			sender.sendMessage(Messages.ARGUMENT_PLAYER);
			return true;
		}

		if (Bukkit.getPlayer(args[0]) == null) {
			sender.sendMessage(Messages.ARGUMENT_PLAYER);
			return true;
		}

		Player p = Bukkit.getPlayer(args[0]);

		if (args.length < 2) {
			sender.sendMessage("Please provide a valid rank.");
			return true;
		}

		String rank = args[1].toLowerCase();

		if (RANK_MAP.get(rank) == null) {
			sender.sendMessage("Please provide a valid rank.");
			return true;
		}

		RankData data = RANK_MAP.get(rank);
		PlayerConfig config = new PlayerConfig(p);
		if (!(data.isMember())) {
			p.setPlayerListName(data.getTab() + p.getName());
			p.setDisplayName(data.getChat() + p.getName());
			config.setRank(rank);
		} else {
			config.setRank("member");
			config.updateRank();
		}


		
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> suggestions = new ArrayList<>();
		if (args.length == 1) for (Player p : Bukkit.getOnlinePlayers()) suggestions.add(p.getName());
		else if (args.length == 2) suggestions.addAll(RANK_MAP.keySet());
		
		return suggestions;
	}
	
}
package us.teaminceptus.noobysmp.player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import net.minecraft.network.chat.TextComponent;
import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.commands.admin.Ranks;
import us.teaminceptus.noobysmp.commands.admin.Ranks.RankData;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.recipes.SMPRecipe;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.events.PlayerTickEvent;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

public class ServerManager implements Listener {

	private static final int PLAYER_UPDATE_SPEED = 60;
	private static final int BROADCAST_SPEED = 20 * 80;

	protected SMP plugin;
	public static Scoreboard noobysmp;
	public static ScoreboardManager manager;
	
	public static final String PACK_URL = "https://github.com/Team-Inceptus/SMPPack/raw/master/SMPPack.zip";
	public static final String[] BROADCAST_STRINGS = {
		ChatColor.BLUE + "Bosses can be summoned with /bosses!",
		ChatColor.GOLD + "Thank you for playing on NoobySMP!",
		ChatColor.GREEN + "This server is currently in beta. Report bugs to our discord at: https://discord.io/thenoobygods",
		ChatColor.BLUE + "Join our Discord: https://discord.io/thenoobygods",
		ChatColor.DARK_AQUA + "Check out our Source Code at: https://github.com/Team-Inceptus/NoobySMP",
		ChatColor.RED + "Subscribe to Team Inceptus: https://www.youtube.com/channel/UCKYvVHwoYgGFt6GUzPvryBg",
		ChatColor.AQUA + "You can research recipes with /getreicpe!",
		ChatColor.LIGHT_PURPLE + "Learn about different items with /query!",
		ChatColor.DARK_PURPLE + "See your progression with /progress!"
	};

	private static void registerScoreboard() {
		ScoreboardManager m = Bukkit.getScoreboardManager();
		
		Scoreboard noobysmp = m.getNewScoreboard();
		Objective side = noobysmp.registerNewObjective("sidebar", "dummy",
		ChatColor.DARK_GREEN + "----- NoobySMP -----");
		side.setDisplaySlot(DisplaySlot.SIDEBAR);

		Objective health = noobysmp.registerNewObjective("hp", Criterias.HEALTH, "Health");
		health.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		health.setRenderType(RenderType.INTEGER);
		
		for (Map.Entry<String, RankData> entry : Ranks.RANK_MAP.entrySet()) {
			String name = entry.getKey();
			String weight = "" + entry.getValue().getWeight();
			
			noobysmp.registerNewTeam(weight + name);
		}

		ServerManager.noobysmp = noobysmp;
		manager = m;
	}
	
	
	public static final BukkitRunnable UPDATE_SCOREBOARD_TASK = new BukkitRunnable() {
		public void run() {
			for (Player p : Bukkit.getOnlinePlayers()) {
				setScoreboard(p);
			}
		}
	};
	
	public static final BukkitRunnable PLAYER_TICK_TASK = new BukkitRunnable() {
		public void run() {
			for (Player p : Bukkit.getOnlinePlayers()) {
				Bukkit.getPluginManager().callEvent(new PlayerTickEvent(p));
			}
		}
	};

	public static final BukkitRunnable TIPS_BROADCAST_TASK = new BukkitRunnable() {
		public void run() {
			Bukkit.broadcastMessage(BROADCAST_STRINGS[r.nextInt(BROADCAST_STRINGS.length)]);
		}
	};

	private static final Random r = new Random();

	public static final BukkitRunnable PLAYER_UPDATES_TASK = new BukkitRunnable() {
		public void run() {
			for (Player p : Bukkit.getOnlinePlayers()) {
				PlayerConfig config = new PlayerConfig(p);

				for (Attribute a : Attribute.values()) if (p.getAttribute(a) != null && p.getAttribute(a).getBaseValue() != config.getAttribute(a)) {
					p.getAttribute(a).setBaseValue(config.getAttribute(a));
				}
			}
		}
	};

	public static final String parseDeathMessage(DamageCause c, Entity damager) {
		String name = damager instanceof Player p ? p.getDisplayName() : (damager.getCustomName() == null ? damager.getName() : damager.getCustomName());
		switch (c) {
			case ENTITY_SWEEP_ATTACK:
			case ENTITY_ATTACK: {
				return "was killed by " + name;
			}
			case THORNS: {
				return "was prickled by " + name;
			}
			case FALLING_BLOCK: {
				return "was squished by " + name;
			}
			case PROJECTILE: {
				return "was shot by " + (damager instanceof Projectile proj ? (proj.getShooter() instanceof Player p ? p.getDisplayName() : ((Entity) proj.getShooter()).getCustomName() == null ? ((Entity) proj.getShooter()).getName() : ((Entity) proj.getShooter()).getCustomName()) : name);
			}
			default: {
				return parseDeathMessage(c);
			}
		}
	}

	public static final String parseDeathMessage(DamageCause c) {
		switch (c) {
			case ENTITY_EXPLOSION:
			case BLOCK_EXPLOSION: {
				return "blew up";
			}
			case CONTACT: {
				return "died when touched";
			}
			case CRAMMING: {
				return "was squished too hard";
			}
			case CUSTOM: {
				return "died in a unique way";
			}
			case DRAGON_BREATH: {
				return "couldn't handle the Dragon's Breath";
			}
			case DROWNING: {
				return "ran out of oxygen";
			}
			case DRYOUT: {
				return "was killed by water";
			}
			case FALL: {
				return "broke their legs";
			}
			case FIRE:
			case FIRE_TICK:
			case HOT_FLOOR:
			case LAVA: {
				return "burned to death";
			}
			case FLY_INTO_WALL: {
				return "got a concussion";
			}
			case FREEZE: {
				return "got frostbite";
			}
			case LIGHTNING: {
				return "was electrocuted";
			}
			case MAGIC: {
				return "was killed by magic";
			}
			case MELTING: {
				return "melted";
			}
			case POISON: {
				return "was poisoned";
			}
			case STARVATION: {
				return "died due to starvation";
			}
			case SUFFOCATION: {
				return "suffocated to death";
			}
			case SUICIDE: {
				return "committed die";
			}
			case VOID: {
				return "got yeeted out of existence";
			}
			case WITHER: {
				return "got sick and died from wither";
			}
			default: {
				return "died";
			}
		}
	}

	public ServerManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		plugin.getLogger().info("Registering Scoreboard...");
		registerScoreboard();
		plugin.getLogger().info("Successfully registered NoobySMP Scoreboard! Creating Tasks...");
		
		UPDATE_SCOREBOARD_TASK.runTaskTimer(plugin, 20, 20);
		PLAYER_TICK_TASK.runTaskTimer(plugin, 1, 1);
		PLAYER_UPDATES_TASK.runTaskTimer(plugin, PLAYER_UPDATE_SPEED, PLAYER_UPDATE_SPEED);
		TIPS_BROADCAST_TASK.runTaskTimer(plugin, BROADCAST_SPEED, BROADCAST_SPEED);

		plugin.getLogger().info("Tasks Created Successfully!");
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
	
	private static void setResourcePack(Player p) {
			((CraftPlayer) p).getHandle().sendTexturePack(PACK_URL, "6FF5086013892BAA1160128BA9295A2615D551D4", false, new TextComponent("NoobySMP has a resource pack that may help items render better. This is not required to accept. It will not override (most) existing textures."));
	}

	public static void setScoreboard(Player p) {
		if (noobysmp == null || manager == null) registerScoreboard();

		PlayerConfig config = new PlayerConfig(p);
		RankData data = RankData.from(p);
		
		Team pTeam = noobysmp.getTeam(data.getWeight() + config.getRank().toLowerCase());
		
		pTeam.addEntry(p.getName());
		config.updateRank();
		
		// Reset Objective
		noobysmp.getObjective(DisplaySlot.SIDEBAR).unregister();
		
		Objective side = noobysmp.registerNewObjective("sidebar", "dummy",
		ChatColor.DARK_GREEN + "----- NoobySMP -----");
		side.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		// Set Scores
		
		Score rank = side.getScore(ChatColor.GREEN + "Rank: " + ChatColor.YELLOW + config.getRank().toUpperCase());
		rank.setScore(2);

		Score space1 = side.getScore(" ");
		space1.setScore(1);
		
		Date time = new Date(p.getPlayerTime());
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm a");

		Score timescore = side.getScore(ChatColor.DARK_GRAY + format.format(time));
		timescore.setScore(0);

		p.setScoreboard(noobysmp);
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
		
		setScoreboard(p);
		setResourcePack(p);
		
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
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		PlayerConfig config = new PlayerConfig(p);

		if (config.isAFK()) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are currently AFK. Do /afk to toggle.");
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		DamageCause c = p.getLastDamageCause().getCause();

		if (p.getKiller() != null) {
			e.setDeathMessage(p.getDisplayName() + " " + ChatColor.GREEN + parseDeathMessage(c, p.getKiller()));
		} else {
			e.setDeathMessage(p.getDisplayName() + " " + ChatColor.GREEN + parseDeathMessage(c));
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		PlayerConfig config = new PlayerConfig(p);

		if (config.isMuted()) {
			p.sendMessage(ChatColor.RED + "You are currently muted.");
			e.setCancelled(true);
			return;
		}

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
		
		if (!(e.getViewers().get(0) instanceof Player p)) return;
		PlayerConfig config = new PlayerConfig(p);

		CraftingInventory inv = e.getInventory();

		if (inv.getMatrix().length < 9) return;

		List<ItemStack> newRecipe = new ArrayList<>();

		for (ItemStack item : inv.getMatrix()) {
			if (item != null && !(item.getItemMeta().hasDisplayName())) {
				newRecipe.add(new ItemStack(item.getType()));
			} else newRecipe.add(item);
		}

		Recipe r = Bukkit.getCraftingRecipe(newRecipe.toArray(new ItemStack[0]), Bukkit.getWorld("world"));
		if (r == null) return;

		for (SMPRecipe rec : SMPRecipe.getByResult(r.getResult())) {
			if (SMPMaterial.getByItem(rec.getResult()) != null && SMPMaterial.getByItem(rec.getResult()).getLevelUnlocked() > config.getLevel()) {
				inv.setResult(Items.LOCKED_ITEM);
				return;
			}

			if (AbilityItem.getByItem(rec.getResult()) != null && AbilityItem.getByItem(rec.getResult()).getLevelUnlocked() > config.getLevel()) {
				inv.setResult(Items.LOCKED_ITEM);
				return;
			}
		}

		inv.setResult(r.getResult());
	}
	
}
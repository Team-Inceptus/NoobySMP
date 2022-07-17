package us.teaminceptus.noobysmp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import us.teaminceptus.noobysmp.ability.AbilityManager;
import us.teaminceptus.noobysmp.ability.cosmetics.Cosmetics;
import us.teaminceptus.noobysmp.commands.Bosses;
import us.teaminceptus.noobysmp.commands.GetRecipe;
import us.teaminceptus.noobysmp.commands.Help;
import us.teaminceptus.noobysmp.commands.MinorCommands;
import us.teaminceptus.noobysmp.commands.PlayerInfo;
import us.teaminceptus.noobysmp.commands.Progress;
import us.teaminceptus.noobysmp.commands.Query;
import us.teaminceptus.noobysmp.commands.Settings;
import us.teaminceptus.noobysmp.commands.TPRequests;
import us.teaminceptus.noobysmp.commands.admin.Ban;
import us.teaminceptus.noobysmp.commands.admin.Catalogue;
import us.teaminceptus.noobysmp.commands.admin.Ecsee;
import us.teaminceptus.noobysmp.commands.admin.Experience;
import us.teaminceptus.noobysmp.commands.admin.FetchData;
import us.teaminceptus.noobysmp.commands.admin.Invsee;
import us.teaminceptus.noobysmp.commands.admin.Ranks;
import us.teaminceptus.noobysmp.commands.admin.RunTest;
import us.teaminceptus.noobysmp.commands.admin.Suspend;
import us.teaminceptus.noobysmp.commands.admin.Tags;
import us.teaminceptus.noobysmp.conquest.ConquestManager;
import us.teaminceptus.noobysmp.entities.EntityManager;
import us.teaminceptus.noobysmp.entities.bosses.BossManager;
import us.teaminceptus.noobysmp.entities.bosses.npc.NPCManager;
import us.teaminceptus.noobysmp.generation.BlockManager;
import us.teaminceptus.noobysmp.generation.ItemManager;
import us.teaminceptus.noobysmp.generation.WorldManager;
import us.teaminceptus.noobysmp.generation.biomes.TitanBiome;
import us.teaminceptus.noobysmp.generation.structures.StructureManager;
import us.teaminceptus.noobysmp.inventory.PlayerMenu;
import us.teaminceptus.noobysmp.leveling.LevelingManager;
import us.teaminceptus.noobysmp.leveling.trades.DailyTrades;
import us.teaminceptus.noobysmp.materials.TagsManager;
import us.teaminceptus.noobysmp.player.ServerManager;
import us.teaminceptus.noobysmp.recipes.RecipeManager;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.ServerConfig;

public class SMP extends JavaPlugin {

	private static final int FILE_UPDATE_SPEED_TICKS = 20 * 30;
	private static File playerDir;

	public void loadFiles() {
		// Server Config
		saveDefaultConfig();
		FileConfiguration config = getConfig();
		
		if (!(config.isBoolean("maintenance"))) {
			config.set("maintenance", false);
		}

		saveConfig();

		new ServerConfig(this);
		// Player Config
		playerDir = new File(getDataFolder(), "players");

		if (!(playerDir.exists())) playerDir.mkdir();
		
		for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
			File pFile = new File(playerDir, p.getUniqueId() + ".yml");
			if (!(pFile.exists())) {
				try {
					pFile.createNewFile();
				} catch (IOException e) {
					getLogger().info("Error loading player file");
					e.printStackTrace();
				}
			}

			FileConfiguration pConfig = YamlConfiguration.loadConfiguration(pFile);

			// Load Data
			if (!(pConfig.isString("name"))) {
				pConfig.set("name", p.getName());
			}

			if (!(pConfig.isBoolean("op"))) {
				pConfig.set("op", p.isOp());
			}

			if (!(pConfig.isString("rank"))) {
				pConfig.set("rank", "member");
			}

			if (!(pConfig.isList("friends"))) {
				pConfig.set("friends", new ArrayList<OfflinePlayer>());
			}

			if (!(pConfig.isBoolean("muted"))) {
				pConfig.set("muted", false);
			}

			if (!(pConfig.isBoolean("afk"))) {
				pConfig.set("afk", false);
			}

			if (!(pConfig.isLocation("home"))) {
				pConfig.set("home", Bukkit.getWorld("world").getSpawnLocation());
			}
			
			if (!(pConfig.isConfigurationSection("statistics"))) {
				pConfig.createSection("statistics");
			}

			ConfigurationSection statistics = pConfig.getConfigurationSection("statistics");

			if (!(statistics.isInt("level"))) {
				statistics.set("level", 0);
			}

			if (!(statistics.isDouble("experience"))) {
				statistics.set("experience", 0.0D);
			}

			if (!(statistics.isInt("farming-level"))) {
				statistics.set("farming-level", 0);
			}
			
			if (!(statistics.isInt("fletching-level"))) {
				statistics.set("flecting-level", 0);
			}

			if (!(pConfig.isConfigurationSection("settings"))) {
				pConfig.createSection("settings");
			}

			ConfigurationSection settings = pConfig.getConfigurationSection("settings");

			if (!(settings.isBoolean("notifications"))) {
				settings.set("notifications", true);
			}
			
			if (!(settings.isBoolean("drop_items"))) {
				settings.set("drop_items", true);
			}
			
			if (!(settings.isBoolean("tag_abilities"))) {
				settings.set("tag_abilities", true);
			}
			
			if (!(settings.isBoolean("speed"))) {
				settings.set("speed", true);
			}
			
			if (!(pConfig.isConfigurationSection("information"))) {
				pConfig.createSection("information");
			}
			
			ConfigurationSection info = pConfig.getConfigurationSection("information");
			
			if (!(info.isLong("last-played"))) {
				info.set("last-played", 0);
			}
			
			if (!(info.isConfigurationSection("items"))) {
				info.createSection("items");
			}
			
			ConfigurationSection items = info.getConfigurationSection("items");
			
			for (EquipmentSlot s : EquipmentSlot.values()) {
				if (!(items.isItemStack(s.name().toLowerCase()))) {
					items.set(s.name().toLowerCase(), new ItemStack(Material.AIR));
				}
			}
			
			try {
				pConfig.save(pFile);
			} catch (IOException e) {
				getLogger().info("Error saving player file");
				e.printStackTrace();
			}
		}
	}
	
	public static final BukkitRunnable UPDATE_TASK = new BukkitRunnable() {
		public void run() {
			PlayerConfig.updateAllItems();
		}
	};
	
	private void startTasks() {
		UPDATE_TASK.runTaskTimer(this, 0, FILE_UPDATE_SPEED_TICKS);
	}
	
	public void onEnable() {
		getLogger().info("Loading files...");
		loadFiles();
		getLogger().info("Files successfully loaded! Loading Commands...");
		
		// User Commands
		new Help(this);
		new Settings(this);
		new PlayerInfo(this);
		new Progress(this);
		new Bosses(this);
		new Cosmetics(this);
//		new Trade(this);
		new Query(this);
		new GetRecipe(this);
		new TPRequests(this);
		new MinorCommands(this);
		new DailyTrades(this);
		new PlayerMenu(this);
		// Admin Commands
		new Ranks(this);
		new Catalogue(this);
		new Experience(this);
//		new SetBiome(this);
		new RunTest(this);
		new FetchData(this);
		new Tags(this);
		new Ban(this);
		new Suspend(this);
		new Invsee(this);
		new Ecsee(this);
		
		getLogger().info("Loading Managers...");
		// Managers
		new EntityManager(this);
		new BossManager(this);
		new NPCManager(this);
		
		new ServerManager(this);
		new BlockManager(this);
		new ConquestManager(this);
		new RecipeManager(this);
		new AbilityManager(this);
		new ItemManager(this);
		new TagsManager(this);
		
		new Items(this);
		
		new LevelingManager(this);
//		new TradesManager(this);
//		new TradeCommandManager(this);
		
		getLogger().info("Loading World Managers...");
		new WorldManager(this);
		new StructureManager(this);

		getLogger().info("Successfully loaded Classes! Loading external worlds...");
		try {
			TitanBiome.registerBiomes();
		} catch (Exception e) {
			getLogger().info("Error registering biomes");
			e.printStackTrace();
		}
		
//		WorldCreator titan = new WorldCreator("world_titan");
//		Bukkit.createWorld(titan);
//		new TitanManager(this);
		
		getLogger().info("Successfylly loaded worlds! Loading tasks...");
		startTasks();
		getLogger().info("Sucessfully loaded tasks!");
		getLogger().info("Done!");
	}

	public static FileConfiguration getConfig(OfflinePlayer p) {
		File file = new File(playerDir, p.getUniqueId() + ".yml");
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public static File getFile(OfflinePlayer p) {
		return new File(playerDir, p.getUniqueId() + ".yml");
	}
	
}
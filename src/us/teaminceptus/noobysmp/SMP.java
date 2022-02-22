package us.teaminceptus.noobysmp;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import us.teaminceptus.noobysmp.ability.AbilityManager;
import us.teaminceptus.noobysmp.commands.Help;
import us.teaminceptus.noobysmp.commands.Settings;
import us.teaminceptus.noobysmp.commands.admin.Catalogue;
import us.teaminceptus.noobysmp.commands.admin.Ranks;
import us.teaminceptus.noobysmp.conquest.ConquestManager;
import us.teaminceptus.noobysmp.entities.EntityManager;
import us.teaminceptus.noobysmp.generation.BlockManager;
import us.teaminceptus.noobysmp.generation.ItemManager;
import us.teaminceptus.noobysmp.player.ServerManager;
import us.teaminceptus.noobysmp.recipes.RecipeManager;

public class SMP extends JavaPlugin {

	private static File playerDir;

	private void loadFiles() {
		playerDir = new File(getDataFolder(), "players");

		if (!(playerDir.exists())) playerDir.mkdir();
		
		for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
			File pFile = new File(playerDir, p.getUniqueId().toString() + ".yml");
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
			
			try {
				pConfig.save(pFile);
			} catch (IOException e) {
				getLogger().info("Error saving player file");
				e.printStackTrace();
			}
		}
	}
	
	public void onEnable() {
		getLogger().info("Loading files...");
		loadFiles();
		getLogger().info("Files successfully loaded! Loading Commands...");
		
		// User Commands
		new Help(this);
		new Settings(this);
		// Admin Commands
		new Ranks(this);
		new Catalogue(this);
		
		getLogger().info("Loading Managers...");
		// Managers
		new EntityManager(this);
		new ServerManager(this);
		new BlockManager(this);
		new ConquestManager(this);
		new RecipeManager(this);
		new AbilityManager(this);
		new ItemManager(this);
		
		getLogger().info("Successfully loaded Classes!");
		
		
		getLogger().info("Done!");
	}

	public static FileConfiguration getConfig(OfflinePlayer p) {
		File file = new File(playerDir, p.getUniqueId().toString() + ".yml");
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public static File getFile(OfflinePlayer p) {
		File file = new File(playerDir, p.getUniqueId().toString() + ".yml");
		return file;
	}
	
}
package us.teaminceptus.noobysmp;

public class SMP extends JavaPlugin {

	private static final File playerDir;

	private void loadFiles() {
		playerDir = new File(getDataFolder().getPath() + "/players");

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

			if (!(statistics.isInt("farming-count"))) {
				statistics.set("farming-count", 0);
			}

			if (!(pConfig.isConfigurationSection("settings"))) {
				pConfig.createSection("settings");
			}

			ConfigurationSection settings = pConfig.getConfigurationSection("settings");

			if (!(settings.isBoolean("notifications")) {
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
		getLogger().info("Files successfully loaded! Loading classes...");
		// TODO add all command + listener classes
	}

	public static FileConfiguration getConfig(OfflinePlayer p) {
		File file = new File(playerDir, p.getUniqueId().toString() + ".yml");
		return YamlConfiguration.loadConfiguration(file);
	}
	
}
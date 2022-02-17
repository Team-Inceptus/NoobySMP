package us.teaminceptus.noobysmp.util;

import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

public class PlayerConfig {

	private final OfflinePlayer p;
	private final FileConfiguration pConfig;

	private static final double EXP_POWER = 2.2D;
	
	public PlayerConfig(OfflinePlayer p) {
		this.p = p;
		this.pConfig = SMP.getConfig(p);
	}

	public static int toLevel(double exp) {
		return (int) Math.floor(Math.pow(exp / 800, 1 / EXP_POWER));
	}

	public static double toMinExperience(int level) {
		return Math.floor(800 * Math.pow(level, EXP_POWER));
	}

	public boolean getSetting(String setting) {
		return pConfig.getConfigurationSection("settings").getBoolean(ChatColor.stripColor(setting).toLowerCase());
	}

	public void setSetting(String setting, boolean value) {
		pConfig.getConfigurationSection("settings").set(ChatColor.stripColor(setting).toLowerCase(), value);
		try {
			pConfig.save(SMP.getFile(p));
		}	catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double getExpToNextLevel() {
		return toMinExperience(getLevel() + 1) - getExperience(); 
	}

	public int getLevel() {
		return pConfig.getConfigurationSection("statistics").getInt("level");
	}

	public double getExperience() {
		return pConfig.getConfigurationSection("statistics").getDouble("experience");
	}

	public void setRank(String value) {
		pConfig.set("rank", value);
	}

	public final boolean hasUnlocked(SMPMaterial item) {
		return getLevel() >= item.getLevelUnlocked();
	}

	public boolean isMember() {
		return pConfig.getString("rank").equalsIgnoreCase("member");	
	}
	
}
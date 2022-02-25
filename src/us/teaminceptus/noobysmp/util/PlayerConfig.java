package us.teaminceptus.noobysmp.util;

import java.io.File;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

public class PlayerConfig {

	private final OfflinePlayer p;
	private final FileConfiguration pConfig;
	private final File pFile;

	private static final double EXP_POWER = 2.2D;
	
	public PlayerConfig(OfflinePlayer p) {
		this.p = p;
		this.pConfig = SMP.getConfig(p);
		this.pFile = SMP.getFile(p);
	}

	public void saveFile() {
		try {
			pConfig.save(pFile);
		} catch (Exception e) {
			JavaPlugin.getPlugin(SMP.class).getLogger().info("Error saving file " + pConfig.getName());
			e.printStackTrace();;
		}
	}

	public OfflinePlayer getPlayer() {
		return this.p;
	}

	public File getFile() {
		return this.pFile;
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
		saveFile();
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

	public void setLevel(int level) {
		double xp = Math.floor(toMinExperience(level) * 100) / 100;

		pConfig.getConfigurationSection("statistics").set("level", level);
		pConfig.getConfigurationSection("statistics").set("experience", xp);
		saveFile();

		if (getSetting("notifications") && p.isOnline()) {
			Player op = p.getPlayer();
			op.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "+" + Double.toString(xp) + " Experience"));
		}
	}

	public void setExperience(double exp) {
		pConfig.getConfigurationSection("statistics").set("experience", exp);
		pConfig.getConfigurationSection("statistics").set("level", toLevel(exp));
		saveFile();

		if (getSetting("notifications") && p.isOnline()) {
			Player op = p.getPlayer();
			op.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "+" + Double.toString(exp) + " Experience"));
		}
	}

	public void addExperience(double exp) {
		setExperience(getExperience() + exp);
	}

	public void addLevel(int level) {
		setLevel(getLevel() + level);
	}

	public void setRank(String value) {
		pConfig.set("rank", value);
		saveFile();
	}
	
	public int getFletchingLevel() {
		return pConfig.getConfigurationSection("statistics").getInt("fletching-level");
	}

	public void setFletchingLevel(int level) {
		pConfig.getConfigurationSection("statistics").set("fletching-level", level);
		saveFile();
	}

	public void incrementFletchingLevel() {
		incrementFletchingLevel(1);
	}

	public void incrementFletchingLevel(int amount) {
		setFletchingLevel(getFletchingLevel() + amount);
	}
	
	public int getFarmingLevel() {
		return pConfig.getConfigurationSection("statistics").getInt("farming-level");
	}

	public void setFarmingLevel(int level) {
		pConfig.getConfigurationSection("statistics").set("farming-level", level);
		saveFile();
	}

	public void incrementFarmingLevel() {
		incrementFarmingLevel(1);
	}

	public void incrementFarmingLevel(int amount) {
		setFarmingLevel(getFarmingLevel() + amount);
	}

	// Other Statistics & Void Methods

	public int getFarmingFind() {
		return (int) Math.floor(getFarmingLevel() / 15) * 10;
	}

	public int getHarvestDiameter() {
		return (int) (Math.floor(getFarmingLevel() / 30) * 2) + 1;
	}

	/**
	 * Method to calculate Hoe Damage
	 * @param damage Damage to calculate
	 * @since 1.0.0
	 * @return Damage calculated from Farming Level
	 */
	public double calculateHoeDamage(double damage) {
		return damage * (1 + (Math.floor(getFarmingLevel() / 10) / 10));
	}

	private Material[] HOES = {
		Material.WOODEN_HOE,
		Material.STONE_HOE,
		Material.IRON_HOE,
		Material.GOLDEN_HOE,
		Material.DIAMOND_HOE,
		Material.NETHERITE_HOE
	};

	/**
	 * Utility method to check the weapon as well.
	 * @param weapon Weapon to check
	 * @param damage Original Damage (NOT FINAL)
	 * @return Calculated Damage (if hoe)
	 */
	public double calculateHoeDamage(ItemStack weapon, double damage) {
		return (Arrays.asList(HOES).contains(weapon.getType()) ? calculateHoeDamage(damage) : damage);	
	}

	// Boolean Methods

	public final boolean hasUnlocked(SMPMaterial item) {
		return getLevel() >= item.getLevelUnlocked();
	}

	public boolean isMember() {
		return pConfig.getString("rank").equalsIgnoreCase("member");	
	}
	
}
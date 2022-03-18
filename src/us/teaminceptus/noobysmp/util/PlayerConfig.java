package us.teaminceptus.noobysmp.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.ability.cosmetics.SMPCosmetic;
import us.teaminceptus.noobysmp.commands.admin.Ranks;
import us.teaminceptus.noobysmp.leveling.LevelingManager.LevelingType;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.recipes.SMPRecipe;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

/**
 * A Built-In wrapper for accessing Player Information via their Configuration File
 * @author GamerCoder215
 * @since 1.0.0
 */
public class PlayerConfig {

	private final OfflinePlayer p;
	private final FileConfiguration pConfig;
	private final File pFile;
	
	private SMPCosmetic activeCosmetic;

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

	public void sendNotification(String message) {
		if (getSetting("notifications") && p.isOnline()) {
			p.getPlayer().sendMessage(message);
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

	public void setLevel(int level) {
		if (level == getLevel()) return;
		
		if (level == 0) {
			pConfig.getConfigurationSection("statistics").set("level", 0);
			pConfig.getConfigurationSection("statistics").set("experience", 0);
			saveFile();
			
			if (p.isOnline()) {
				Player op = p.getPlayer();
				updateRank();
				
				if (getSetting("notifications")) {
					op.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_AQUA + "Reset Experience and Level"));
				}
			}
			return;
		}
		
		double xp = Math.floor(toMinExperience(level) * 100) / 100;
		double add = xp - getExperience();
		
		pConfig.getConfigurationSection("statistics").set("level", level);
		pConfig.getConfigurationSection("statistics").set("experience", xp);
		saveFile();
		
		if (p.isOnline()) {
			Player op = p.getPlayer();
			updateRank();
			if (getSetting("notifications")) {
				sendNotification(ChatColor.GOLD + "Your level has changed to level " + ChatColor.YELLOW + getLevel() + ChatColor.GOLD + "!");
			}
			
			for (SMPRecipe r : SMPRecipe.getRecipes()) {
				if (r.getKey() != null && r.getResult() instanceof Unlockable l && new PlayerConfig(p).getLevel() <= l.getLevelUnlocked()) {
					op.discoverRecipe(r.getKey());
				}
			}
			
			if (getSetting("notifications")) {
				op.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent((add < 0 ? ChatColor.RED + "" : ChatColor.GREEN + "+") + Double.toString(Math.floor(add * 100) / 100) + " Experience"));
			}
		}
	}

	/**
	 * Will silently fail if player is offline or is not a member.
	 */
	public void updateRank() {
		if (!(p.isOnline())) return;
		if (!(isMember())) return;
		Player op = p.getPlayer();
		
		ChatColor namePrefix = (getLevel() < 10 ? ChatColor.GRAY : ChatColor.WHITE);
		ChatColor levelPrefix = (getLevel() > 200 ? ChatColor.GOLD : Ranks.LEVEL_COLOR.get(getLevel()));
		op.setDisplayName(levelPrefix + (getLevel() > 100 ? ChatColor.BOLD + "" : "") + "[" + getLevel() + "] " + ChatColor.RESET + namePrefix + p.getName());
		op.setPlayerListName(levelPrefix + p.getName());
	}

	public void setExperience(double exp) {
		boolean changeRank = toLevel(exp) != getLevel();
		
		double add = exp - getExperience();
		
		pConfig.getConfigurationSection("statistics").set("experience", exp);
		pConfig.getConfigurationSection("statistics").set("level", toLevel(exp));
		saveFile();
		
		if (p.isOnline()) {
			Player op = p.getPlayer();
			
			if (changeRank) {
				updateRank();
				
				if (getSetting("notifications")) {
					sendNotification(ChatColor.GOLD + "Your level has changed to level " + ChatColor.YELLOW + getLevel() + ChatColor.GOLD + "!");
				}
				
				for (SMPRecipe r : SMPRecipe.getRecipes()) {
					if (r.getKey() != null && r.getResult() instanceof Unlockable l && new PlayerConfig(p).getLevel() <= l.getLevelUnlocked()) {
						op.discoverRecipe(r.getKey());
					}
				}
				
			}
			
			if (getSetting("notifications")) {
				op.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent((add < 0 ? ChatColor.RED + "" : ChatColor.GREEN + "+") + Double.toString(Math.floor(add * 100) / 100) + " Experience"));
			}
		}
	}

	public void addExperience(double exp) {
		setExperience(getExperience() + exp);
	}

	/**
	 * Will return -1 if the player has a level above 100
	 */
	public int getTagLimit() {
		if (getLevel() >= 100) return -1;

		return 3 + (int) (Math.floor(getLevel() / 10));
	}

	public boolean isAFK() {
		return pConfig.getBoolean("afk");
	}

	public void setAFK(boolean afk) {
		pConfig.set("afk", afk);
		saveFile();
	}

	public void toggleAFK() {
		if (isAFK()) setAFK(false);
		else setAFK(true);
	}

	public void updateAllAttributes() {
		for (Attribute a : Attribute.values()) setAttribute(a);
	}

	public void setAttribute(Attribute a) {
		if (!(p.isOnline())) return;

		p.getPlayer().getAttribute(a).setBaseValue(Math.min(getAttribute(a), Integer.MAX_VALUE));
	}

	public double getAttribute(Attribute a) {
		switch (a) {
			case GENERIC_ARMOR: {
				return Math.min(( getLevel() + getFletchingLevel() ) / 5, 500);
			}
			case GENERIC_ARMOR_TOUGHNESS: {
				return ( getLevel() + getFarmingLevel() ) / 7;
			}
			case GENERIC_ATTACK_DAMAGE: {
				return Math.min( Math.max((getLevel() + 2) / 8, 1), 500);
			}
			case GENERIC_ATTACK_SPEED: {
				return Math.min( Math.max(( getLevel() + 1) / 10, 4), 20);
			}
			case GENERIC_MOVEMENT_SPEED: {
				return Math.min(  Math.max((getLevel() + 5) / 15, 0.1), 2);
			}
			case GENERIC_MAX_HEALTH: {
				return Math.min( (getLevel() / 5) + 20, 80);
			}
			case GENERIC_KNOCKBACK_RESISTANCE: {
				if (getLevel() >= 45) return 1;
				else return (getLevel() / 45);
			}
			case GENERIC_LUCK: {
				return getFarmingLevel();
			}
			default: {
				return 0;
			}
		}
	}
	
	private PlayerConfig config = this;
	
	private BukkitRunnable cosmeticTask = new BukkitRunnable() {
		public void run() {
			try {
				if (!(p.isOnline())) cancel();
				Player op = p.getPlayer();
				config.activeCosmetic.createEffect(op.getLocation().add(0, 0.5, 0));
			} catch (NullPointerException e) {
				cancel();
			}
		}
	};
	
	public void updateCosmetic() {
		try {
			if (this.cosmeticTask == null) {
				this.cosmeticTask = new BukkitRunnable() {
					public void run() {
						try {
							if (!(p.isOnline())) cancel();
							Player op = p.getPlayer();
							config.activeCosmetic.createEffect(op.getLocation().add(0, 0.5, 0));
						} catch (NullPointerException e) {
							cancel();
						}
					}
				};
				return;
			}
			if (this.activeCosmetic == null) {
				this.cosmeticTask.cancel();
				return;
			}
			
			if (!(this.cosmeticTask.isCancelled())) this.cosmeticTask.cancel();
			else {
				this.cosmeticTask.cancel();
				this.cosmeticTask.runTaskTimer(JavaPlugin.getPlugin(SMP.class), 1, 1);
			}
		} catch (IllegalStateException e) {
			this.cosmeticTask.runTaskTimer(JavaPlugin.getPlugin(SMP.class), 1, 1);
		}
	}
	
	public void cancelCosmetic() {
		this.cosmeticTask = null;
		updateCosmetic();
	}
	
	/**
	 * This method does NOT do level checking. Please add that yourself.
	 * @param c Cosmetc to use
	 */
	public void setActiveCosmetic(SMPCosmetic c) {
		this.activeCosmetic = c;
		updateCosmetic();
	}

	public void addLevel(int level) {
		setLevel(getLevel() + level);
	}

	public void setRank(String value) {
		pConfig.set("rank", value);
		saveFile();

		if (p.isOnline()) {
			new BukkitRunnable() {
				public void run() {
					p.getPlayer().updateCommands();
				}
			}.runTask(JavaPlugin.getPlugin(SMP.class));
		}
	}

	public boolean isMuted() {
		return pConfig.getBoolean("muted");
	}

	/**
	 * Will return Overworld Spawn Location if not set
	 * @return Home Location
	 */
	public Location getHome() {
		return pConfig.getLocation("home");
	}

	public void setHome(Location home) {
		pConfig.set("home", home);
		saveFile();
	}

	public void setMuted(boolean muted) {
		pConfig.set("muted", muted);
		saveFile();
	}
	
	public static void updateAllItems() {
		for (Player p : Bukkit.getOnlinePlayers()) new PlayerConfig(p).updateItemsInConfig();
	}
	
	public String getRank() {
		return pConfig.getString("rank");
	}
	
	public int getLevel(LevelingType type) {
		switch (type) {
			case FARMING:
				return getFarmingLevel();
			case FLETCHING:
				return getFletchingLevel();
			case LEVEL:
				return getLevel();
			default:
				return getLevel();
		}
	}
	
	/**
	 * Get the display name of the player.
	 * @return display name of player, or regular name if member
	 */
	public String getDisplayName() {
		if (isMember()) return p.getName();
		
		return Ranks.RANK_MAP.get(getRank()).getChat() + p.getName() + ChatColor.RESET;
	}
	
	public Inventory getPlayerInfo() {
		Inventory inv = Generator.genGUI(54, getDisplayName() + "'s Info", new CancelHolder());
		
		ItemStack helmet = (p.isOnline() ? p.getPlayer().getEquipment().getItem(EquipmentSlot.HEAD) : getItemFromConfig(EquipmentSlot.HEAD));
		ItemStack chestplate = (p.isOnline() ? p.getPlayer().getEquipment().getItem(EquipmentSlot.CHEST) : getItemFromConfig(EquipmentSlot.CHEST));
		ItemStack leggings = (p.isOnline() ? p.getPlayer().getEquipment().getItem(EquipmentSlot.LEGS) : getItemFromConfig(EquipmentSlot.LEGS));
		ItemStack boots = (p.isOnline() ? p.getPlayer().getEquipment().getItem(EquipmentSlot.FEET) : getItemFromConfig(EquipmentSlot.FEET));
		ItemStack mainhand = (p.isOnline() ? p.getPlayer().getEquipment().getItem(EquipmentSlot.HAND) : getItemFromConfig(EquipmentSlot.HAND));
		ItemStack offhand = (p.isOnline() ? p.getPlayer().getEquipment().getItem(EquipmentSlot.OFF_HAND) : getItemFromConfig(EquipmentSlot.OFF_HAND));
		
		for (int i = 10; i < 44; i++) inv.setItem(i, Items.Inventory.GUI_PANE);
		
		inv.setItem(10, helmet);
		inv.setItem(19, chestplate);
		inv.setItem(28, leggings);
		inv.setItem(37, boots);
		
		inv.setItem(21, mainhand);
		inv.setItem(22, offhand);
		
		ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta smeta = (SkullMeta) playerHead.getItemMeta();
		smeta.setOwningPlayer(p);
		smeta.setDisplayName(getDisplayName());
		smeta.setLore(ImmutableList.<String>builder().add(p.isOnline() ? ChatColor.GREEN + "Online" : ChatColor.RED + "Offline").build());
		playerHead.setItemMeta(smeta);
		
		inv.setItem(12, playerHead);
		
		return inv;
	}
	
	/**
	 * Will silently fail if Player is offline.
	 */
	public void updateItemsInConfig() {
		if (!(p.isOnline())) return;
		Player op = p.getPlayer();
		
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			pConfig.getConfigurationSection("information").getConfigurationSection("items").set(slot.name().toLowerCase(), op.getEquipment().getItem(slot));
		}
		saveFile();
	}
	
	public ItemStack getItemFromConfig(EquipmentSlot slot) {
		return pConfig.getConfigurationSection("information").getConfigurationSection("items").getItemStack(slot.name().toLowerCase());
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
	
	public void setLevel(LevelingType t, int level) {
		switch (t) {
			case FARMING:
				setFarmingLevel(level);
				break;
			case FLETCHING:
				setFletchingLevel(level);
				break;
			case LEVEL:
				setLevel(level);
				break;
			default:
				break;
		}
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

	public List<Friend> getFriends() {
		return Friend.getFriendsFor(p);
	}

	public List<Friend> getOnlineFriends() {
		return Friend.getFriendsFor(p).stream().filter(f -> f.getFriend().isOnline()).toList();
	}

	public List<Friend> getOfflineFriends() {
		return Friend.getFriendsFor(p).stream().filter(f -> !(f.getFriend().isOnline())).toList();
	}

	public static class Friend {

		private static final Map<OfflinePlayer, OfflinePlayer> requestMap = new HashMap<>();
		private static final Map<OfflinePlayer, List<Friend>> friendMap = new HashMap<>();

		private final OfflinePlayer owner;
		private final OfflinePlayer friend;

		public Friend(OfflinePlayer owner, OfflinePlayer friend) {
			this.owner = owner;
			this.friend = friend;

			if (friendMap.containsKey(owner)) {
				friendMap.get(owner).add(this);
			} else {
				friendMap.put(owner, new ArrayList<>(ImmutableList.<Friend>builder().add(this).build()));
			}
		}

		public final OfflinePlayer getOwner() {
			return this.owner;
		}

		public final OfflinePlayer getFriend() {
			return this.friend;
		}

		public static boolean hasOutgoingRequest(OfflinePlayer p) {
			return requestMap.containsKey(p);
		}

		public static List<Friend> getFriendsFor(OfflinePlayer target) {
			return friendMap.get(target);
		}

	}

	// Boolean Methods

	public final boolean hasUnlocked(SMPMaterial item) {
		return getLevel() >= item.getLevelUnlocked();
	}

	public boolean isMember() {
		return pConfig.getString("rank").equalsIgnoreCase("member");	
	}


	
}
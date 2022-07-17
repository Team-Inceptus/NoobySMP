package us.teaminceptus.noobysmp.inventory;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.commands.Settings;
import us.teaminceptus.noobysmp.entities.bosses.BossManager;
import us.teaminceptus.noobysmp.leveling.LevelingManager;
import us.teaminceptus.noobysmp.leveling.LevelingManager.LevelingType;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.Messages;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

public class PlayerMenu implements CommandExecutor, Listener {

	protected final SMP plugin;

	public PlayerMenu(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		plugin.getCommand("menu").setExecutor(this);
	}

	public static class MenuHolder extends CancelHolder {}

	public static Inventory getMenu(Player p) {
		Inventory menu = Generator.genGUI(45, p.getDisplayName(), new MenuHolder());
		
		PlayerConfig config = new PlayerConfig(p);
		
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		meta.setOwningPlayer(p);
		meta.setDisplayName(p.getDisplayName());
		meta.setLore(Arrays.asList(
				ChatColor.GREEN + "Level: " + ChatColor.GOLD + config.getLevel(), ChatColor.GREEN + "Expereince: " + ChatColor.GOLD + Math.floor(config.getExperience() * 100) / 100,
				ChatColor.GREEN + "XP to Next Level: " + ChatColor.GOLD + Math.floor(config.getExpToNextLevel() * 100) / 100,
				ChatColor.GOLD + "Current Tag Limit: " + config.getTagLimit(),
				" ",
				ChatColor.GRAY + "" + ChatColor.UNDERLINE + "Statistics"
			));
		head.setItemMeta(meta);
		menu.setItem(13, head);

		ItemStack settings = Items.itemBuilder(Material.BEDROCK).setName(ChatColor.DARK_PURPLE + "Settings").build();
		ItemStack bosses = Items.itemBuilder(Material.BONE).setName(ChatColor.RED + "Bosses").build();
		
		ItemStack progress = Items.itemBuilder(Material.DIAMOND_SWORD).setName(ChatColor.YELLOW + "Level Progress").build();
		ItemStack fprogress = Items.itemBuilder(Material.IRON_HOE).setName(ChatColor.YELLOW + "Farming Progress").build();
		ItemStack fletprogress = Items.itemBuilder(Material.ARROW).setName("Fletching Progress").build();
		
		menu.setItem(20, settings);
		
		menu.setItem(24, bosses);
		
		menu.setItem(21, fprogress);
		menu.setItem(22, progress);
		menu.setItem(23, fletprogress);
		
		return menu;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player p)) return;
		if (e.getClickedInventory() == null) return;
		if (!(e.getClickedInventory().getHolder() instanceof MenuHolder)) return;
		if (e.getCurrentItem() == null) return;
		ItemStack item = e.getCurrentItem();
		if (!(item.hasItemMeta())) return;

		switch (item.getType()) {
			case BEDROCK -> {
				p.openInventory(Settings.getSettings(p));
				p.playSound(p, Sound.BLOCK_ANVIL_USE, 3F, 1F);
				break;
			}
			case BONE -> {
				p.openInventory(BossManager.getBossMenu(p));
				p.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL, 3F, 1F);
				break;
			}
			case DIAMOND_SWORD -> {
				p.openInventory(LevelingManager.getLevelingMenu(p, LevelingType.LEVEL));
				p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 3F, 1F);
				break;
			}
			case IRON_HOE -> {
				p.openInventory(LevelingManager.getLevelingMenu(p, LevelingType.FARMING));
				p.playSound(p, Sound.ITEM_HOE_TILL, 3F, 1F);
				break;
			}
			case ARROW -> {
				p.openInventory(LevelingManager.getLevelingMenu(p, LevelingType.FLETCHING));
				p.playSound(p, Sound.ENTITY_ARROW_SHOOT, 3F, 1F);
				break;
			}
			default -> {
				break;
			}
		}
		
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player p)) {
			sender.sendMessage(Messages.PLAYER_ONLY_CMD);
			return false;
		}

		p.openInventory(getMenu(p));
		return false;
	}
	
}
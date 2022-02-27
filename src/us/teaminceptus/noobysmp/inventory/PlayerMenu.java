package us.teaminceptus.noobysmp.inventory;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

public class PlayerMenu implements CommandExecutor, Listener {

	protected SMP plugin;

	public PlayerMenu(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public static class MenuHolder extends CancelHolder {};
	
	public static Inventory getMenu(Player p) {
		Inventory menu = Generator.genGUI(45, p.getDisplayName(), new MenuHolder());
		
		PlayerConfig config = new PlayerConfig(p);
		
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		meta.setOwningPlayer(p);
		meta.setDisplayName(p.getDisplayName());
		meta.setLore(Arrays.asList(ChatColor.GREEN + "Level: " + ChatColor.GOLD + config.getLevel(), ChatColor.GREEN + "Expereince: " + ChatColor.GOLD + Double.toString(Math.floor(config.getExperience() * 100) / 100),
				ChatColor.GREEN + "XP to Next Level: " + ChatColor.GOLD + Double.toString(Math.floor(config.getExpToNextLevel() * 100) / 100)));
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

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		return false;
	}
	
}
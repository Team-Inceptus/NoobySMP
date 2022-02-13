package us.teaminceptus.noobysmp.player;

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

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Items;

public class PlayerStatistics implements CommandExecutor, Listener {
	
	protected SMP plugin;
	
	public PlayerStatistics(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("statistics").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public static Inventory getStatisticsMenu() {
		Inventory inv = Generator.genGUI(45, "Player Skills & Statistics");
		
		ItemStack level = Items.itemBuilder(Material.EMERALD_BLOCK).setName(ChatColor.YELLOW + "Leveling").build();
		inv.setItem(12, level);
		
		ItemStack conquest = Items.itemBuilder(Material.DIAMOND_SWORD).setName(ChatColor.YELLOW + "Conquest").addGlint().build();
		inv.setItem(13, conquest);
		
		inv.setItem(14, Items.COMING_SOON);
		
		for (int i = 27; i < 36; i++) inv.setItem(i, Items.Inventory.GUI_PANE);
		
		return inv;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player p)) return false;
		
		
		return true;
	}
	
}

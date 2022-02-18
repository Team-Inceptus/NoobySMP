package us.teaminceptus.noobysmp.inventory;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.inventoryholder.PlayerHolder;

public class PlayerMenu implements Listener {

	protected SMP plugin;

	public PlayerMenu(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public static Inventory getMenu(Player p) {
		PlayerConfig config = new PlayerConfig(p);

		if (config.getLevel() < 1) return null;

		Inventory menu = Generator.genGUI(45, p.getDisplayName(), new PlayerHolder(p));

		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		meta.setOwningPlayer(p);
		meta.setDisplayName(p.getDisplayName());
		meta.setLore(Arrays.asList(ChatColor.GREEN + "Level: " + ChatColor.GOLD + config.getLevel(), ChatColor.GREEN + "Expereince: " + ChatColor.GOLD + Double.toString(Math.floor(config.getExperience() * 100) / 100),								 ChatColor.GREEN + "XP to Next Level: " + ChatColor.GOLD + Double.toString(Math.floor(config.getExpToNextLevel() * 100) / 100)));
		head.setItemMeta(meta);
		menu.setItem(20, head);

		ItemStack settings = new ItemStack(Material.BEDROCK);
		ItemMeta sMeta = settings.getItemMeta();
		sMeta.setDisplayName(ChatColor.DARK_PURPLE + "Settings");
		settings.setItemMeta(sMeta);
		menu.setItem(20, settings);

		
		return menu;
	}
	
}
package us.teaminceptus.noobysmp.commands.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Messages;

public class FetchData implements TabExecutor {

	protected SMP plugin;
	
	public FetchData(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("fetchdata").setExecutor(this);
		plugin.getCommand("fetchdata").setTabCompleter(this);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> suggestions = new ArrayList<>();
		
		if (args.length == 1) suggestions.addAll(Arrays.asList("persistentdata"));
		else if (args.length == 2) {
			switch (args[0].toLowerCase()) {
				case "persistentdata": {
					suggestions.addAll(Arrays.asList("chunk", "item"));
					break;
				}
			}
		}
		
		return suggestions;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player p)) {
			sender.sendMessage(Messages.PLAYER_ONLY_CMD);
			return false;
		}
		
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "Please provide a valid fetch data.");
			return false;
		}
		
		switch (args[0].toLowerCase()) {
			case "persistentdata": {
				if (args.length < 2) {
					p.sendMessage(ChatColor.RED + "Please provide a valid data container.");
					return false;
				}
				
				if (args.length < 3) {
					p.sendMessage(ChatColor.RED + "Please provide a valid data key.");
					return false;
				}
				
				switch (args[1].toLowerCase()) {
					case "chunk": {
						// TODO
						break;
					}
					case "item": {
						if (p.getInventory().getItemInMainHand() == null) {
							p.sendMessage(ChatColor.RED + "Please hold a valid item.");
							return false;
						}
						
						ItemStack item = p.getInventory().getItemInMainHand();
						String data = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, args[2]), PersistentDataType.STRING);
						
						p.sendMessage(ChatColor.GREEN + "The value of this item is: " + ChatColor.GOLD + data);
						
						break;
					}
					default: {
						p.sendMessage(ChatColor.RED + "Please provide a valid data type.");
						return false;
					}
				}
				
				return true;
			}
		
		}
		
		return false;
	}

}

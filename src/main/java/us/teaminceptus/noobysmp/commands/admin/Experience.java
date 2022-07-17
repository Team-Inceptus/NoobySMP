package us.teaminceptus.noobysmp.commands.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.leveling.LevelingManager.LevelingType;
import us.teaminceptus.noobysmp.util.Messages;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.SMPUtil;

public class Experience implements TabExecutor {
	
	protected final SMP plugin;
	
	public Experience(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("experience").setExecutor(this);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> suggestions = new ArrayList<>();

		switch (args.length) {
			case 1 -> {
				for (LevelingType t : LevelingType.values()) suggestions.add(t.name().toLowerCase());
				return suggestions;
			}
			case 2 -> {
				for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
					if (suggestions.contains(p.getName())) continue;
					suggestions.add(p.getName());
				}
				return suggestions;
			}
			case 3 -> {
				suggestions.addAll(Arrays.asList("level", "exp", "setlevel", "setexp"));
				return suggestions;
			}
		}
		
		return suggestions;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender.hasPermission("smp.admin.experience"))) {
			sender.sendMessage(Messages.NO_PERMISSION_CMD);
			return false;
		}
		
		try {
			if (args.length < 1) {
				sender.sendMessage(ChatColor.RED + "Please provide a valid Leveling Type.");
				return false;
			}
			
			LevelingType type = LevelingType.valueOf(args[0].toUpperCase());
			
			if (args.length < 2) {
				sender.sendMessage(Messages.ARGUMENT_PLAYER);
				return false;
			}
			
			if (SMPUtil.nameToUUID(args[0]) == null) {
				sender.sendMessage(Messages.ARGUMENT_PLAYER);
				return false;
			}
			
			OfflinePlayer p = SMPUtil.getOfflinePlayer(args[1]);
			PlayerConfig config = new PlayerConfig(p);
			
			if (args.length < 3) {
				sender.sendMessage(Messages.ARGUMENT_ACTION);
				return false;
			}
			
			switch (args[2].toLowerCase()) {
				case "level": {
					sender.sendMessage(ChatColor.GOLD + p.getName() + "'s " + ChatColor.GREEN + "Level is " + ChatColor.GOLD + config.getLevel(type));
					return true;
				}
				case "exp": {
					sender.sendMessage(ChatColor.GOLD + p.getName() + "'s " + ChatColor.GREEN + "Base Experience Amount is " + ChatColor.GOLD + config.getExperience());
					return true;
				}
				case "setlevel": {
					if (args.length < 4) {
						sender.sendMessage(Messages.ARGUMENT_INT);
						return false;
					}
					
					config.setLevel(type, Integer.parseInt(args[3]));
					sender.sendMessage(ChatColor.GREEN + "Successfully set " + ChatColor.GOLD + p.getName() + "'s" + ChatColor.GREEN + " level to " + ChatColor.GOLD + args[3] + ChatColor.GREEN + ".");
					return true;
				}
				case "setexp": {
					if (args.length < 4) {
						sender.sendMessage(Messages.ARGUMENT_DOUBLE);
						return false;
					}
					
					config.setExperience(Double.parseDouble(args[3]));
					sender.sendMessage(ChatColor.GREEN + "Successfully set " + ChatColor.GOLD + p.getName() + "'s" + ChatColor.GREEN + " experience amount to " + ChatColor.GOLD + args[3] + ChatColor.GREEN + ".");
				}
				default: {
					sender.sendMessage(Messages.ARGUMENT_ACTION);
					return false;
				}
			}
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED + "There was an error: " + e.getMessage());
			return false;
		}
	}

}

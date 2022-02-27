package us.teaminceptus.noobysmp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.leveling.LevelingManager;
import us.teaminceptus.noobysmp.leveling.LevelingManager.LevelingType;
import us.teaminceptus.noobysmp.util.Messages;

public class Progress implements TabExecutor  {
	
	protected SMP plugin;
	
	public Progress(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("progress").setExecutor(this);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> suggestions = new ArrayList<>();
		
		if (args.length == 1) for (LevelingType t : LevelingType.values()) suggestions.add(t.name().toLowerCase());
		
		return suggestions;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			if (!(sender instanceof Player p)) {
				sender.sendMessage(Messages.PLAYER_ONLY_CMD);
				return false;
			}
			if (args.length < 1) {
				p.openInventory(LevelingManager.getLevelingMenu(p, LevelingType.LEVEL));
			} else {
				LevelingType t = LevelingType.valueOf(args[0].toUpperCase());
				
				p.openInventory(LevelingManager.getLevelingMenu(p, t));
			}
			
		} catch (IllegalArgumentException e) {
			sender.sendMessage(ChatColor.RED + "There was an error: " + e.getMessage());
			return false;
		}
		
		return false;
	}

}

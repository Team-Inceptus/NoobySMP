package us.teaminceptus.noobysmp.commands.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.generation.biomes.TitanBiome;
import us.teaminceptus.noobysmp.util.Messages;

public class SetBiome implements TabExecutor {

	protected SMP plugin;
	
	public SetBiome(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("setbiome").setExecutor(this);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> suggestions = new ArrayList<>();
		
		if (args.length == 1) for (TitanBiome b : TitanBiome.values()) suggestions.add("noobysmp:" + b.name().toLowerCase());
		else if (args.length == 2) suggestions.addAll(Arrays.asList("chunk", "location"));
		
		return suggestions;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player p)) {
			sender.sendMessage(Messages.PLAYER_ONLY_CMD);
			return false;
		}
		
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "Please provide a valid biome.");
			return false;
		}
		
		try {
			TitanBiome biome = TitanBiome.valueOf(args[0].toLowerCase().replace("noobysmp:", "").toUpperCase());
			
			final String type;
			
			if (args.length < 2) {
				type = "location";
			} else type = args[1].toLowerCase();
			
			if (type.equalsIgnoreCase("chunk")) {
				biome.setBiome(p.getLocation().getChunk());
			} else if (type.equalsIgnoreCase("location")) {
				biome.setBiome(p.getLocation());
			}
			
			p.sendMessage(ChatColor.GREEN + "Successfully set biome to " + ChatColor.GOLD + biome.getName());
			
			return true;
		} catch (Exception e) {
			p.sendMessage(ChatColor.RED + "There was an error: " + e.getMessage());
			return false;
		}
		
	}
	
	

}

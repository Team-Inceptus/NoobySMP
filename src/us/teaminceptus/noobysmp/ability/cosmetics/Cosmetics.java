package us.teaminceptus.noobysmp.ability.cosmetics;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import us.teaminceptus.noobysmp.SMP;

public class Cosmetics implements CommandExecutor {

	protected SMP plugin;
	
	public Cosmetics(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("cosmetics").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		
		
		return false;
	}

}

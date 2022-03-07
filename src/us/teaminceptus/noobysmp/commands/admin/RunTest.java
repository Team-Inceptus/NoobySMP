package us.teaminceptus.noobysmp.commands.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.entities.bosses.BossManager;
import us.teaminceptus.noobysmp.util.Messages;

/**
 * Add manual methods for future testing in here
 *
 */
public class RunTest implements TabExecutor {
	
	protected SMP plugin;
	
	public RunTest(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("runtest").setExecutor(this);
		plugin.getCommand("runtest").setTabCompleter(this);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> suggestions = new ArrayList<>();
		if (args.length == 1) {
			suggestions.addAll(Arrays.asList("throwaxe"));
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
			p.sendMessage(ChatColor.RED + "Please provide a valid test.");
			return false;
		}
		
		switch (args[0].toLowerCase()) {
			case "throwaxe": {
				BossManager.throwItem(p.getEyeLocation(), 10, new ItemStack(Material.IRON_AXE), 10);
				break;
			}
			default: {
				return true;
			}
		}
		
		return true;
	};

}

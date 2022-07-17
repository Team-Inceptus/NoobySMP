package us.teaminceptus.noobysmp.ability.cosmetics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Messages;
import us.teaminceptus.noobysmp.util.PlayerConfig;

public class Cosmetics implements TabExecutor {

	public static final String VALID_COSMETIC = ChatColor.RED + "Please provide a valid cosmetic.";
	
	protected final SMP plugin;
	
	public Cosmetics(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("cosmetics").setExecutor(this);
		plugin.getCommand("cosmetics").setTabCompleter(this);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player p)) {
			sender.sendMessage(Messages.PLAYER_ONLY_CMD);
			return false;
		}
		
		if (args.length < 1) {
			p.sendMessage(VALID_COSMETIC);
			return false;
		}
		
		PlayerConfig config = new PlayerConfig(p);
		
		if (args[0].equalsIgnoreCase("cancel")) {
			config.cancelCosmetic();
			p.sendMessage(ChatColor.GREEN + "Successfully attempted to cancel cosmetic.");
			return true;
		}
		
		if (SMPCosmetic.matchCosmetic(args[0].toLowerCase().replace("smp:", "")) == null) {
			p.sendMessage(VALID_COSMETIC);
			return false;
		}
		
		SMPCosmetic cosmetic = SMPCosmetic.matchCosmetic(args[0].toLowerCase().replace("smp:", ""));

		
		if (cosmetic.getLevelUnlocked() > config.getLevel()) {
			p.sendMessage(Messages.TOO_LOW_LEVEL);
			return false;
		}
		
		config.setActiveCosmetic(cosmetic);
		p.sendMessage(ChatColor.GREEN + "Successfully updated Cosmetic!");
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> suggestions = new ArrayList<>();
		
		if (args.length == 1) {
			for (SMPCosmetic c : SMPCosmetic.values()) suggestions.add("smp:" + c.name().toLowerCase());
			suggestions.add("cancel");
		}
		
		return suggestions;
	}

}

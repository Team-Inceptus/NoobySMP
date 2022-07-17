package us.teaminceptus.noobysmp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Messages;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.SMPUtil;

public class PlayerInfo implements TabExecutor {
	
	protected final SMP plugin;
	
	public PlayerInfo(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("playerinfo").setExecutor(this); 
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> suggestions = new ArrayList<>();
		if (args.length == 1)
			for (OfflinePlayer p : Bukkit.getOfflinePlayers()) suggestions.add(p.getName());
		
		return suggestions;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player p)) {
			sender.sendMessage(Messages.PLAYER_ONLY_CMD);
			return false;
		}
		
		if (args.length < 1) {
			sender.sendMessage(Messages.ARGUMENT_PLAYER);
			return false;
		}
		
		if (SMPUtil.nameToUUID(args[0]) == null) {
			sender.sendMessage(Messages.ARGUMENT_PLAYER);
			return false;
		}
		
		PlayerConfig target = new PlayerConfig(SMPUtil.getOfflinePlayer(args[0]));
		p.openInventory(target.getPlayerInfo());
		p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3F, 2F);
		
		return true;
	}

}

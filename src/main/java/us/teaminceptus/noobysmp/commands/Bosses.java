package us.teaminceptus.noobysmp.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.entities.bosses.BossManager;
import us.teaminceptus.noobysmp.util.Messages;

public class Bosses implements CommandExecutor {

	protected final SMP plugin;
	
	public Bosses(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("bosses").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player p)) {
			sender.sendMessage(Messages.PLAYER_ONLY_CMD);
			return false;
		}
		
		p.openInventory(BossManager.getBossMenu(p));
		p.playSound(p, Sound.BLOCK_ENDER_CHEST_OPEN, 3F, 1F);
		return true;
	}

}

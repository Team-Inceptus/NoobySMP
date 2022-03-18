package us.teaminceptus.noobysmp.commands.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Messages;
import us.teaminceptus.noobysmp.util.SMPUtil;

public class Ban implements TabExecutor {
	
	public SMP plugin;
	
	public Ban(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("ban").setExecutor(this);
        plugin.getCommand("ban").setTabCompleter(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String bumper = org.apache.commons.lang.StringUtils.repeat("\n", 35);
		if (args.length < 1) {
			sender.sendMessage(Messages.ARGUMENT_PLAYER);
            return false;
		}

        UUID uuid = SMPUtil.nameToUUID(args[0]);
        
        if (uuid == null) {
            sender.sendMessage(ChatColor.RED + "This player does not exist.");
            return false;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
        
        if (args.length < 2) {
           sender.sendMessage(ChatColor.RED + "A reason needs to be provided.");
           return false;
        }
        
        List<String> reasonArgs = new ArrayList<>();
        
        for (int i = 1; i < args.length; i++) {
            reasonArgs.add(args[i]);
        }

        String reason = String.join(" ", reasonArgs);
        String banMsg = ChatColor.RED + "You have been permanently banned!\n\n" + ChatColor.GOLD + "Admin: " + ChatColor.DARK_RED + sender.getName() + ChatColor.GOLD + "\nReason: " + ChatColor.WHITE + reason;

        if (target.isOnline()) {
            Bukkit.getPlayer(args[0]).kickPlayer(banMsg);
        }

        Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), bumper + banMsg + bumper, null, sender.getName());
		return false;
	}

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) for (OfflinePlayer p : Bukkit.getOfflinePlayers()) suggestions.add(p.getName());
        
        return suggestions;
    }

}
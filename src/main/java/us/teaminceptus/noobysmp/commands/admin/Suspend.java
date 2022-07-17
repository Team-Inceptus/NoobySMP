package us.teaminceptus.noobysmp.commands.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

public class Suspend implements TabExecutor {
	
	public final SMP plugin;
	
	public Suspend(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("suspend").setExecutor(this);
        plugin.getCommand("suspend").setTabCompleter(this);
	}

    private static final String TIME_INFO = "\nd - Days\nm - minutes\nM - months\nh - Hours\nw - Weeks";
	
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
            sender.sendMessage(ChatColor.RED + "Please provide a valid time." + TIME_INFO);
            return false;
        }

        long timeBanned;

        long timeMultiplier; // Millis
        String timeEnd;

        try {
            timeBanned = Long.parseLong(args[1].substring(0, args[1].length() - 1));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Please provide a valid time." + TIME_INFO);
            return false;
        }
 
        switch (Character.toString(args[1].charAt(args[1].length() - 1))) {
            case "d": {
                timeMultiplier = 1000 * 60 * 60 * 24;
                timeEnd = " Day(s)";
                break;
            }
            case "m": {
                timeMultiplier = 1000 * 60 * 60;
                timeEnd = " Minute(s)";
                break;
            }
            case "M": {
                timeMultiplier = 1000L * 60 * 60 * 24 * 30;
                timeEnd = " Month(s) (30 Days)";
                break;
            }
            case "w": {
                timeMultiplier = 1000 * 60 * 60 * 24 * 7;
                timeEnd = " Week(s)";
                break;
            }
            case "h": {
                timeMultiplier = 1000 * 60 * 60;
                timeEnd = " Hour(s)";
                break;
            }
            default: {
                sender.sendMessage(ChatColor.RED + "Please provide a valid time modifier." + TIME_INFO);
                return false;
            }
        }

        Date time = new Date(System.currentTimeMillis() + (timeBanned * timeMultiplier));

        if (args.length < 3) {
           sender.sendMessage(ChatColor.RED + "A reason needs to be provided.");
           return false;
        }

        List<String> reasonArgs = new ArrayList<>();

        reasonArgs.addAll(Arrays.asList(args).subList(1, args.length));

        String reason = String.join(" ", reasonArgs);
        String banMsg = ChatColor.RED + "You have been temporarily banned!\n\n" + ChatColor.GOLD + "Admin: " + ChatColor.DARK_RED + sender.getName() + ChatColor.GOLD + "\nReason: " + ChatColor.WHITE + reason + ChatColor.GOLD + "\nTime: " + timeBanned + " " + timeEnd;

        if (target.isOnline()) {
            Bukkit.getPlayer(args[0]).kickPlayer(banMsg);
        }

        Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), bumper + banMsg + bumper, time, sender.getName());
		return false;
	}

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) for (OfflinePlayer p : Bukkit.getOfflinePlayers()) suggestions.add(p.getName());
        else if (args.length == 2) {
            suggestions.addAll(Arrays.asList("7d", "5h", "3M", "45m", "3w", "24d", "2y", "120s"));
        }
        return suggestions;
    }

}
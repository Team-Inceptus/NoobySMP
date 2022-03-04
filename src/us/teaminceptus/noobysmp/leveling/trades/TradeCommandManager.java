package us.teaminceptus.noobysmp.leveling.trades;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.leveling.trades.TradesManager.TradeInstance;
import us.teaminceptus.noobysmp.util.Messages;
import us.teaminceptus.noobysmp.util.PlayerConfig;

public class TradeCommandManager implements CommandExecutor {
    
    public static final String NO_REQUESTS = ChatColor.RED + "You do not have any pending trades.";
    protected SMP plugin;

    public TradeCommandManager(SMP plugin) {
        this.plugin = plugin;
        plugin.getCommand("accepttrade").setExecutor(this);
        plugin.getCommand("rejecttrade").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(Messages.PLAYER_ONLY_CMD);
            return false;
        }
        switch (command.getName().toLowerCase()) {
            case "accepttrade": {
                if (!(TradesManager.requests.containsKey(p.getUniqueId()))) {
                    p.sendMessage(NO_REQUESTS);
                    return false;
                }
                TradeInstance.createTrade(TradesManager.requests.get(p.getUniqueId()), p);
                break;
            }
            case "rejecttrade": {
                if (!(TradesManager.requests.containsKey(p.getUniqueId()))) {
                    p.sendMessage(NO_REQUESTS);
                    return false;
                }
                TradesManager.requests.get(p.getUniqueId()).sendMessage(ChatColor.RED + new PlayerConfig(p).getDisplayName() + ChatColor.RED + " has rejected your trade request!");
                TradesManager.requests.put(p.getUniqueId(), null);
                break;
            }
        }
        return false;
    }

}

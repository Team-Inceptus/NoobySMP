package us.teaminceptus.noobysmp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Messages;
import us.teaminceptus.noobysmp.util.Queryable;
import us.teaminceptus.noobysmp.util.Queryable.QueryID;

public class Query implements TabExecutor {
    
    protected final SMP plugin;

    public Query(SMP plugin) {
        this.plugin = plugin;
        plugin.getCommand("query").setExecutor(this);
        plugin.getCommand("query").setTabCompleter(this);
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        
        if (args.length == 1) for (Queryable q : Queryable.ALL) suggestions.add(q.queryId().type() + ":" + q.queryId().value());

        return suggestions;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(Messages.ARGUMENT_PLAYER);
            return false;
        }

        try {
            if (args.length < 1) {
                p.sendMessage(ChatColor.RED + "Please provide a valid item to query.");
                return false;
            }

            QueryID id = new QueryID(args[0].split(":")[0], args[0].split(":")[1]);

            if (Queryable.getById(id) == null) {
                p.sendMessage(ChatColor.RED + "Please provide a valid item to query.");
                return false;
            }

            Queryable q = Queryable.getById(id);

            p.openInventory(q.getQueryInventory());
            p.playSound(p, Sound.ENTITY_VILLAGER_TRADE, 3F, 1F);
        } catch (IndexOutOfBoundsException e) {
            p.sendMessage(ChatColor.RED + "Please provide a valid item to query.");
            return false;
        }
        return true;
    }

}

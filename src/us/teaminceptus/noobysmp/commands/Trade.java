package us.teaminceptus.noobysmp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.leveling.trades.TradesManager;
import us.teaminceptus.noobysmp.leveling.trades.TradesManager.TradeInstance;
import us.teaminceptus.noobysmp.util.Messages;
import us.teaminceptus.noobysmp.util.PlayerConfig;

public class Trade implements CommandExecutor {

    protected SMP plugin;

    public Trade(SMP plugin) {
        this.plugin = plugin;
        plugin.getCommand("trade").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(Messages.PLAYER_ONLY_CMD);
            return false;
        }

        if (args.length < 1) {
            p.sendMessage(Messages.ARGUMENT_PLAYER);
            return false;
        }

        if (!(TradeInstance.canTrade(p))) {
            p.sendMessage(ChatColor.RED + "You have not unlocked trading yet!");
            return false;
        }

        if (Bukkit.getPlayer(args[0]) == null) {
            p.sendMessage(Messages.ARGUMENT_PLAYER);
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        
        if (target.getUniqueId().equals(p.getUniqueId())) {
        	p.sendMessage(ChatColor.RED + "You can't trade with yourself!");
        	return false;
        }

        if (!(TradeInstance.canTrade(target))) {
            p.sendMessage(ChatColor.RED + "This player cannot trade yet!");
            return false;
        }   

        if (TradesManager.requests.containsKey(target.getUniqueId())) {
            p.sendMessage(ChatColor.RED + "This player already has a pending request!");
            return false;
        }

        if (TradesManager.requests.containsValue(p)) {
            p.sendMessage(ChatColor.RED + "You already have an outgoing request!");
            return false;
        }

        if (TradeInstance.isInTrade(p)) {
            p.sendMessage(ChatColor.RED + "This player is already trading with: " + new PlayerConfig(TradeInstance.from(p).getHolder().p1).getDisplayName());
            return false;
        }

        TradesManager.requests.put(target.getUniqueId(), p);

        TextComponent message1 = new TextComponent(ChatColor.GREEN + "You have an incoming request from " + p.getName() + "! Click ");
        TextComponent action = new TextComponent(ChatColor.YELLOW + "here");
        action.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "accepttrade"));
        TextComponent message2 = new TextComponent(ChatColor.GREEN + " to accept!");
        
        target.spigot().sendMessage(new BaseComponent[] {message1, action, message2});
        
        return true;
    }

}

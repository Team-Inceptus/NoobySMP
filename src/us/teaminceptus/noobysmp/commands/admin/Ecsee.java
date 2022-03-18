package us.teaminceptus.noobysmp.commands.admin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Messages;

public class Ecsee implements TabExecutor {
    
    protected SMP plugin;

    public Ecsee(SMP plugin) {
        this.plugin = plugin;
        plugin.getCommand("enderchestsee").setExecutor(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) return null;
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) for (Player op : Bukkit.getOnlinePlayers().stream().filter(op -> !(p.getUniqueId().equals(op.getUniqueId()))).toList()) suggestions.add(op.getName());

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

        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(Messages.ARGUMENT_PLAYER);
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target.getUniqueId().equals(p.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Please provide a valid target that is not yourself.");
            return false;
        }

        p.openInventory(target.getEnderChest());
        p.playSound(p, Sound.BLOCK_CHEST_OPEN, 3F, 1F);
        return false;
    }

    

}

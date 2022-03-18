package us.teaminceptus.noobysmp.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Messages;
import us.teaminceptus.noobysmp.util.PlayerConfig;

public class Mute implements CommandExecutor {
    
    protected SMP plugin;

    public Mute(SMP plugin) {
        this.plugin = plugin;
        plugin.getCommand("mute").setExecutor(this);
        plugin.getCommand("unmute").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Messages.ARGUMENT_PLAYER);
            return false;
        }

        if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(Messages.ARGUMENT_PLAYER);
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        PlayerConfig tConfig = new PlayerConfig(target);

        boolean mute = (cmd.getName().equalsIgnoreCase("mute") ? true : false);

        if (tConfig.isMuted() == mute) {
            sender.sendMessage(ChatColor.RED + "This player is already " + cmd.getName() + "d.");
            return false;
        }

        tConfig.setMuted(mute);
        sender.sendMessage(ChatColor.GREEN + "Successfully " + cmd.getName() + "d " + ChatColor.GOLD + target.getName() + ChatColor.GREEN + ".");
        return true;
    }

    

}

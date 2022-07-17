package us.teaminceptus.noobysmp.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Messages;

/**
 * Teleport Request Commands (/tpa, /tpaccept, /tpdeny, etc)
 */
public class TPRequests implements TabExecutor {
    
    protected final SMP plugin;

    public TPRequests(SMP plugin) {
        this.plugin = plugin;

        plugin.getCommand("tpa").setExecutor(this);
        plugin.getCommand("tpaccept").setExecutor(this);
        plugin.getCommand("tpdeny").setExecutor(this);

        plugin.getCommand("tpa").setTabCompleter(this);
        plugin.getCommand("tpaccept").setTabCompleter(this);
        plugin.getCommand("tpdeny").setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return null;
        List<String> suggestions = new ArrayList<>();
        
        if (args.length == 1) 
            if (!(cmd.getName().equalsIgnoreCase("tpdeny")) && !(cmd.getName().equalsIgnoreCase("tpeveryone")))  
                for (Player op : Bukkit.getOnlinePlayers().stream().filter(op -> !(op.getUniqueId().equals(p.getUniqueId()))).toList()) suggestions.add(op.getName());
        

        return suggestions;
    }

    private static final Map<UUID, UUID> TPA_REQUESTS = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(Messages.PLAYER_ONLY_CMD);
            return false;
        }

        switch (cmd.getName().toLowerCase()) {
            case "tpa" -> {
                if (args.length < 1) {
                    p.sendMessage(Messages.ARGUMENT_PLAYER);
                    return false;
                }

                if (Bukkit.getPlayer(args[0]) == null) {
                    p.sendMessage(Messages.ARGUMENT_PLAYER);
                    return false;
                }

                Player target = Bukkit.getPlayer(args[0]);

                if (TPA_REQUESTS.get(p.getUniqueId()) != null) {
                    p.sendMessage(ChatColor.RED + "You already have an outgoing request!");
                    return false;
                }

                TPA_REQUESTS.put(p.getUniqueId(), target.getUniqueId());
                p.sendMessage(ChatColor.AQUA + "You have successfully sent a TPA Request to " + ChatColor.GREEN + target.getDisplayName() + ChatColor.AQUA + "!\nThis will expire in 30 seconds.");
                target.playSound(target, Sound.ENTITY_ARROW_HIT_PLAYER, 3F, 0F);
                target.sendMessage(ChatColor.AQUA + "Incoming TPA Request from " + ChatColor.GREEN + p.getDisplayName() + ChatColor.AQUA + ". Type /tpaccept to accept, or /tpdeny to deny.");

                new BukkitRunnable() {
                    public void run() {
                        if (TPA_REQUESTS.get(p.getUniqueId()) != null) {
                            p.sendMessage(ChatColor.RED + "Your TPA Request has expired.");
                            TPA_REQUESTS.remove(p.getUniqueId());
                        }
                    }
                }.runTaskLater(plugin, 20 * 30);
                return false;
            }
            case "tpaccept" -> {
                if (!(TPA_REQUESTS.containsValue(p.getUniqueId()))) {
                    p.sendMessage(ChatColor.RED + "You do not have any outgoing requests.");
                    return false;
                }

                for (UUID uid : TPA_REQUESTS.keySet()) {
                    if (!(TPA_REQUESTS.get(uid).equals(p.getUniqueId()))) continue;

                    Player requester = Bukkit.getPlayer(uid);
                    requester.playSound(requester, Sound.ENTITY_ENDERMAN_TELEPORT, 3F, 1F);
                    requester.teleport(p);
                    requester.sendMessage(p.getDisplayName() + ChatColor.GREEN + " has accepted your teleport request!");
                    TPA_REQUESTS.remove(uid);
                }

                p.sendMessage(ChatColor.GREEN + "You have accepted all teleport requests!");
                return false;
            }
            case "tpdeny" -> {
                if (!(TPA_REQUESTS.containsValue(p.getUniqueId()))) {
                    p.sendMessage(ChatColor.RED + "You do not have any outgoing requests.");
                    return false;
                }

                for (UUID uid : TPA_REQUESTS.keySet()) {
                    if (!(TPA_REQUESTS.get(uid).equals(p.getUniqueId()))) continue;

                    Player requester = Bukkit.getPlayer(uid);
                    requester.sendMessage(p.getDisplayName() + ChatColor.RED + " has denied your teleport request!");
                    TPA_REQUESTS.remove(uid);
                }

                p.sendMessage(ChatColor.RED + "You have denied all teleport requests!");
                return false;
            }
        }

        return false;
    }



}

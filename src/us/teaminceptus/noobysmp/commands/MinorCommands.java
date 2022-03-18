package us.teaminceptus.noobysmp.commands;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Messages;
import us.teaminceptus.noobysmp.util.PlayerConfig;

/**
 * Minor Commands manager with no TabCompleter (i.e. open inventory) relating to players
 */
public class MinorCommands implements CommandExecutor {
    
    protected SMP plugin;

    public MinorCommands(SMP plugin) {
        this.plugin = plugin;
        
        plugin.getCommand("craft").setExecutor(this);
        plugin.getCommand("bed").setExecutor(this);
        plugin.getCommand("enderchest").setExecutor(this);
        plugin.getCommand("hub").setExecutor(this);
        plugin.getCommand("home").setExecutor(this);
        plugin.getCommand("sethome").setExecutor(this);
        plugin.getCommand("flyspeed").setExecutor(this);
        plugin.getCommand("compass").setExecutor(this);
        plugin.getCommand("etable").setExecutor(this);
        plugin.getCommand("afk").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(Messages.PLAYER_ONLY_CMD);
            return false;
        }

        PlayerConfig config = new PlayerConfig(p);

        switch (cmd.getName().toLowerCase()) {
            case "craft": {
                if (config.getLevel() < 5) {
                    p.sendMessage(Messages.TOO_LOW_LEVEL);
                    return false;
                }

                p.openWorkbench(p.getLocation(), true);
                return true;
            }
            case "enderchest": {
                if (config.getLevel() < 5) {
                    p.sendMessage(Messages.TOO_LOW_LEVEL);
                    return false;
                }

                p.openInventory(p.getEnderChest());
                p.playSound(p, Sound.BLOCK_ENDER_CHEST_OPEN, 3F, 1F);
                return true;
            }
            case "flyspeed": {
                if (!(p.getAllowFlight())) {
                    p.sendMessage(ChatColor.RED + "You are not allowed to fly.");
                    return false;
                }

                if (!(p.isFlying())) {
                    p.sendMessage(ChatColor.RED + "You are not flying!");
                    return false;
                }

                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please provide a fly speed between 1 and 10.");
                    return false;
                }

                try {
                    float fs = Float.parseFloat(args[1]);

                    p.setFlySpeed(fs);
                    p.sendMessage(ChatColor.GREEN + "Successfully set fly speed to " + ChatColor.GOLD + fs + ChatColor.GREEN + ".");
                } catch (IllegalArgumentException e) {
                    p.sendMessage(ChatColor.RED + "Please provide a fly speed between 1 and 10.");
                    return false;
                }

                return true;
            }
            case "hub": {
                p.sendMessage(ChatColor.RED + "Teleporting to Hub...");
                p.teleport(p.getWorld().getSpawnLocation());
                p.playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, 3F, 1F);

                return true;
            }
            case "sethome": {
                config.setHome(p.getLocation());
                p.sendMessage(ChatColor.GOLD + "Successfully changed home to current location.");
                return true;
            }
            case "home": {
                p.sendMessage(ChatColor.GOLD + "Teleporting to home...");
                p.teleport(config.getHome());
                return true;
            }
            case "bed": {
                p.sendMessage(ChatColor.BLUE + "Teleporting to Spawn Point...");
                p.teleport(p.getBedSpawnLocation());
                return true;
            }
            case "compass": {
                if (p.getInventory().getItemInMainHand() != null) {
                    ItemStack item = p.getInventory().getItemInMainHand();

                    if (item.hasItemMeta() && item.getItemMeta() instanceof CompassMeta m) {
                        if (m.hasLodestone()) {
                            p.sendMessage(ChatColor.DARK_AQUA + "Teleporting to Lodestone...");
                            p.teleport(m.getLodestone());
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.AQUA + "This command teleports you to a compass that is pointing to a lodestone. Use /bed or /hub to teleport back to your spawn.");
                    return false;
                }

                return true;
            }
            case "etable": {
                if (config.getLevel() < 5) {
                    p.sendMessage(Messages.TOO_LOW_LEVEL);
                    return false;
                }

                p.openEnchanting(p.getLocation(), true);
                return true;
            }
            case "afk": {
                config.toggleAFK();
                if (config.isAFK()) p.sendMessage(ChatColor.GREEN + "You are now AFK!");
                else p.sendMessage(ChatColor.RED + "You are no longer AFK.");
                return true;
            }
        }
        return true;
    }

    

}

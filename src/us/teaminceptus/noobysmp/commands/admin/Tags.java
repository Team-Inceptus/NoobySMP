package us.teaminceptus.noobysmp.commands.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPTag;
import us.teaminceptus.noobysmp.util.Messages;

public class Tags implements TabExecutor {
    
    protected SMP plugin;

    public Tags(SMP plugin) {
        this.plugin = plugin;
        plugin.getCommand("tags").setExecutor(this);
        plugin.getCommand("tags").setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) suggestions.addAll(Arrays.asList("add", "list"));
        else {
            if (args[0].equalsIgnoreCase("list")) return suggestions;

            List<AbilityItem> items = Arrays.asList(AbilityItem.values()).stream().filter(a -> SMPTag.getByOrigin(a) != null).toList();

            for (AbilityItem item : items) suggestions.add("smptag:" + item.name().toLowerCase());
        }
        return suggestions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(Messages.PLAYER_ONLY_CMD);
            return false;
        }

        if (args.length < 1) {
            p.sendMessage(Messages.ARGUMENT_ACTION);
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "add": {
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Please provide a valid tag.");
                    return false;
                }

                if (p.getInventory().getItemInMainHand() == null) {
                    p.sendMessage(ChatColor.RED + "Please hold a valid tag target.");
                    return false;
                }

                ItemStack target = p.getInventory().getItemInMainHand().clone();

                List<SMPTag<?>> tags = new ArrayList<>();

                for (int i = 1; i < args.length; i++) {
                    if (AbilityItem.matchEnum(args[i].toLowerCase().replace("smptag:", "").toUpperCase()) == null) {
                        p.sendMessage(ChatColor.RED + "Please provide a valid tag.");
                        return false;
                    }

                    SMPTag<?> tag = SMPTag.getByOrigin(AbilityItem.matchEnum(args[i].toLowerCase().replace("smptag:", "").toUpperCase()));
                    if (!(tag.getTarget().matches(target))) {
                        p.sendMessage(ChatColor.RED + "The tag " + tag.getName().toUpperCase() + " does not support this item.");
                        return false;
                    }

                    tags.add(tag);
                }

                for (SMPTag<?> tag : tags) target = tag.addTag(target).clone();

                p.getInventory().addItem(target);
                p.playSound(p, Sound.ENTITY_ARROW_HIT_PLAYER, 3F, 2F);
                p.sendMessage(ChatColor.GREEN + "Successfully added " + ChatColor.GOLD + tags.size() + ChatColor.GREEN + " tags!");

                break;  
            }
            case "list": {
                if (p.getInventory().getItemInMainHand() == null) {
                    p.sendMessage(ChatColor.RED + "Please hold a valid item.");
                    return false;
                }

                ItemStack target = p.getInventory().getItemInMainHand().clone();       

                List<SMPTag<?>> tags = SMPTag.getTags(target);

                List<String> tagInfo = new ArrayList<>();

                for (SMPTag<?> tag : tags) tagInfo.add(ChatColor.GOLD + "Name: " + ChatColor.AQUA + tag.getName() + 
                ChatColor.GOLD + " | Target: " + ChatColor.AQUA + tag.getTarget().name() + ChatColor.GOLD + " | Origin: " + ChatColor.AQUA + tag.getOrigin().getDisplayName());

                String msg = ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Tags (" + tagInfo.size() + ")\n\n" + String.join("\n", tagInfo);

                p.sendMessage(msg);
                p.playSound(p, Sound.ENTITY_ARROW_HIT_PLAYER, 3F, 2F);
                break;
            }
        }
        return true;
    }
}

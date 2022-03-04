package us.teaminceptus.noobysmp.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

public class Friends implements TabExecutor, Listener {
    
    protected SMP plugin;

    public Friends(SMP plugin) {
        this.plugin = plugin;
        plugin.getCommand("friends").setExecutor(this);
        plugin.getCommand("friends").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static class FriendsHolder extends CancelHolder {};

    public Inventory getFriendsList(OfflinePlayer p, int index) {
        Inventory inv = Generator.genGUI(54, "Friends List - Page " + Integer.toString(index), new FriendsHolder());
        PlayerConfig config = new PlayerConfig(p);

        ItemStack head = Items.itemBuilder(Material.PLAYER_HEAD).setName(ChatColor.RED + config.getDisplayName()).setLore(
            ImmutableList.<String>builder()
            .add((p.isOnline() ? ChatColor.GREEN + "Online" : ChatColor.RED + "Offline"))
            .build()).build();
        
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(p);
        head.setItemMeta(meta);
        inv.setItem(4, head);



        return inv;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) suggestions.addAll(Arrays.asList("add", "remove", "list"));

        return suggestions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        return false;
    }

}

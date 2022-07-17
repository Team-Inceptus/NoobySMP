package us.teaminceptus.noobysmp.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import us.teaminceptus.noobysmp.SMP;

/**
 * Wrapper for Server Configuration
 */
public class ServerConfig implements Listener {
    
    private static SMP plugin;
    private static FileConfiguration config;

    public static final String MAINTENANCE_MESSAGE = ChatColor.RED + "The server is currently under maintenance.\n\nCheck the Discord for updates: https://discord.io/thenoobygods";

    public ServerConfig(SMP plugin) {
        ServerConfig.plugin = plugin;
        config = plugin.getConfig();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // Event Listeners
    @EventHandler
    public void onJoin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        PlayerConfig config = new PlayerConfig(p);

        if (isMaintenanceOn() && config.isMember()) {
            e.disallow(Result.KICK_FULL, MAINTENANCE_MESSAGE);
        }
    }

    // Methods

    public static void saveConfig() {
        plugin.saveConfig();
    }

    public static boolean isMaintenanceOn() {
        return config.getBoolean("maintenance");
    }

    private static void updateMaintenance() {
        if (isMaintenanceOn()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                PlayerConfig config = new PlayerConfig(p);

                if (config.isMember()) {
                    p.kickPlayer(MAINTENANCE_MESSAGE);
                }
            }
        }
    }

    public static void setMaintenance(boolean maintenance) {
        config.set("maintenance", maintenance);
        saveConfig();
        updateMaintenance();
    }

}

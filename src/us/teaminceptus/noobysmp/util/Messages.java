package us.teaminceptus.noobysmp.util;

import org.bukkit.ChatColor;

/**
 * Interface for auto static & public fields.
 * 
 * <strong>Not legal for Implementation!</strong>
 */
public interface Messages {

	String NO_PERMISSION_CMD = ChatColor.RED + "You do not have permission to use this command!";
	String TOO_LOW_LEVEL = ChatColor.RED + "You do not have a high enough level to use this!";
	String PLAYER_ONLY_CMD = ChatColor.RED + "You need to be a player to execute this command!";

	// Arguments
	String ARGUMENT_PLAYER = ChatColor.RED + "Please provide a valid player.";
	String ARGUMENT_INT = ChatColor.RED + "Please provide a valid integer.";
	String ARGUMENT_ITEM = ChatColor.RED + "Please provide a valid item.";
	
}
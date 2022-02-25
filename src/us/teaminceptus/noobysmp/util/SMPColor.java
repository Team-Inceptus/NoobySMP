package us.teaminceptus.noobysmp.util;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public class SMPColor {
	
	public static String chatHex(String hexCode, String message) {
		String hex1 = "&" + Character.toString(hexCode.charAt(0)).toUpperCase();
		String hex2 = "&" + Character.toString(hexCode.charAt(1)).toUpperCase();
		String hex3 = "&" + Character.toString(hexCode.charAt(2)).toUpperCase();
		String hex4 = "&" + Character.toString(hexCode.charAt(3)).toUpperCase();
		String hex5 = "&" + Character.toString(hexCode.charAt(4)).toUpperCase();
		String hex6 = "&" + Character.toString(hexCode.charAt(5)).toUpperCase();
		
		return ChatColor.translateAlternateColorCodes('&', "&x" + hex1 + hex2 + hex3 + hex4 + hex5 + hex6 + message);
	}

	private static Color fromHex(String hex) {
		return Color.fromRGB(
					Integer.parseInt(hex.substring(0, 2), 16),
					Integer.parseInt(hex.substring(2, 4), 16),
					Integer.parseInt(hex.substring(4, 6), 16)
				);
	}
	
	public static final Color DARK_GRAY = fromHex("3a3a39");

}

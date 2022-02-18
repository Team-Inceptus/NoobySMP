package us.teaminceptus.noobysmp.util;

import org.bukkit.Color;

public class SMPColor {
	
	private static final Color fromHex(String hex) {
		return Color.fromRGB(
					Integer.parseInt(hex.substring(0, 2), 16),
					Integer.parseInt(hex.substring(2, 4), 16),
					Integer.parseInt(hex.substring(4, 6), 16)
				);
	}
	
	public static final Color DARK_GRAY = fromHex("3a3a39");

}

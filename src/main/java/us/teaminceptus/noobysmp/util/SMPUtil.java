package us.teaminceptus.noobysmp.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.google.gson.Gson;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public class SMPUtil {
		
	private final static HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
	
	// Constants
	public static final int ANVIL_ITEM_SLOT = 0;
	public static final int ANVIL_ADDITION_SLOT = 1;
	public static final int ANVIL_RESULT_SLOT = 2;

	private static class ColorUtil {

		private static final Map<ChatColor, ColorSet<Integer, Integer, Integer>> colorMap = new HashMap<>();
	
		static {
			colorMap.put(ChatColor.BLACK, new ColorSet<>(0, 0, 0));
			colorMap.put(ChatColor.DARK_BLUE, new ColorSet<>(0, 0, 170));
			colorMap.put(ChatColor.DARK_GREEN, new ColorSet<>(0, 170, 0));
			colorMap.put(ChatColor.DARK_AQUA, new ColorSet<>(0, 170, 170));
			colorMap.put(ChatColor.DARK_RED, new ColorSet<>(170, 0, 0));
			colorMap.put(ChatColor.DARK_PURPLE, new ColorSet<>(170, 0, 170));
			colorMap.put(ChatColor.GOLD, new ColorSet<>(255, 170, 0));
			colorMap.put(ChatColor.GRAY, new ColorSet<>(170, 170, 170));
			colorMap.put(ChatColor.DARK_GRAY, new ColorSet<>(85, 85, 85));
			colorMap.put(ChatColor.BLUE, new ColorSet<>(85, 85, 255));
			colorMap.put(ChatColor.GREEN, new ColorSet<>(85, 255, 85));
			colorMap.put(ChatColor.AQUA, new ColorSet<>(85, 255, 255));
			colorMap.put(ChatColor.RED, new ColorSet<>(255, 85, 85));
			colorMap.put(ChatColor.LIGHT_PURPLE, new ColorSet<>(255, 85, 255));
			colorMap.put(ChatColor.YELLOW, new ColorSet<>(255, 255, 85));
			colorMap.put(ChatColor.WHITE, new ColorSet<>(255, 255, 255));
		}
	
		private static class ColorSet<R, G, B> {
			R red;
			G green;
			B blue;
	
			ColorSet(R red, G green, B blue) {
				this.red = red;
				this.green = green;
				this.blue = blue;
			}
	
			public R getRed() {
				return red;
			}
	
			public G getGreen() {
				return green;
			}
	
			public B getBlue() {
				return blue;
			}
	
		}
	
		public static ChatColor fromRGB(int r, int g, int b) {
			TreeMap<Integer, ChatColor> closest = new TreeMap<>();
			colorMap.forEach((color, set) -> {
				int red = Math.abs(r - set.getRed());
				int green = Math.abs(g - set.getGreen());
				int blue = Math.abs(b - set.getBlue());
				closest.put(red + green + blue, color);
			});
			return closest.firstEntry().getValue();
		}
	
	}

	public static ChatColor fromRGB(int r, int g, int b) {
		return ColorUtil.fromRGB(r, g, b);
	}

	public static class APIPlayer {
		
		public final String name;
		public final String id;
		
		public APIPlayer(String name, String id) {
			this.name = name;
			this.id = id;
		}
		
	}
	
	public static UUID nameToUUID(String name) {
		if (Bukkit.getOnlineMode()) {
			try {
				HttpRequest request = HttpRequest.newBuilder()
						.GET()
						.uri(URI.create("https://api.mojang.com/users/profiles/minecraft/" + name))
						.setHeader("User-Agent", "Java 17 HttpClient Bot")
						.build();
				HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
				
					if (response.statusCode() == 200) {
						Gson g = new Gson();
	
						return untrimUUID(g.fromJson(response.body(), APIPlayer.class).id);
					}
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
			}
    	return null;
	}
	
	public static OfflinePlayer getOfflinePlayer(String name) {
		return Bukkit.getOfflinePlayer(nameToUUID(name));
	}
	
	public static String withSuffix(double count) {
	    if (count < 1000) return "" + Math.floor(count * 100) / 100;
	    int exp = (int) (Math.log(count) / Math.log(1000));
	    return String.format("%.1f%c",
	                         count / Math.pow(1000, exp),
	                         "KMBTQISPOND".charAt(exp-1));
	}
	
	public static int statusCode(String url) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.GET()
					.uri(URI.create(url))
					.setHeader("User-Agent", "Java 17 HttpClient Bot")
					.build();
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			
			return response.statusCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 404;
	}
	
	public static UUID untrimUUID(String oldUUID) {
		String p1 = oldUUID.substring(0, 8);
		String p2 = oldUUID.substring(8, 12);
		String p3 = oldUUID.substring(12, 16);
		String p4 = oldUUID.substring(16, 20);
		String p5 = oldUUID.substring(20, 32);
		
		String newUUID = p1 + "-" + p2 + "-" + p3 + "-" + p4 + "-" + p5;
		
		return UUID.fromString(newUUID);
	}
	

}

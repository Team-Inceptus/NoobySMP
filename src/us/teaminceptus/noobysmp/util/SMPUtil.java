package us.teaminceptus.noobysmp.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.google.gson.Gson;

public class SMPUtil {
		
	private final static HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
	
	public static class APIPlayer {
		
		public String name;
		public String id;
		
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

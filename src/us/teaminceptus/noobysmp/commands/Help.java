package us.teaminceptus.noobysmp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import us.teaminceptus.noobysmp.SMP;

public class Help implements CommandExecutor {
	
	protected SMP plugin;
	
	public Help(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("help").setExecutor(this);
	}
	
	public static ItemStack getHelpBook() {
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();
		
		List<BaseComponent[]> pages = new ArrayList<>();
		
		pages.add(new BaseComponent[] {new TextComponent("NoobySMP (TheNoobyGodsSMP) is a public survival Minecraft server owned by TheNoobyGods and developed by Team Inceptus.\n\n"
				+ "This SMP features a wide range of content, featuring:\n"
				+ "- Custom Tables\n"
				+ "- Custom Recipes & Items\n"
				+ "- New Dimensions\n"
				+ "And More!\n\n"
				+ "We hope you enjoy the content we have provided!")
				});
			
		TextComponent discordLink = new TextComponent(ChatColor.BLUE + "https://discord.io/thenoobygods");
		discordLink.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new Text(ChatColor.AQUA + "Click here to join")));
		discordLink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.io/thenoobygods"));
		
		TextComponent inceptusLink = new TextComponent(ChatColor.BLUE + "https://teaminceptus.us");
		inceptusLink.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new Text(ChatColor.AQUA + "Click here to visit")));
		inceptusLink.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://teaminceptus.us"));
		
		pages.add(new BaseComponent[] {
			new TextComponent(ChatColor.UNDERLINE + "Media" + ChatColor.RESET + "\n\n"),
			new TextComponent(ChatColor.RESET + "Discord: "), discordLink,
			new TextComponent(ChatColor.RESET + "Team Inceptus: "), inceptusLink,
		});
		
		pages.add(new BaseComponent[] {
			new TextComponent("For bug reporting / technical difficulties, please contact a member of Team Inceptus on Discord.")
		});
		
		meta.setPages(new ArrayList<>());
		meta.spigot().setPages(pages);
		meta.setTitle(ChatColor.GREEN + "NoobySMP Help Book");
		meta.setAuthor("GamerCoder215");
		book.setItemMeta(meta);
		
		return book;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player p)) return false;
		
		p.openBook(getHelpBook());
		p.playSound(p, Sound.ITEM_BOOK_PAGE_TURN, 3F, 1F);
		return true;
	}
	
}

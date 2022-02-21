package us.teaminceptus.noobysmp.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import us.teaminceptus.noobysmp.SMP;

public class Items implements Listener {
	
	private static final String REMOVE_STRS = "!,.?/\\[]{}()*&^%$#@-=+";
	
	protected SMP plugin;
	
	public Items(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.isCancelled()) return;
		
		if (e.getCurrentItem() == null) return;
		ItemStack item = e.getCurrentItem();
		
		for (ItemStack non : NON_COLLECTIBLES) if (compareLocalization(item, non)) e.setCancelled(true);
	}
	
	public static final ItemStack LOCKED_ITEM = itemBuilder(Material.BARRIER).setName(ChatColor.RED + "Locked!").build();
	public static final ItemStack COMING_SOON = itemBuilder(Material.BEDROCK).setName(ChatColor.DARK_PURPLE + "Coming Soon!").build();
	public static final ItemStack NOT_ENOUGH_XP = itemBuilder(Material.BARRIER).setName(ChatColor.RED + "Not Enough Experience!").build();
	
	
	public static final ItemStack fromNBT(String nbtStr) {
		try {
			CompoundTag nbt =  TagParser.parseTag(nbtStr);
			net.minecraft.world.item.ItemStack nmsItem = net.minecraft.world.item.ItemStack.of(nbt);
			org.bukkit.inventory.ItemStack item = CraftItemStack.asBukkitCopy(nmsItem);
			return item;
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static final String getLocalization(ItemStack input) {
		if (!(input.hasItemMeta())) return null;
		return (input.getItemMeta().getLocalizedName());
	}
	
	public static final boolean compareLocalization(ItemStack first, ItemStack second) {
		return getLocalization(first).equals(getLocalization(second));
	}

	public static ItemStack getHead(String texture) {
	  ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
	  SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
	  GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
	  gameProfile.getProperties().put("textures", new Property("textures", texture));
	  try {
	    Method method = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
	    method.setAccessible(true);
	    method.invoke(skullMeta, gameProfile);
	  } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
	    ex.printStackTrace();
	  }
	
	  itemStack.setItemMeta(skullMeta);
	  return itemStack;
	}
	
	public static class Builder {

		ItemStack item;

		Builder(Material type) {
			this.item = new ItemStack(type);
		}

		public Builder setName(String display) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(display);
			String localizedName = ChatColor.stripColor(display).toLowerCase().replace(' ', '_');
			
			for (Character c : REMOVE_STRS.toCharArray()) localizedName.replace(c.toString(), "");
			
			meta.setLocalizedName(localizedName);
			item.setItemMeta(meta);
			return this;
		}

		public Builder setLore(List<String> lore) {
			ItemMeta meta = item.getItemMeta();
			meta.setLore(lore);
			item.setItemMeta(meta);
			return this;
		}

		public Builder addGlint() {
			ItemMeta meta = item.getItemMeta();
			meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			return this;
		}

		public ItemStack build() {
			return this.item;
		}
		
	}
	
	public static Builder itemBuilder(Material type) {
		return new Builder(type);
	}

	public static class Inventory {

		public static final ItemStack GUI_PANE = itemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build();
		public static final ItemStack NEXT_ARROW = itemBuilder(Material.ARROW).setName(ChatColor.GREEN + "Next").build();
		public static final ItemStack BACK_ARROW = itemBuilder(Material.ARROW).setName(ChatColor.RED + "Back").build();
	}
	
	public static final ItemStack[] NON_COLLECTIBLES = {
		LOCKED_ITEM,
		COMING_SOON,
		Inventory.BACK_ARROW,
		Inventory.GUI_PANE,
		Inventory.NEXT_ARROW
	};
	
}
package us.teaminceptus.noobysmp.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

public class Items implements Listener {
	
	private static final String REMOVE_STRS = "!,.?/\\[]{}()*&^%$#@-=+";
	
	protected final SMP plugin;
	
	public Items(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public static ItemStack noName(Material m) {
		return itemBuilder(m).setName(" ").setLocalizedName("no_name").build();
	}
	
	public static final ItemStack LOCKED_ITEM = itemBuilder(Material.BARRIER).setName(ChatColor.RED + "Locked!").build();
	public static final ItemStack COMING_SOON = itemBuilder(Material.BEDROCK).setName(ChatColor.DARK_PURPLE + "Coming Soon!").build();
	public static final ItemStack NOT_ENOUGH_XP = itemBuilder(Material.BARRIER).setName(ChatColor.RED + "Not Enough Experience!").build();
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getCurrentItem() == null) return;
		ItemStack item = e.getCurrentItem();
		if (!(item.hasItemMeta())) return;

		for (ItemStack non : NON_COLLECTIBLES) if (item.isSimilar(non)) e.setCancelled(true);
	}

	@EventHandler
	public void onMove(InventoryMoveItemEvent e) {
		if (e.getItem() == null) return;
		ItemStack item = e.getItem();
		if (!(item.hasItemMeta())) return;

		for (ItemStack non : NON_COLLECTIBLES) if (item.isSimilar(non)) e.setCancelled(true);
		if (e.getDestination().getHolder() instanceof CancelHolder) e.setCancelled(true);
	}

	public static ItemStack fromNBT(String nbtStr) {
		try {
			CompoundTag nbt =  TagParser.parseTag(nbtStr);
			net.minecraft.world.item.ItemStack nmsItem = net.minecraft.world.item.ItemStack.of(nbt);
			return CraftItemStack.asBukkitCopy(nmsItem);
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getLocalization(ItemStack input) {
		if (!(input.hasItemMeta())) return null;
		return (input.getItemMeta().getLocalizedName());
	}
	
	public static boolean compareLocalization(ItemStack first, ItemStack second) {
		if (getLocalization(first) == null) return false;
		if (getLocalization(second) == null) return false;
		
		if (SMPMaterial.getByItem(first) == SMPMaterial.getByItem(second)) return true;
		if (AbilityItem.getByItem(first) == AbilityItem.getByItem(second)) return true;
		
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

		final ItemStack item;

		Builder(Material type) {
			this.item = new ItemStack(type);
		}
		
		Builder(ItemStack starter) {
			this.item = starter.clone();
		}
		
		public Builder setName(String display) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(display);
			String localizedName = ChatColor.stripColor(display).toLowerCase().replace(' ', '_');
			
				for (Character c : REMOVE_STRS.toCharArray()) localizedName = localizedName.replace(c.toString(), "");
			
			meta.setLocalizedName(localizedName);
			item.setItemMeta(meta);
			return this;
		}

		public Builder setType(Material type) {
			item.setType(type);
			return this;
		}

		public Builder setLocalizedName(String localized) {
			ItemMeta meta = item.getItemMeta();
			meta.setLocalizedName(localized);
			item.setItemMeta(meta);
			return this;
		}

		public Builder setLore(List<String> lore) {
			ItemMeta meta = item.getItemMeta();
			meta.setLore(lore);
			item.setItemMeta(meta);
			return this;
		}
		
		public Builder addFlags(ItemFlag... flags) {
			ItemMeta meta = item.getItemMeta();
			meta.addItemFlags(flags);
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
	
	public static Builder itemBuilder(ItemStack starter) {
		return new Builder(starter);
	}

	public interface Inventory {

		ItemStack GUI_PANE = noName(Material.BLACK_STAINED_GLASS_PANE);
		ItemStack NEXT_ARROW = itemBuilder(Material.ARROW).setName(ChatColor.GREEN + "Next").build();
		ItemStack BACK_ARROW = itemBuilder(Material.ARROW).setName(ChatColor.RED + "Back").build();
	
		ItemStack BACK_HEAD = itemBuilder(getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ=="))
				.setName(ChatColor.RED + "Back").build();
		
		ItemStack FORWARD_HEAD = itemBuilder(getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19"))
				.setName(ChatColor.GREEN + "Forward").build();
	}
	
	public interface Tags {
		
		ItemStack TOO_MANY_TAGS = errorItem("You have too many Tags!");
		ItemStack NOT_COMPATIBLE = errorItem("This tag is not compatible with this item.");
	}

	public static ItemStack errorItem(String message) {
		return itemBuilder(Material.BARRIER).setName(ChatColor.RED + message).build();
	}
	
	public static final ItemStack[] NON_COLLECTIBLES = {
		LOCKED_ITEM,
		COMING_SOON,
		Inventory.BACK_ARROW,
		Inventory.GUI_PANE,
		Inventory.NEXT_ARROW,
		Inventory.BACK_HEAD,
		Inventory.FORWARD_HEAD,
		Tags.TOO_MANY_TAGS,
		Tags.NOT_COMPATIBLE
	};
	
}
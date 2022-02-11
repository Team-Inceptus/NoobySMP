package us.teaminceptus.noobysmp.util;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {

	public static final ItemStack LOCKED_ITEM = itemBuilder(Material.BARRIER).setName(ChatColor.RED + "Locked!").build();
	
	public static class Builder {

		ItemStack item;

		Builder(Material type) {
			this.item = new ItemStack(type);
		}

		public Builder setName(String display) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(display);
			meta.setLocalizedName(ChatColor.stripColor(display).toLowerCase().replace(" ", "_"));
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
	
}
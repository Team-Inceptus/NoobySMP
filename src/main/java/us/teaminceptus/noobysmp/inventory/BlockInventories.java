package us.teaminceptus.noobysmp.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.collect.ImmutableMap;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.inventoryholder.BlockHolder;

public class BlockInventories implements Listener {
	
	protected final SMP plugin;
	
	public BlockInventories(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public static Inventory getEnchantingTable(Player p) {
		Inventory inv = Generator.genGUI(54, "Enchanting Table", new BlockHolder(Material.ENCHANTING_TABLE));
		PlayerConfig config = new PlayerConfig(p);
		
		for (int i = 10; i < 44; i++) inv.setItem(i, Items.Inventory.GUI_PANE);
		
		inv.setItem(13, null);
		inv.setItem(22, new ItemStack(Material.ENCHANTING_TABLE));
		
		for (int i = 28; i < 35; i += 2) inv.setItem(i, null);
		for (int i = 37; i < 44; i += 2) inv.setItem(i, null);
		
		ItemStack playerInfo = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta pMeta = (SkullMeta) playerInfo.getItemMeta();
		pMeta.setDisplayName(ChatColor.GOLD + p.getName());
		pMeta.setOwningPlayer(p);
		pMeta.setLore(Arrays.asList(
				ChatColor.AQUA + Integer.toString(p.getTotalExperience()) + " Experience",
				ChatColor.DARK_AQUA + "Level " + config.getLevel()));
		
		playerInfo.setItemMeta(pMeta);
		
		return inv;
	}
	
	private static final Map<Material, Inventory> inventoryMap = ImmutableMap.<Material, Inventory>builder()
			.build();
	
	@EventHandler
	public void onCraft(PrepareItemCraftEvent e) {
		// Repair with Enchants
		CraftingInventory inv = e.getInventory();
		if (e.isRepair()) {
			ItemStack baseResult = e.getInventory().getResult();
			
			List<Map<Enchantment, Integer>> newEnchants = new ArrayList<>();
			
			for (ItemStack item : inv.getMatrix()) {
				if (item == null) continue;
				if (!(item.hasItemMeta())) continue;
				if (!(item.getItemMeta().hasEnchants())) continue;
				
				newEnchants.add(item.getEnchantments());
			}
			
			for (Map<Enchantment, Integer> enchant : newEnchants) baseResult.addUnsafeEnchantments(enchant);
			
			inv.setResult(baseResult);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.useInteractedBlock() == Result.DENY) return;
		if (e.getClickedBlock() == null) return;
		Player p = e.getPlayer();
		Block b = e.getClickedBlock();
		
		Material type = b.getType();
		if (inventoryMap.get(type) == null) return;
		
		p.openInventory(inventoryMap.get(type));
	}

}

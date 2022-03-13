package us.teaminceptus.noobysmp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.recipes.RecipeManager;
import us.teaminceptus.noobysmp.util.Messages;

public class GetRecipe implements TabExecutor {
	
	protected SMP plugin;
	
	public GetRecipe(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("getrecipe").setExecutor(this);
		plugin.getCommand("getrecipe").setTabCompleter(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player p)) {
			sender.sendMessage(Messages.PLAYER_ONLY_CMD);
			return false;
		}
		
		if (args.length < 1) {
			if (p.getInventory().getItemInMainHand() == null) {
				p.sendMessage(Messages.ARGUMENT_ITEM);
				return false;
			}
			
			ItemStack target = p.getInventory().getItemInMainHand();
			
			List<Inventory> invs = RecipeManager.getRecipeMenus(target);
			if (invs.size() < 1) {
				p.sendMessage(ChatColor.RED + "There are no recipes for this given item.");
				p.sendMessage(ChatColor.AQUA + "Hint: Hold the item you want to get the recipe for, or provide a material ID.");
			}
			
			p.openInventory(invs.get(0));
			return true;
		} else {
			try {
				SMPMaterial m = SMPMaterial.matchEnum(args[0].toLowerCase());
				
				if (m != null) {
					List<Inventory> invs = RecipeManager.getRecipeMenus(m.getItem());
					p.openInventory(invs.get(0));
					return true;
				}
				
				AbilityItem aM = AbilityItem.matchEnum(args[0].toLowerCase());
				
				if (aM != null) {
					List<Inventory> invs = RecipeManager.getRecipeMenus(aM.getItem());
					p.openInventory(invs.get(0));
					return true;
				}
				
				p.sendMessage(ChatColor.RED + "This item does not exist. If this is an error, contact GamerCoder215.");
				return true;
			} 
			catch (IndexOutOfBoundsException e) {
				p.sendMessage(ChatColor.RED + "No recipes were found for this item.");
				return false;
			}
			catch (Exception e) {
				p.sendMessage(ChatColor.RED + "There was an error: " + e.getMessage());
				return false;
			}
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> suggestions = new ArrayList<>();
		if (args.length == 1) {
			for (SMPMaterial m : SMPMaterial.values()) suggestions.add(m.name().toLowerCase());
			for (AbilityItem m : AbilityItem.values()) suggestions.add(m.name().toLowerCase());
		}
		
		return suggestions;
	}

}

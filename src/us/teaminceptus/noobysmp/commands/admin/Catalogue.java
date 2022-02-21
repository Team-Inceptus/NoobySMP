package us.teaminceptus.noobysmp.commands.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.util.Messages;

public class Catalogue implements TabExecutor {
	
	protected SMP plugin;
	
	public Catalogue(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("catalogue").setExecutor(this);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> suggestions = new ArrayList<>();
		
		switch (args.length) {
			case 1: {
				suggestions.addAll(Arrays.asList("item"));
				return suggestions;
			}
			case 2: {
				if (args[0].equalsIgnoreCase("item")) {
					for (SMPMaterial m : SMPMaterial.values()) suggestions.add("smpmaterial:" + m.getLocalization());
					for (AbilityItem m : AbilityItem.values()) suggestions.add("smpability:" + m.getLocalization());
				}
				
				return suggestions;
			}
		}
		
		return suggestions;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player p)) {
			sender.sendMessage(Messages.PLAYER_ONLY_CMD);
			return true;
		}
		
		if (!(p.hasPermission("smp.admin.catalogue"))) {
			p.sendMessage(Messages.NO_PERMISSION_CMD);
			return true;
		}
		
		if (args.length < 1) {
			p.sendMessage(ChatColor.RED + "Please provide a valid category.");
			return false;
		}
		
		switch (args[0].toLowerCase()) {
			case "item": {
				if (args.length < 2) {
					p.sendMessage(Messages.ARGUMENT_ITEM);
					return false;
				}
				
				String id = args[1].toLowerCase().replace("smpmaterial:", "").replace("smpability:", "");
				
				if (SMPMaterial.getByLocalization(id) != null) {
					p.getInventory().addItem(SMPMaterial.getByLocalization(id).getItem());
					p.playSound(p, Sound.ENTITY_ARROW_HIT_PLAYER, 3F, 1F);
				} else if (AbilityItem.getByLocalization(id) != null) {
					p.getInventory().addItem(AbilityItem.getByLocalization(id).getItem());
					p.playSound(p, Sound.ENTITY_ARROW_HIT_PLAYER, 3F, 1F);
				}
				
				break;
			}
		}
		
		return true;
	}
	


}

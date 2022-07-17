package us.teaminceptus.noobysmp.commands.admin;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.entities.SMPEntity;
import us.teaminceptus.noobysmp.entities.bosses.SMPBoss;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.util.Messages;

public class Catalogue implements TabExecutor {
	
	protected final SMP plugin;
	
	public Catalogue(SMP plugin) {
		this.plugin = plugin;
		plugin.getCommand("catalogue").setExecutor(this);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> suggestions = new ArrayList<>();

		switch (args.length) {
			case 1 -> {
				suggestions.addAll(Arrays.asList("item", "entity"));
				return suggestions;
			}
			case 2 -> {
				if (args[0].equalsIgnoreCase("item")) {
					for (SMPMaterial m : SMPMaterial.values()) suggestions.add("smpmaterial:" + m.getLocalization());
					for (AbilityItem m : AbilityItem.values()) suggestions.add("smpability:" + m.getLocalization());
				} else if (args[0].equalsIgnoreCase("entity")) {
					for (Class<? extends SMPEntity<?>> clazz : SMPEntity.CLASS_LIST)
						suggestions.add("entity:" + clazz.getSimpleName().toLowerCase());
					for (Class<? extends SMPEntity<?>> clazz : SMPEntity.TITAN_CLASS_LIST)
						suggestions.add("titan:" + clazz.getSimpleName().toLowerCase());
					for (Class<? extends SMPBoss<?>> clazz : SMPBoss.CLASS_LIST)
						suggestions.add("boss:" + clazz.getSimpleName().toLowerCase());
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
			case "item" -> {
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
			case "entity" -> {
				if (args.length < 2) {
					p.sendMessage(ChatColor.RED + "Please provide a valid entity.");
					return false;
				}

				String id = args[1].toLowerCase().replace("titan:", "").replace("entity:", "").replace("boss:", "");

				try {
					for (Class<? extends SMPEntity<?>> clazz : SMPEntity.CLASS_LIST)
						if (clazz.getSimpleName().equalsIgnoreCase(id)) {
							Constructor<?> constr = clazz.getConstructor(Location.class);
							constr.newInstance(p.getLocation());
							p.playSound(p, Sound.ENTITY_WITHER_SPAWN, 3F, 1F);
							return true;
						}

					for (Class<? extends SMPEntity<?>> clazz : SMPEntity.TITAN_CLASS_LIST)
						if (clazz.getSimpleName().equalsIgnoreCase(id)) {
							Constructor<?> constr = clazz.getConstructor(Location.class);
							constr.newInstance(p.getLocation());
							p.playSound(p, Sound.ENTITY_WITHER_SPAWN, 3F, 1F);
							return true;
						}

					for (Class<? extends SMPBoss<?>> clazz : SMPBoss.CLASS_LIST)
						if (clazz.getSimpleName().equalsIgnoreCase(id)) {
							Constructor<?> constr = clazz.getConstructor(Location.class);
							constr.newInstance(p.getLocation());
							p.playSound(p, Sound.ENTITY_WITHER_SPAWN, 3F, 1F);
							return true;
						}
				} catch (Exception e) {
					p.sendMessage(ChatColor.RED + "There was an error: " + e.getMessage());
					return false;
				}
				break;
			}
		}
		
		return true;
	}
	


}

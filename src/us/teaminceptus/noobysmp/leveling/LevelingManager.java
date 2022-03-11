package us.teaminceptus.noobysmp.leveling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Fire;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.ability.cosmetics.SMPCosmetic;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.SMPUtil;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

public class LevelingManager implements Listener {
    
    protected SMP plugin;

    public LevelingManager(SMP plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static enum LevelingType {

        LEVEL(0, Material.DIAMOND_SWORD),
        FARMING(1, Material.IRON_HOE),
        FLETCHING(2, Material.ARROW)
        ;

        private final int id;
        private final Material m;

        private LevelingType(int id, Material m) {
            this.id = id;
            this.m = m;
        }

        public int getId() {
            return this.id;
        }
        
        public Material getIcon() {
        	return this.m;
        }

        public static LevelingType getById(int id) {
            for (LevelingType type : values()) if (type.id == id) return type;
            return null;
        }
        
        public static LevelingType getByIcon(Material id) {
            for (LevelingType type : values()) if (type.m == id) return type;
            return null;
        }
    }

    public static ItemStack getLevelInfo(int level, OfflinePlayer p, LevelingType type) {
        PlayerConfig config = new PlayerConfig(p);
        if (level < 0) return Items.Inventory.GUI_PANE;
        if (level == 0) return new ItemStack(type.getIcon());
        
        switch (type) {
            case FARMING: {
                ItemStack farming = Items.itemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName(ChatColor.YELLOW + "Farming Level " + Integer.toString(level)).build();
                ItemMeta meta = farming.getItemMeta();

                List<String> lore = new ArrayList<>();

                if (level % 25 == 0 && level <= 100) {
                    lore.add(ChatColor.DARK_PURPLE + "+1 Farming Drops");
                }

                if (level % 15 == 0 && level <= 90) {
                    lore.add(ChatColor.AQUA + "+10 Farming Find");
                }

                if (level % 5 == 0) {
                    lore.add(ChatColor.DARK_RED + "+10% Hoe Damage");
                }

                if (level % 30 == 0 && level <= 120) {
                    lore.add(ChatColor.GOLD + "+1 Harvest Radius");
                }

                meta.setLore(lore);
                farming.setItemMeta(meta);

                return farming;
            }
            case FLETCHING: {
                ItemStack fletching = Items.itemBuilder(Material.WHITE_STAINED_GLASS_PANE).addGlint().setName(ChatColor.YELLOW + "Fletching Level " + Integer.toString(level)).build();
                ItemMeta meta = fletching.getItemMeta();

                List<String> lore = new ArrayList<>();

                if (level % 5 == 0 && level <= 100) {
                    lore.add(ChatColor.DARK_PURPLE + "+10% Projectile Velocity");
                }

                if (level % 25 == 0 && level <= 100) {
                    lore.add(ChatColor.GREEN + "+1 Projectile Shot");
                }

                if (level % 20 == 0 && level <= 100) {
                    lore.add(ChatColor.DARK_GREEN + "+20% Chance of Projectile Bounce");
                }

                if (level % 15 == 0 && level < 100) {
                    lore.add(ChatColor.DARK_AQUA + "+10% Projectile Deflect");
                }

                if (level % 8 == 0 && level <= 8 * 20) {
                    lore.add(ChatColor.RED + "+5% Projectile Chain");
                }

                if (level == 80) {
                    lore.add(ChatColor.GOLD + "Homing Projectiles");
                }

                meta.setLore(lore);
                fletching.setItemMeta(meta);

                return fletching;
            }
            case LEVEL: {
                ItemStack levelI = Items.itemBuilder(Material.CYAN_STAINED_GLASS_PANE).addGlint().setName(ChatColor.YELLOW + "Level " + Integer.toString(level)).build();
                ItemMeta meta = levelI.getItemMeta();

                List<String> lore = new ArrayList<>();
                lore.add(config.getLevel() > level ? ChatColor.GREEN + SMPUtil.withSuffix(PlayerConfig.toMinExperience(level)) : ChatColor.GOLD + SMPUtil.withSuffix(config.getExperience()) + " / " + SMPUtil.withSuffix(PlayerConfig.toMinExperience(level)));

                List<String> perks = new ArrayList<>();
                for (SMPMaterial m : SMPMaterial.values()) if (m.getLevelUnlocked() == level) perks.add(m.getDisplayName() + " Recipe");
                for (AbilityItem m : AbilityItem.values()) if (m.getLevelUnlocked() == level) perks.add(m.getDisplayName() + " Recipe");
                for (SMPCosmetic c : SMPCosmetic.values()) if (c.getLevelUnlocked() == level) perks.add(ChatColor.AQUA + c.getName() + " Cosmetic");

                if (perks.size() > 6) {
                    lore.add(perks.get(0));
                    lore.add(perks.get(1));
                    lore.add(perks.get(2));
                    lore.add(perks.get(3));
                    lore.add(perks.get(4));
                    lore.add(perks.get(5));
                    lore.add(ChatColor.WHITE + "" + ChatColor.ITALIC + "And More...");
                } else {
                    lore.addAll(perks);
                }
                
                meta.setLore(lore);
                levelI.setItemMeta(meta);

                return levelI;
            }
            default:
                return null;
            
        }
    }
    
    
    public static class LevelHolder extends CancelHolder {};

    public static Inventory getLevelingMenu(Player p, LevelingType type) {
    	PlayerConfig config = new PlayerConfig(p);
        final int level;

        switch (type) {
            case FARMING:
                level = config.getFarmingLevel();
                break;
            case FLETCHING:
                level = config.getFletchingLevel();
                break;
            case LEVEL:
                level = config.getLevel();
                break;
            default:
                level = config.getLevel();
                break;
        }
        
        return getLevelingMenu(p, type, level);
    }
    
    public static Inventory getLevelingMenu(Player p, LevelingType type, int level) {
        Inventory inv = Generator.genGUI(45, p.getDisplayName() + ChatColor.RESET + " - Leveling", new LevelHolder());
        PlayerConfig config = new PlayerConfig(p);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(p);
        meta.setDisplayName(p.getPlayerListName());
        meta.setLore(ImmutableList.<String>builder()
        .add(ChatColor.AQUA + "Level " + Integer.toString(config.getLevel()))
        .add(" ")
        .add(ChatColor.GOLD + "Farming Level " + Integer.toString(config.getFarmingLevel()))
        .add(ChatColor.YELLOW + "Fletching Level " + Integer.toString(config.getFletchingLevel()))
        .build());

        head.setItemMeta(meta);
        inv.setItem(4, head);
        
        inv.setItem(18, (level <= 5 ? Items.Inventory.GUI_PANE : Items.Inventory.BACK_HEAD));
        inv.setItem(26, Items.Inventory.FORWARD_HEAD);

        inv.setItem(9, getLevelInfo(level - 4, p, type));
        inv.setItem(10, getLevelInfo(level - 3, p,  type));
        inv.setItem(11, getLevelInfo(level - 2, p, type));
        inv.setItem(12, getLevelInfo(level - 1, p, type));
        
        inv.setItem(13, level == 0 ? new ItemStack(type.getIcon()) : getLevelInfo(level, p, type));

        inv.setItem(14, getLevelInfo(level + 1, p, type));
        inv.setItem(15, getLevelInfo(level + 2, p, type));
        inv.setItem(16, getLevelInfo(level + 3, p, type));
        inv.setItem(17, getLevelInfo(level + 4, p, type));
        
        int splitPositon = type == LevelingType.LEVEL ? 1 : 2;
        
        // Highlight Current Level
        for (int i = 9; i < 18; i++) {
        	if (inv.getItem(i) == null) continue;
        	try {
	        	int currentLevel = Integer.parseInt(ChatColor.stripColor(inv.getItem(i).getItemMeta().getDisplayName()).split(" ")[splitPositon]);
	        	
	        	if (currentLevel == config.getLevel()) {
	        		ItemStack clone = inv.getItem(i);
	        		ItemMeta cmeta = clone.getItemMeta();
	        		cmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	        		cmeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
	        		clone.setItemMeta(cmeta);
	        		
	        		inv.setItem(i, clone);
	        		break;
	        	}
        	} catch (IllegalArgumentException | IndexOutOfBoundsException err) {
        		continue;
        	}
        }
        
        // Level-Specific Perks
        
        switch (type) {
			case FARMING: {
				
				break;
			}
			case FLETCHING: {
				int velocity = (int) Math.floor(config.getFletchingLevel() / 5) * 10;
				ItemStack velocityI = Items.itemBuilder(Material.SPECTRAL_ARROW).setName(ChatColor.GREEN + "+" + velocity + "% Arrow Velocity").build();
				inv.setItem(20, velocityI);
				
				int shots = (int) Math.floor(config.getFletchingLevel() / 25);
				ItemStack shotsI = Items.itemBuilder(Material.BOW).setName(ChatColor.GOLD + "+" + shots + " Projectile Shots").build();
				inv.setItem(21, shotsI);
				
				int bounceChance = (int) Math.min(Math.floor(config.getFletchingLevel() / 20) * 20, 100);
				ItemStack bounceI = Items.itemBuilder(Material.SLIME_BALL).setName(ChatColor.DARK_GREEN + "+" + bounceChance + "% Bounce Chance").build();
				inv.setItem(22, bounceI);
				
				int deflect = (int) Math.min(Math.floor(config.getFletchingLevel() / 15) * 10, 60);
				ItemStack deflectI = Items.itemBuilder(Material.DIAMOND_CHESTPLATE).setName(ChatColor.RED + "+" + deflect + "% Deflect Chance").build();
				inv.setItem(23, deflectI);
				
				int chainChance = (int) Math.min(Math.floor(config.getFletchingLevel() / 8) * 5, 100);
				ItemStack chainI = Items.itemBuilder(Material.CHAIN).setName(ChatColor.DARK_AQUA + "+" + chainChance + "% Chain Arrow Chance").build();
				inv.setItem(24, chainI);
				
				if (config.getFletchingLevel() >= 80) {
					inv.setItem(31, Items.itemBuilder(Material.WITHER_SKELETON_SKULL).setName(ChatColor.GOLD + "Homing Arrows").build());
				} else inv.setItem(31, Items.LOCKED_ITEM);
				
				break;
			}
			case LEVEL: {
				break;
			}
			default:
				break;
	        	
        }
        
        return inv;
    }
    
    // Inventory Management
    
    @EventHandler
    public void onClick(InventoryClickEvent e) {
    	if (!(e.getWhoClicked() instanceof Player p)) return;
    	InventoryView view = e.getView();
    	ItemStack clickedItem = e.getCurrentItem();
    	
    	Inventory inv = view.getTopInventory();
    	
    	if (!(inv.getHolder() instanceof LevelHolder)) return;
    	if (clickedItem == null) return;
    	
    	if (clickedItem.getType() == Material.PLAYER_HEAD) {
    		LevelingType type;
    		
    		switch (inv.getItem(17).getType()) {
    			case YELLOW_STAINED_GLASS_PANE:
    				type = LevelingType.FARMING;
    				break;
    			case WHITE_STAINED_GLASS_PANE: 
    				type = LevelingType.FLETCHING;
    				break;
    			default:
    				type = LevelingType.LEVEL;
    				break;
    		}
    		
    		int splitPosition = type == LevelingType.LEVEL ? 1 : 2;
    		
    		if (clickedItem.getItemMeta().getLocalizedName().equalsIgnoreCase("back")) {
    			int lastLevel = Integer.parseInt(ChatColor.stripColor(inv.getItem(13).getItemMeta().getDisplayName()).split(" ")[splitPosition]);
    			
    			p.openInventory(getLevelingMenu(p, type, lastLevel - 1));
    			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 3F, 0.5F);
    		} else if (clickedItem.getItemMeta().getLocalizedName().equalsIgnoreCase("forward")) {
    			String[] display = ChatColor.stripColor(inv.getItem(13).getItemMeta().getDisplayName()).split(" ");
    			
    			final int nextLevel;
    			
    			if (display.length == 1) nextLevel = 1;
    			else nextLevel = Integer.parseInt(display[splitPosition]);
    			
    			p.openInventory(getLevelingMenu(p, type, nextLevel + 1));
    			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 3F, 2F);
    		}
    		
    		
    	}
    	
    }
    
    // Leveling a Player

    private static Random r = new Random();

    @EventHandler
    public void onKill(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p)) return;
        if (!(e.getEntity() instanceof LivingEntity en)) return;

        if (en.getHealth() - e.getFinalDamage() > 0) return;

        PlayerConfig config = new PlayerConfig(p);

        if (en instanceof Player target) {
            config.addExperience(target.getTotalExperience() / r.nextInt(10) + 10);
        } else {
            config.addExperience(en.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() / r.nextInt(10) + 10);
        }
    }

    @EventHandler
    public void onAdvance(PlayerAdvancementDoneEvent e) {
        Player p = e.getPlayer();
        PlayerConfig config = new PlayerConfig(p);
        config.addExperience(30 * r.nextDouble());
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent e) {
        Player p = e.getEnchanter();
        PlayerConfig config = new PlayerConfig(p);
        int eLevel = 0;
        for (int i : e.getEnchantsToAdd().values()) eLevel += i;

        config.addExperience(eLevel * 1.5 * r.nextDouble());
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getState() != State.CAUGHT_ENTITY && e.getState() != State.CAUGHT_FISH) return;
        Player p = e.getPlayer();
        PlayerConfig config = new PlayerConfig(p);

        if (e.getCaught() != null && e.getCaught() instanceof LivingEntity en) {
            config.addExperience(en.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * r.nextDouble() * 2);
        } else {
            config.addExperience(35 * r.nextDouble());
        }
    }

    @EventHandler
    public void onExp(PlayerExpChangeEvent e) {
        Player p = e.getPlayer();
        PlayerConfig config = new PlayerConfig(p);

        config.addExperience(e.getAmount() * r.nextDouble());
    }

    // Abilities Because of Level
    
    // Farming

    private static final Map<Material, Integer> FINDABLES = ImmutableMap.<Material, Integer>builder()
    .put(Material.COAL, 50)
    .put(Material.DIAMOND, 10)
    .put(Material.EMERALD, 15)
    .put(Material.IRON_INGOT, 40)
    .put(Material.GOLD_INGOT, 20)
    .put(Material.GOLD_NUGGET, 35)
    .put(Material.POTATO, 90)
    .put(Material.CARROT, 80)
    .put(Material.BREAD, 75)
    .put(Material.GOLDEN_APPLE, 15)
    .put(Material.APPLE, 70)
    .put(Material.BEETROOT, 95)
    .put(Material.STICK, 90)
    .put(Material.ARROW, 80)
    .put(Material.DIRT, 100)
    .put(Material.STONE, 80)
    .put(Material.AMETHYST_SHARD, 13)
    .put(Material.OBSIDIAN, 5)
    .put(Material.NETHERITE_INGOT, 2)
    .put(Material.NETHERITE_SCRAP, 4)
    .put(Material.NETHERRACK, 20)
    .put(Material.COARSE_DIRT, 100)
    .put(Material.GRAVEL, 80)
    .put(Material.HONEYCOMB, 50)
    .put(Material.GLASS_BOTTLE, 60)
    .put(Material.COMPASS, 10)
    .build();

    private static List<ItemStack> calculateFarmingDrops(Player p, Block b) {
        List<ItemStack> drops = new ArrayList<>();

        return drops;
    }

    @EventHandler
    public void onFarm(BlockBreakEvent e) {
        Player p = e.getPlayer();
        PlayerConfig config = new PlayerConfig(p);
        Block b = e.getBlock();
        
        if (!(b.getBlockData() instanceof Ageable) || b.getBlockData() instanceof Fire) return;

        e.setDropItems(false);
        ItemStack tool = p.getEquipment().getItemInMainHand();

        List<ItemStack> drops = calculateFarmingDrops(p, b);

        drops.addAll(b.getDrops(tool, p)); // Players will always break with their main hand
        
        // Break Radius
        int y = b.getY();
        double radius = config.getHarvestDiameter() - 1 / 2;
        BoundingBox area = new BoundingBox(radius, y, radius, -radius, y, -radius);
        
        for (double dx = area.getMinX(); dx < area.getMaxX(); dx++) {
            int x = (int) Math.floor(dx);
            for (double dz = area.getMinZ(); dz < area.getMaxZ(); dz++) {
                int z = (int) Math.floor(dz);
                Block relative = b.getWorld().getBlockAt(x, y, z);

                if (relative.getBlockData() instanceof Ageable && !(relative.getBlockData() instanceof Fire)) {
                	relative.breakNaturally();
                	drops.addAll(calculateFarmingDrops(p, relative));
                }
            }
        }
        // Farming Find
        for (int i = 0; i < config.getHarvestDiameter(); i++) {
            Material chosen = FINDABLES.keySet().stream().toList().get( r.nextInt(FINDABLES.size() ) );
            
            if (r.nextInt(100) < FINDABLES.get(chosen)) {
                drops.add(new ItemStack(chosen, r.nextInt(2) + 1));
            }
        }

        // Farming Count
        for (int i = 0; i < Math.min(Math.floor(config.getFarmingLevel() / 10), 4); i++) {
            List<ItemStack> clone = new ArrayList<>(drops);
            drops.addAll(clone);
        }

        for (ItemStack item : drops)
            b.getWorld().dropItemNaturally(b.getLocation(), item);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player p)) return;
        PlayerConfig config = new PlayerConfig(p);

        e.setDamage(config.calculateHoeDamage(p.getInventory().getItemInMainHand(), e.getDamage())); // Attacking will always be in the main hand
    }

    // Fletching

    @EventHandler
    public void onShoot(ProjectileLaunchEvent e) {
        Projectile proj = e.getEntity();
        if (!(proj.getShooter() instanceof Player p)) return;
        PlayerConfig config = new PlayerConfig(p);

        List<Projectile> targets = new ArrayList<>();
        targets.add(proj);
        
        // Projectile Shots
        for (int i = 0; i < (int) Math.floor(config.getFletchingLevel() / 25); i++) {
        	Location cloneL = proj.getLocation();
        	cloneL.setDirection(p.getLocation().getDirection());
            Projectile clone = proj.getWorld().spawn(cloneL, proj.getClass());
            targets.add(clone);
        }
        
        // Velocity
        double velocityBuff = Math.floor(config.getFletchingLevel() / 8) * 0.1;
        if (velocityBuff > 0) {
        	for (Projectile project : targets) {
        		project.setVelocity(proj.getVelocity().multiply(1 + velocityBuff));
        	}
        }
        
        // Homing
        if (config.getLevel() >= 80) {
            for (Projectile projectile : targets) {
                List<Entity> nearbyEntities = projectile.getNearbyEntities(30, 15, 30).stream().filter(en -> en instanceof LivingEntity target && !(en instanceof ArmorStand) && !(target.getUniqueId().equals(p.getUniqueId()))).toList();
                if (nearbyEntities.size() < 1) continue;;
                Map<Double, Entity> nearestEntities = new HashMap<>();
                for (Entity en : nearbyEntities) nearestEntities.put(en.getLocation().distanceSquared(projectile.getLocation()), en);
                Entity target = (nearestEntities.keySet().size() < 1 || java.util.Collections.min(nearestEntities.keySet()) == null ? nearbyEntities.get(0) : nearestEntities.get(java.util.Collections.min(nearestEntities.keySet())));
                if (target.getLocation().distanceSquared(p.getLocation()) >= 100) continue;
                if (target.getWorld().rayTraceEntities(p.getLocation(), p.getLocation().getDirection(), 10, en -> en instanceof LivingEntity ltarget && !(en instanceof ArmorStand) && !(ltarget.getUniqueId().equals(p.getUniqueId()))) == null) continue;
                new BukkitRunnable() {
                    public void run() {
                        if (target.isDead()) cancel();
                        if (target instanceof Player p && (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR)) cancel();
                        if ((projectile instanceof Arrow arr && arr.isInBlock()) || projectile.isDead() || projectile.getLocation().distanceSquared(p.getLocation()) >= 2500) cancel();
                        if (nearbyEntities.size() > 0) {
                            moveToward(projectile, (target instanceof LivingEntity ltarget ? ltarget.getEyeLocation() : target.getLocation()), 1 + (config.getFletchingLevel() - 80 / 10));
                        }
                    }
                }.runTaskTimer(plugin, 1, 1);
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        Projectile proj = e.getEntity();
        if (!(proj.getShooter() instanceof Player p)) return;
        PlayerConfig config = new PlayerConfig(p);

        if (e.getHitBlock() != null) {
            // Bounce
            double chance = Math.floor(config.getLevel() / 20) * 20;

            if (r.nextInt(100) < chance ) {
                if (proj instanceof Fireball f) {
                    f.setDirection(f.getDirection().multiply(-1));
                } else if (proj instanceof ThrowableProjectile || proj instanceof AbstractArrow) {
                    Location newLoc = proj.getLocation();
                    newLoc.setDirection(newLoc.getDirection().multiply(-1));
                    proj.teleport(newLoc);

                    proj.setVelocity(proj.getVelocity().multiply(0.8));
                }
            }

        } else if (e.getHitEntity() != null) {
            // Chain
            double chance = (Math.floor(config.getFletchingLevel() / 8) / 20) * 100;

            if (r.nextInt(100) < chance) {
                Location clone1L = proj.getLocation();
                clone1L.setYaw(clone1L.getYaw() + 30);
                Projectile clone1 = proj.getWorld().spawn(clone1L, proj.getClass());
                clone1.setShooter(p);

                Location clone2L = proj.getLocation();
                clone2L.setYaw(clone2L.getYaw() + 150);
                Projectile clone2 = proj.getWorld().spawn(clone2L, proj.getClass());
                clone2.setShooter(p);

                Location clone3L = proj.getLocation();
                clone2L.setYaw(clone3L.getYaw() + 270);
                Projectile clone3 = proj.getWorld().spawn(clone3L, proj.getClass());
                clone3.setShooter(p);

                Bukkit.getPluginManager().callEvent(new ProjectileLaunchEvent(clone1));
                Bukkit.getPluginManager().callEvent(new ProjectileLaunchEvent(clone2));
                Bukkit.getPluginManager().callEvent(new ProjectileLaunchEvent(clone3));
            }
        }
    }

    @EventHandler
    public void onDamageProj(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Projectile proj)) return;
        if (!(e.getEntity() instanceof Player p)) return;

        PlayerConfig config = new PlayerConfig(p);

        double chance = Math.floor(config.getLevel() / 15) * 10;
        
        if (r.nextInt(100) < chance) {
            e.setCancelled(true);
            proj.remove();
            Location reverse = proj.getLocation();
            reverse.setDirection(reverse.getDirection().multiply(-1));
            Projectile clone = proj.getWorld().spawn(reverse, proj.getClass());
            clone.setVelocity(clone.getVelocity().multiply(1.25));

            p.playSound(p, Sound.ITEM_SHIELD_BLOCK, 3F, 1F);
        }
    }

	private static void moveToward(Entity entity, Location to, double speed){
		Location loc = entity.getLocation();
		double x = loc.getX() - to.getX();
		double y = loc.getY() - to.getY();
		double z = loc.getZ() - to.getZ();
		Vector velocity = new Vector(x, y, z).normalize().multiply(-speed);
		entity.setVelocity(velocity);   
	}
}

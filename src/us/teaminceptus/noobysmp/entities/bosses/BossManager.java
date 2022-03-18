package us.teaminceptus.noobysmp.entities.bosses;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.google.common.collect.ImmutableList;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Description;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Drop;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Experience;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.HP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.NotGeneratabele;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.SpawnCost;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Tier;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.CancelChance;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Defensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.MinionSpawn;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Offensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Repeated;
import us.teaminceptus.noobysmp.entities.bosses.npc.NPCBoss;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

public class BossManager implements Listener {
    
    // private static final int T5_UNLOCK = 50;
    private static final int T4_UNLOCK = 35;
    private static final int T3_UNLOCK = 20;
    private static final int T2_UNLOCK = 10;
    private static final int T1_UNLOCK = 5;
    protected SMP plugin;

    
    private final BukkitRunnable REPEATED_ATTACKS = new BukkitRunnable() {
    	public void run() {
    		for (World w : Bukkit.getWorlds()) {
    			for (LivingEntity e : w.getEntitiesByClass(LivingEntity.class)) {
    				if (SMPBoss.getByUUID(e.getUniqueId()) == null && NPCBoss.getByUUID(e.getUniqueId()) == null) continue;
    				
    				final 
    				Class<?> bossClass;
    				if (SMPBoss.getByUUID(e.getUniqueId()) != null) {
    					bossClass = SMPBoss.getByUUID(e.getUniqueId()).getClass();
    				} else if (NPCBoss.getByUUID(e.getUniqueId()) != null) {
    					bossClass = NPCBoss.getByUUID(e.getUniqueId()).getClass();
    				} else bossClass = null;
    				
    				if (bossClass == null) continue;
    				
    				for (Method m : bossClass.getDeclaredMethods()) {
    					if (m.isAnnotationPresent(Repeated.class)) {
    						Repeated r = m.getAnnotation(Repeated.class);
    						
    						new BukkitRunnable() {
    							public void run() {
    								if (e.isDead()) cancel();
    								
    								try {
    									m.invoke(SMPBoss.getByUUID(e.getUniqueId()) == null ? NPCBoss.getByUUID(e.getUniqueId()) : SMPBoss.getByUUID(e.getUniqueId()));
    								} catch (Exception err) {
    									plugin.getLogger().info("Error executing repeated attack " + m.getName() + " in class " + bossClass.getName());
    									err.printStackTrace();
    								}
    							}
    						}.runTaskTimer(plugin, r.value(), r.value());
    					}
    				}
    			}
    		}
    	}
    };
    
    public BossManager(SMP plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        REPEATED_ATTACKS.runTaskTimer(plugin, 2, 2);
        plugin.getLogger().info("Loaded Repeated Attacks Runnable");
    }
  
    
	public static void throwItem(Location start, double distance, ItemStack item, double damage) {
		Vector direction = start.getDirection();
		
		ArmorStand stand = start.getWorld().spawn(start, ArmorStand.class);
		
		stand.setInvulnerable(true);
		stand.setInvisible(true);
		stand.setBasePlate(false);
		stand.setArms(true);
		stand.setGravity(false);
		
		stand.getEquipment().setItemInMainHand(item);
		stand.setRightArmPose(EulerAngle.ZERO);
		
		double tpRange = 0.2;
		
		new BukkitRunnable() {
			int runs = 0;
			
			public void run() {
				
				if (stand.isDead()) cancel();
				// Rotate Arm
				double currentRot = stand.getRightArmPose().getX();
				stand.setRightArmPose(new EulerAngle(currentRot + 0.5, 0, 0));
				
				// Move
				
				Location target = stand.getLocation().add(direction.normalize().multiply(tpRange));
				
				if (!(target.getBlock().isPassable())) {
					stand.remove();
					cancel();
				}
				
				stand.teleport(target);
				
				// Damage
				List<LivingEntity> targets = new ArrayList<>();
				
				for (Entity en : stand.getNearbyEntities(0.75, 0.75, 0.75)) if (en instanceof LivingEntity len) targets.add(len);
				
				for (LivingEntity len : targets) len.damage(damage, stand);
				
				if (runs >= (1 / tpRange) * distance) {
					stand.remove();
					cancel();
				}
				
				runs++;
			}
		}.runTaskTimer(JavaPlugin.getPlugin(SMP.class), 0, 1);
	}

    private static class BossHolder extends CancelHolder {

        private BossHolder() {
        }
    }

    private static class BossMenuHolder extends CancelHolder {

        private BossMenuHolder() {
        }
    }

    private static Random r = new Random();

    public static Inventory getBossMenu(Player p) {
        Inventory inv = Generator.genGUI(27, "SMP Bosses", new BossMenuHolder());
        
        inv.setItem(10, Items.Inventory.GUI_PANE);
        inv.setItem(16, Items.Inventory.GUI_PANE);

        PlayerConfig config = new PlayerConfig(p);
        
        ItemStack t1 = Items.itemBuilder(Material.WOODEN_SWORD).addGlint().addFlags(ItemFlag.HIDE_ATTRIBUTES).setName(ChatColor.AQUA + "Tier 1").build();
        ItemStack t2 = Items.itemBuilder(Material.STONE_SWORD).addGlint().addFlags(ItemFlag.HIDE_ATTRIBUTES).setName(ChatColor.AQUA + "Tier 2").build();
        ItemStack t3 = Items.itemBuilder(Material.IRON_SWORD).addGlint().addFlags(ItemFlag.HIDE_ATTRIBUTES).setName(ChatColor.AQUA + "Tier 3").build();
        ItemStack t4 = Items.itemBuilder(Material.DIAMOND_SWORD).addGlint().addFlags(ItemFlag.HIDE_ATTRIBUTES).setName(ChatColor.AQUA + "Tier 4").build();
        // ItemStack t5 = Items.itemBuilder(Material.NETHERITE_SWORD).addGlint().addFlags(ItemFlag.HIDE_ATTRIBUTES).setName(ChatColor.AQUA + "Tier 5").build();
        
        ItemStack locked = Items.LOCKED_ITEM;

        inv.setItem(11, config.getLevel() < T1_UNLOCK ? locked : t1);
        inv.setItem(12, config.getLevel() < T2_UNLOCK ? locked : t2);
        inv.setItem(13, config.getLevel() < T3_UNLOCK ? locked : t3);
        inv.setItem(14, config.getLevel() < T4_UNLOCK ? locked : t4);
        inv.setItem(15, Items.COMING_SOON);

        return inv;
    }

    public static Inventory getBosses(Player p, int selectedTier) {
        Inventory inv = Generator.genGUI(54, "SMP Bosses - Tier " + Integer.toString(selectedTier), new BossHolder());

        for (Class<? extends SMPBoss<? extends Mob>> bossClass : SMPBoss.CLASS_LIST) createBoss(selectedTier, inv, bossClass);
        for (Class<? extends NPCBoss> npcClass : NPCBoss.NPC_BOSS_LIST) createBoss(selectedTier, inv, npcClass);

        return inv;
    }

    private static void createBoss(int selectedTier, Inventory inv, Class<?> bossClass) {
        try {
            if (bossClass.isAnnotationPresent(NotGeneratabele.class))
                return;

            int tier = bossClass.getAnnotation(Tier.class).value();
            if (tier != selectedTier)
                return;

            Material icon = bossClass.getAnnotation(Icon.class).value();

            List<String> description = new ArrayList<>();

            for (String s : bossClass.getAnnotation(Description.class).value()) description.add(ChatColor.translateAlternateColorCodes('&', ChatColor.GRAY + s));
            
            Map<ItemStack, Integer> spawnCost = new HashMap<>();
            for (String s : bossClass.getAnnotation(SpawnCost.class).value()) {
                String id = s.split(":")[0];
                int count = Integer.parseInt(s.split(":")[1]);

                Material m = Material.matchMaterial(id.toUpperCase());
                SMPMaterial m2 = SMPMaterial.getByLocalization(id.toLowerCase());
                AbilityItem m3 = AbilityItem.getByLocalization(id.toLowerCase());
                if (m != null) {
                    spawnCost.put(new ItemStack(m), count);
                } else if (m2 != null) {
                    spawnCost.put(m2.getItem(), count);
                } else if (m3 != null) {
                    spawnCost.put(m3.getItem(), count);
                }
            }

            DisplayName display = bossClass.getAnnotation(DisplayName.class);

            String displayName = display.cc() + display.value();

            List<String> spawnCostList = new ArrayList<>();

            for (Map.Entry<ItemStack, Integer> entry : spawnCost.entrySet()) {
                ItemStack item = entry.getKey();
                spawnCostList.add((item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : ChatColor.WHITE + WordUtils.capitalize(item.getType().name().toLowerCase().replace('_', ' ')))
                + ChatColor.GRAY + " x" + Integer.toString(entry.getValue()));
            }

            List<String> drops = new ArrayList<>();

            for (Drop d : bossClass.getAnnotationsByType(Drop.class)) {
                String am = d.amount();
                final String amount;

                if (am.contains("-")) {
                    amount = " x" + am.split("-")[0] + " - x" + am.split("-")[1];
                } else amount = " x" + am;

                if (SMPMaterial.getByLocalization(d.drop().toLowerCase()) != null) {
                    SMPMaterial m = SMPMaterial.getByLocalization(d.drop().toLowerCase());

                    drops.add(m.getDisplayName() + ChatColor.GRAY + amount + ChatColor.DARK_GRAY + " (" + d.chance() + "%)");
                } else if (AbilityItem.getByLocalization(d.drop().toLowerCase()) != null) {
                    AbilityItem m = AbilityItem.getByLocalization(d.drop().toLowerCase());

                    drops.add(m.getDisplayName() + ChatColor.GRAY + amount + ChatColor.DARK_GRAY + " (" + d.chance() + "%)");
                } else if (Material.getMaterial(d.drop().toUpperCase()) != null) {
                    Material m = Material.getMaterial(d.drop().toUpperCase());

                    String mName = WordUtils.capitalizeFully(m.name().replace('_', ' '));
                    drops.add(ChatColor.WHITE + mName + ChatColor.GRAY + amount + ChatColor.DARK_GRAY + " (" + d.chance() + "%)");
                }
            }

            
            HP healthA = bossClass.getAnnotation(HP.class);
            double health = healthA.value();
            
            DecimalFormat df = new DecimalFormat("#.00");
            df.setGroupingUsed(true);
            df.setGroupingSize(3);
            
            ItemStack item = new ItemStack(icon);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GRAY + "Tier " + Integer.toString(tier) + " Boss - " + displayName);
            meta.setLore(ImmutableList.<String>builder()
            .add(ChatColor.RED + df.format(health) + " HP")
            .add(" ")
            .addAll(description)
            .add(" ")
            .add(ChatColor.DARK_GRAY + "" + ChatColor.UNDERLINE + "Spawn Cost")
            .add(" ")
            .addAll(spawnCostList)
            .add(" ")
            .add(ChatColor.DARK_GRAY + "" + ChatColor.UNDERLINE + "Drops")
            .add(" ")
            .addAll(drops)
            .build());
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
            meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
            item.setItemMeta(meta);
            
            inv.addItem(item);
        } catch (Exception e) {
            JavaPlugin.getPlugin(SMP.class).getLogger().info("Error constructing entity " + bossClass.getName());
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (e.getCurrentItem() == null) return;
        ItemStack clickedItem = e.getCurrentItem();

        InventoryView view = e.getView();
        
        Inventory inv = view.getTopInventory();

        if (inv.getHolder() instanceof BossHolder) {

            Class<?> bossClass = (SMPBoss.getByIcon(clickedItem.getType()) != null ? SMPBoss.getByIcon(clickedItem.getType()) : NPCBoss.getByIcon(clickedItem.getType()));
            if (bossClass == null) return;

            SpawnCost cost = bossClass.getAnnotation(SpawnCost.class);
            Map<ItemStack, Integer> itemRequirements = new HashMap<>();
            Map<Material, Integer> matRequirements = new HashMap<>();

            for (String s : cost.value()) {
                String id = s.split(":")[0];
                int amount = Integer.parseInt(s.split(":")[1]);

                if (Material.matchMaterial(id.toUpperCase()) != null) {
                    matRequirements.put(Material.matchMaterial(id.toUpperCase()), amount);
                } else {
                    if (SMPMaterial.getByLocalization(id.toLowerCase()) != null) {
                        itemRequirements.put(SMPMaterial.getByLocalization(id.toLowerCase()).getItem(), amount);
                    } else if (AbilityItem.getByLocalization(id.toLowerCase()) != null) {
                        itemRequirements.put(AbilityItem.getByLocalization(id.toLowerCase()).getItem(), amount);
                    }
                }
            }

            for (Material m : matRequirements.keySet()) {
                if (!(p.getInventory().contains(m, matRequirements.get(m)))) {
                    p.sendMessage(ChatColor.RED + "You are missing: " + m.name() + "!");
                    return;
                }
            }

            for (ItemStack item : itemRequirements.keySet()) {
                if (!(p.getInventory().containsAtLeast(item, itemRequirements.get(item)))) {
                    p.sendMessage(ChatColor.RED + "You are missing: " + (item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name()));
                    return;
                }
            }

            try {
                bossClass.getConstructor(Location.class).newInstance(p.getLocation());
                p.playSound(p, Sound.ENTITY_WITHER_SPAWN, 3F, 1F);
                p.closeInventory();
                
                for (ItemStack item : itemRequirements.keySet()) {
                	item.setAmount(itemRequirements.get(item));
                	p.getInventory().removeItem(item);
                }
                
                for (Material m : matRequirements.keySet()) {
                	ItemStack stack = new ItemStack(m);
                	stack.setAmount(matRequirements.get(m));
                	p.getInventory().removeItem(stack);
                }
            } catch (Exception err) {
                plugin.getLogger().info("Error spawning entity " + bossClass.getName());
                err.printStackTrace();
            }
        } else if (inv.getHolder() instanceof BossMenuHolder) {
        	if (!(Arrays.asList(BOSSES_SWORDS).contains(clickedItem.getType()))) return;
        	
        	int tier = Integer.parseInt(ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).split(" ")[1]);
        	
        	p.openInventory(getBosses(p, tier));
        }
    }
    
    private static final Material[] BOSSES_SWORDS = {
    	Material.DIAMOND_SWORD,
    	Material.WOODEN_SWORD,
    	Material.IRON_SWORD,
    	Material.STONE_SWORD,
    	Material.NETHERITE_SWORD
    };

    @EventHandler
    public void onDamageDefensive(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) return;
        if (!(e.getDamager() instanceof Player p)) return;
        if (!(e.getEntity() instanceof Mob m)) return;

        if (!(SMPBoss.getBossList().contains(m.getUniqueId()))) return;

        SMPBoss<? extends Mob> boss = SMPBoss.getByUUID(m.getUniqueId());

        for (Method method : boss.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Defensive.class)) {
                Defensive def = method.getAnnotation(Defensive.class);
                
                if (r.nextInt(100) < def.chance()) {
                    try {
                       method.invoke(boss, p);
                    } catch (Exception err) {
                        plugin.getLogger().info("Error executing attack " + method.getName() + " in " + boss.getClass().getName());
                        err.printStackTrace();
                    }
                }
            }
        }

        try {
           Constructor<?> constructor = boss.getClass().getConstructor(Location.class);

           for (Annotation ann : constructor.getAnnotations()) {
               if (ann.annotationType().equals(MinionSpawn.class)) {
                   MinionSpawn[] spawns = constructor.getAnnotationsByType(MinionSpawn.class);
                   
                   for (MinionSpawn spawn : spawns) {
	                   if (r.nextInt(100) < spawn.chance()) {
	                       m.getWorld().spawnEntity(m.getLocation(), spawn.type());   
	                   }
                   }
               } else if (ann.annotationType().equals(CancelChance.class)) {
            	   CancelChance cancel = constructor.getAnnotation(CancelChance.class);
            	   
            	   if (r.nextInt(100) < cancel.value()) {
            		   e.setCancelled(true);
            		   p.sendMessage(m.getCustomName() + ChatColor.DARK_RED + " has blocked your attack!");
            	   }
               }
           }
        } catch (Exception err) {
            plugin.getLogger().info("Error executing non-method attack in class " + boss.getClass().getName());
            err.printStackTrace();
        }
    }

    @EventHandler
    public void onDamageOffensive(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) return;
        if (!(e.getEntity() instanceof Player p)) return;
        if (!(e.getDamager() instanceof Mob) && !(e.getDamager() instanceof Projectile)) return;

        Mob m = (e.getDamager() instanceof Projectile proj ? (proj.getShooter() instanceof Mob source ? source : null) : (Mob) e.getDamager());

        if (m == null) return;

        if (!(SMPBoss.getBossList().contains(m.getUniqueId()))) return;

        SMPBoss<? extends Mob> boss = SMPBoss.getByUUID(m.getUniqueId());

        for (Method method : boss.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Offensive.class)) {
                Offensive off = method.getAnnotation(Offensive.class);
                
                if (r.nextInt(100) < off.chance()) {
                    try {
                       method.invoke(boss, p);
                    } catch (Exception err) {
                        plugin.getLogger().info("Error executing attack " + method.getName() + " in " + boss.getClass().getName());
                        err.printStackTrace();
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onDeath(EntityDeathEvent e) {
    	if (!(e.getEntity() instanceof Mob m)) return;
    	
    	SMPBoss<? extends Mob> boss = SMPBoss.getByUUID(m.getUniqueId());
    	if (boss == null) return;
    	
    	try {
    		Constructor<?> constr = boss.getClass().getConstructor(Location.class);
    		
    		Experience exp = constr.getAnnotation(Experience.class);
    		
    		e.setDroppedExp(exp.value());
    		for (ItemStack drop : boss.getBossDrops().keySet()) {
    			if (r.nextInt(100) < boss.getBossDrops().get(drop)) m.getWorld().dropItemNaturally(m.getLocation(), drop);
    		}
    	} catch (Exception err) {
    		plugin.getLogger().info("Error dropping drops for entity " + boss.getClass().getName());
    		err.printStackTrace();
    	}
    }

}

package us.teaminceptus.noobysmp.entities.bosses;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Description;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.DisplayName;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.NotGeneratabele;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.SpawnCost;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Tier;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Defensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.MinionSpawn;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Offensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Repeated;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;

public class BossManager implements Listener {
    
    private static final int T5_UNLOCK = 50;
    private static final int T4_UNLOCK = 35;
    private static final int T3_UNLOCK = 20;
    private static final int T2_UNLOCK = 10;
    private static final int T1_UNLOCK = 5;
    protected SMP plugin;

    public BossManager(SMP plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private static class BossHolder implements InventoryHolder {

        private BossHolder() {
        }

        @Override
        public Inventory getInventory() {
            return null;
        }
        
    }

    private static class BossMenuHolder implements InventoryHolder {

        private BossMenuHolder() {
        }

        @Override
        public Inventory getInventory() {
            return null;
        }
    }

    private static Random r = new Random();

    public static Inventory getBossMenu(Player p) {
        Inventory inv = Generator.genGUI(27, "SMP Bosses", new BossMenuHolder());

        inv.setItem(10, Items.Inventory.GUI_PANE);
        inv.setItem(16, Items.Inventory.GUI_PANE);

        PlayerConfig config = new PlayerConfig(p);
        
        ItemStack t1 = Items.itemBuilder(Material.WOODEN_SWORD).addGlint().setName(ChatColor.AQUA + "Tier 1").build();
        ItemStack t2 = Items.itemBuilder(Material.STONE_SWORD).addGlint().setName(ChatColor.AQUA + "Tier 2").build();
        ItemStack t3 = Items.itemBuilder(Material.IRON_SWORD).addGlint().setName(ChatColor.AQUA + "Tier 3").build();
        ItemStack t4 = Items.itemBuilder(Material.DIAMOND_SWORD).addGlint().setName(ChatColor.AQUA + "Tier 4").build();
        ItemStack t5 = Items.itemBuilder(Material.NETHERITE_SWORD).addGlint().setName(ChatColor.AQUA + "Tier 5").build();
        
        ItemStack locked = Items.LOCKED_ITEM;

        inv.setItem(11, config.getLevel() < T1_UNLOCK ? locked : t1);
        inv.setItem(12, config.getLevel() < T2_UNLOCK ? locked : t2);
        inv.setItem(13, config.getLevel() < T3_UNLOCK ? locked : t3);
        inv.setItem(14, config.getLevel() < T4_UNLOCK ? locked : t4);
        inv.setItem(15, config.getLevel() < T5_UNLOCK ? locked : t5);

        return inv;
    }

    public static Inventory getBosses(Player p, int selectedTier) {
        Inventory inv = Generator.genGUI(54, "SMP Bosses - Tier " + Integer.toString(selectedTier), new BossHolder());

        for (Class<? extends SMPBoss<? extends Mob>> bossClass : SMPBoss.CLASS_LIST) {
            try {
                if (bossClass.isAnnotationPresent(NotGeneratabele.class)) continue;

                int tier = bossClass.getAnnotation(Tier.class).value();
                if (tier != selectedTier) continue;

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

                ItemStack item = new ItemStack(icon);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.DARK_GRAY + "Tier " + Integer.toString(tier) + " Boss - " + displayName);
                meta.setLore(ImmutableList.<String>builder()
                .add(" ")
                .addAll(description)
                .add(" ")
                .add(ChatColor.DARK_GRAY + "" + ChatColor.UNDERLINE + "Spawn Cost")
                .add(" ")
                .addAll(spawnCostList)
                .build());
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES);
                meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
                item.setItemMeta(meta);
            } catch (Exception e) {
                JavaPlugin.getPlugin(SMP.class).getLogger().info("Error constructing entity " + bossClass.getName());
                e.printStackTrace();
            }
        }

        return inv;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (e.getCurrentItem() == null) return;
        ItemStack clickedItem = e.getCurrentItem();

        InventoryView view = e.getView();
        
        Inventory inv = view.getTopInventory();

        if (inv.getHolder() instanceof BossHolder) {

            Class<? extends SMPBoss<? extends Mob>> bossClass = SMPBoss.getByIcon(clickedItem.getType());
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
            } catch (Exception err) {
                plugin.getLogger().info("Error spawning entity " + bossClass.getName());
                err.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        if (!(e.getEntity() instanceof Mob m)) return;

        if (!(SMPBoss.getBossList().contains(m.getUniqueId()))) return;

        SMPBoss<? extends Mob> boss = SMPBoss.getByUUID(m.getUniqueId());

        for (Method method : boss.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Repeated.class)) {
                Repeated rep = method.getAnnotation(Repeated.class);

                new BukkitRunnable() {
                    public void run() {
                        if (m.isDead()) cancel();

                        try {
                            method.invoke(boss);
                        } catch (Exception err) {
                            plugin.getLogger().info("Error executing repeated task " + method.getName() + " in " + boss.getClass().getName());
                            err.printStackTrace();
                        }
                    }
                }.runTaskTimer(plugin, rep.interval(), rep.interval());
            }
        }
    }

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
                   MinionSpawn spawn = constructor.getAnnotation(MinionSpawn.class);

                   if (r.nextInt(100) < spawn.chance()) {
                       m.getWorld().spawnEntity(m.getLocation(), spawn.type());
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

}

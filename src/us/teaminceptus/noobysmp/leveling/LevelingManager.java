package us.teaminceptus.noobysmp.leveling;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Fire;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.BoundingBox;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.ability.cosmetics.SMPCosmetic;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.SMPColor;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

public class LevelingManager implements Listener {
    
    protected SMP plugin;

    public LevelingManager(SMP plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static enum LevelingType {

        LEVEL(0),
        FARMING(1),
        FLETCHING(2)
        ;

        private final int id;

        private LevelingType(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public static LevelingType getById(int id) {
            for (LevelingType type : values()) if (type.id == id) return type;
            return null;
        }
    }

    public static ItemStack getLevelInfo(int level, OfflinePlayer p, LevelingType type) {
        PlayerConfig config = new PlayerConfig(p);
        if (level < 0) return Items.Inventory.GUI_PANE;

        switch (type) {
            case FARMING: {
                ItemStack farming = Items.itemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName(ChatColor.YELLOW + "Farming Level " + Integer.toString(level)).build();
                ItemMeta meta = farming.getItemMeta();

                List<String> lore = new ArrayList<>();

                if (level % 25 == 0 && level <= 100) {
                    lore.add(ChatColor.DARK_PURPLE + "+1 Farming Drops");
                }

                if (level % 15 == 0) {
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
                lore.add(PlayerConfig.toMinExperience(level) < config.getExperience() ? ChatColor.YELLOW + Double.toString(Math.floor(config.getExperience() * 100) / 100) + " / " + Double.toString(PlayerConfig.toMinExperience(level)) : ChatColor.GREEN + Double.toString(PlayerConfig.toMinExperience(level)));

                if (level % 5 == 0) {
                    lore.add(ChatColor.DARK_PURPLE + "+10% Projectile Velocity");
                }

                if (level % 25 == 0) {
                    lore.add(ChatColor.GREEN + "+1 Projectile Shot");
                }

                if (level % 20 == 0) {
                    lore.add(ChatColor.DARK_GREEN + "+20% Chance of Projectile Bounce");
                }

                if (level % 15 == 0) {
                    lore.add(ChatColor.DARK_AQUA + "+10% Projectile Deflect");
                }

                if (level % 8 == 0) {
                    lore.add(ChatColor.RED + "+5% Projectile Chain");
                }

                if (level == 80) {
                    lore.add(ChatColor.GOLD + "Homing Projectiles");
                }

                if (level == 25) {
                    lore.add(ChatColor.BLUE + "Infinity");
                }

                meta.setLore(lore);
                fletching.setItemMeta(meta);

                return fletching;
            }
            case LEVEL: {
                ItemStack levelI = Items.itemBuilder(Material.CYAN_STAINED_GLASS_PANE).addGlint().setName(ChatColor.YELLOW + "Level " + Integer.toString(level)).build();
                ItemMeta meta = levelI.getItemMeta();

                List<String> lore = new ArrayList<>();
                

                List<String> perks = new ArrayList<>();
                for (SMPMaterial m : SMPMaterial.values()) if (m.getLevelUnlocked() == level) perks.add(m.getName() + " Recipe");
                for (AbilityItem m : AbilityItem.values()) if (m.getLevelUnlocked() == level) perks.add(m.getName() + " Recipe");
                for (SMPCosmetic c : SMPCosmetic.values()) if (c.getLevelUnlocked() == level) perks.add(c.getName() + " Cosmetic");

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



    public static Inventory getLevelingMenu(Player p, LevelingType type) {
        Inventory inv = Generator.genGUI(45, p.getDisplayName() + ChatColor.RESET + " - Leveling", new CancelHolder());
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

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(p);
        meta.setDisplayName(p.getPlayerListName());
        meta.setLore(ImmutableList.<String>builder()
        .add(ChatColor.AQUA + "Level " + Integer.toString(config.getLevel()))
        .add(" ")
        .add(ChatColor.GOLD + "Farming " + Integer.toString(config.getFarmingLevel()))
        .add(ChatColor.YELLOW + "Fletching " + Integer.toString(config.getFletchingLevel()))
        .build());

        head.setItemMeta(meta);
        inv.setItem(3, head);

        inv.setItem(9, getLevelInfo(level - 4, p, type));
        inv.setItem(10, getLevelInfo(level - 3, p,  type));
        inv.setItem(11, getLevelInfo(level - 2, p, type));
        inv.setItem(12, getLevelInfo(level - 1, p, type));

        ItemStack currentLevel = getLevelInfo(level, p,  type);
        ItemMeta cmeta = currentLevel.getItemMeta();
        cmeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        cmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        currentLevel.setItemMeta(cmeta);
        inv.setItem(13, currentLevel);

        inv.setItem(14, getLevelInfo(level + 1, p, type));
        inv.setItem(15, getLevelInfo(level + 2, p, type));
        inv.setItem(16, getLevelInfo(level + 3, p, type));
        inv.setItem(17, getLevelInfo(level + 4, p, type));

        // Switch Type

        ItemStack levelI = Items.itemBuilder(Material.DIAMOND_SWORD).setName(ChatColor.AQUA + "Player Level").build();
        ItemStack farmingI = Items.itemBuilder(Material.IRON_HOE).setName(ChatColor.YELLOW + "Farming Level").build();
        ItemStack fletchingI = Items.itemBuilder(Material.ARROW).setName(SMPColor.chatHex("#F5F5DC", "Fletching Level")).build();

        inv.setItem(20, farmingI);
        inv.setItem(21, levelI);
        inv.setItem(22, fletchingI);

        // Statistics



        return inv;
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

    // Normal

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
    .put(Material.IRON_HOE, 20)
    .put(Material.DIAMOND_HOE, 3)
    .put(Material.NETHERITE_HOE, 1)
    .put(Material.DIAMOND_HOE, 3)
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
                    if (relative.breakNaturally(tool)) drops.addAll(calculateFarmingDrops(p, relative));
                }
            }
        }
        // Farming Find
        for (int i = 0; i < config.getHarvestDiameter(); i++) {
            Material chosen = FINDABLES.keySet().stream().toList().get( r.nextInt(FINDABLES.size() ) );
            
            if (r.nextInt(100) < FINDABLES.get(chosen)) {
                drops.add(new ItemStack(chosen, r.nextInt(3) + 1));
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

    // Fletching
}

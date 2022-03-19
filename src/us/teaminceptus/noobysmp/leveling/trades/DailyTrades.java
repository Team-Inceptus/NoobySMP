package us.teaminceptus.noobysmp.leveling.trades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.scheduler.BukkitRunnable;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.util.Messages;
import us.teaminceptus.noobysmp.util.PlayerConfig;

public class DailyTrades implements CommandExecutor {
    
    protected SMP plugin;

    private static final int TRADE_COUNT = 7;
    // 1 Minecraft Day
    private static final long TRADES_UPDATE_SPEED = 20 * 60 * 10;

    public DailyTrades(SMP plugin) {
        DAILY_TRADES = genDailyTrades();

        this.plugin = plugin;
        plugin.getCommand("dailytrades").setExecutor(this);

        TRADES_UPDATE.runTaskTimer(plugin, TRADES_UPDATE_SPEED, TRADES_UPDATE_SPEED);
    }

    public static Merchant DAILY_TRADES;

    public static final BukkitRunnable TRADES_UPDATE = new BukkitRunnable() {
        public void run() {
            DAILY_TRADES = genDailyTrades();
            Bukkit.broadcastMessage(ChatColor.AQUA + "The Daily Trades have updated! Use /dailytrades to check them out!");
        }
    };

    public static enum DailyTrade {

        OAK_LOG(Material.CRAFTING_TABLE, Material.OAK_LOG),
        STICK(Material.OAK_LOG, new ItemStack(Material.STICK, 8)),
        GOLDEN_APPLE(new ItemStack(Material.IRON_BLOCK, 3), Material.GOLDEN_APPLE),
        GOLD_INGOT(new ItemStack(Material.IRON_INGOT, 4), Material.GOLD_INGOT),
        IRON_SHOVEL(Material.WOODEN_SHOVEL, Material.IRON_INGOT, Material.IRON_SHOVEL),
        DIAMOND_SHOVEL(Material.STONE_SHOVEL, Material.DIAMOND, Material.DIAMOND_SHOVEL),
        NETHERITE_SWORD(Material.IRON_SWORD, new ItemStack(Material.NETHERITE_SCRAP, 6), Material.NETHERITE_SWORD),
        DIAMOND(new ItemStack(Material.AMETHYST_BLOCK, 8), Material.DIAMOND),
        AMETHYST_SHARD(Material.AMETHYST_BLOCK, new ItemStack(Material.AMETHYST_SHARD, 4)),
        IRON_SWORD(Material.WOODEN_SWORD, Material.IRON_INGOT, Material.IRON_SWORD),
        DIAMOND_AXE(Material.STONE_AXE, Material.DIAMOND, Material.DIAMOND_AXE),
        DIAMOND_SWORD(Material.STONE_SWORD, Material.DIAMOND, Material.DIAMOND_SWORD),
        RUBY_SWORD(new ItemStack(Material.DIAMOND_SWORD), SMPMaterial.RUBY.getItem(), SMPMaterial.RUBY_SWORD.getItem()),
        ENDERITE_SWORD(new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.END_STONE, 64), SMPMaterial.ENDERITE_SWORD.getItem()),
        ENDERITE(new ItemStack(Material.DIAMOND, 16), SMPMaterial.ENDERITE.getItem()),
        IRON_INGOT(new ItemStack(Material.COBBLESTONE, 64), new ItemStack(Material.COBBLESTONE, 32), new ItemStack(Material.IRON_INGOT, 2)),
        COBBLESTONE(new ItemStack(Material.STONE, 16), new ItemStack(Material.COBBLESTONE, 16)),
        DIAMOND_2(new ItemStack(Material.EMERALD, 32), Material.DIAMOND),
        RUBY(new ItemStack(Material.EMERALD, 61), SMPMaterial.RUBY.getItem()),
        ENDERITE_2(new ItemStack(Material.EMERALD_BLOCK, 15), SMPMaterial.ENDERITE.getItem()),
        DIAMOND_PICKAXE(Material.IRON_PICKAXE, Material.DIAMOND, Material.DIAMOND_PICKAXE),
        DIAMOND_AXE_2(Material.IRON_AXE, Material.DIAMOND, Material.DIAMOND_AXE),
        RUBY_PICKAXE(new ItemStack(Material.NETHERITE_PICKAXE), new ItemStack(Material.EMERALD, 22), SMPMaterial.RUBY_PICKAXE.getItem()),
        PACKED_ICE(Material.ICE, Material.EMERALD, Material.PACKED_ICE),
        BLUE_ICE(Material.PACKED_ICE, Material.EMERALD_BLOCK, Material.BLUE_ICE),
        EMERALD(SMPMaterial.RUBY.getItem(), new ItemStack(Material.EMERALD, 6)),
        EMERALD_2(SMPMaterial.ENDERITE.getItem(), new ItemStack(Material.EMERALD, 28)),
        EMERALD_SWORD(new ItemStack(Material.NETHERITE_SWORD), new ItemStack(Material.EMERALD, 39), SMPMaterial.EMERALD_SWORD.getItem()),
        BLACKSTONE(new ItemStack(Material.COBBLESTONE, 64), new ItemStack(Material.BLACKSTONE, 6)),
        COPPER(new ItemStack(Material.COAL, 32), new ItemStack(Material.RAW_COPPER, 4)),
        COPPER_2(new ItemStack(Material.IRON_INGOT, 10), new ItemStack(Material.RAW_COPPER, 18)),
        COAL(Material.CHARCOAL, Material.COAL),
        COAL_2(Material.IRON_INGOT, new ItemStack(Material.COAL, 5)),
        COAL_3(Material.DIAMOND, new ItemStack(Material.COAL, 17)),
        DIAMOND_3(SMPMaterial.ENCHANTED_DIAMOND.getItem(), new ItemStack(Material.DIAMOND, 8)),
        GOLD_INGOT_2(SMPMaterial.ENCHANTED_GOLD.getItem(), new ItemStack(Material.GOLD_INGOT, 8)),
        NETHER_STAR(SMPMaterial.NETHERITE_STAR.getItem(), new ItemStack(Material.NETHER_STAR, 2)),

        // Ability Items

        INFINIBALL(new ItemStack(Material.FIRE_CHARGE, 32), Material.TNT, AbilityItem.INFINIBALL.getItem()),
        NETHER_ENRICHMENT(new ItemStack(Material.MAGMA_BLOCK, 64), new ItemStack(Material.FIRE_CHARGE, 32), AbilityItem.NETHER_ENRICHMENT.getItem()),
        SCROLL_ELECTRIC(SMPMaterial.ENDERITE.getItem(32), SMPMaterial.ENCHANTED_DIAMOND.getItem(4), AbilityItem.SCROLL_ELECTRIC.getItem()),

        
        ;

        private final MerchantRecipe recipe;
        private final ItemStack result;

        private DailyTrade(ItemStack ing1, ItemStack ing2, ItemStack result, int maxUses) {
            this.result = result;
            MerchantRecipe recipe = new MerchantRecipe(result, 0, maxUses, true);

            recipe.addIngredient(ing1);
            if (ing2 != null) recipe.addIngredient(ing2);

            this.recipe = recipe;
        }

        private DailyTrade(Material ing1, Material ing2, ItemStack result) {
            this(new ItemStack(ing1), new ItemStack(ing2), result);
        }

        private DailyTrade(Material ing1, ItemStack ing2, Material result) {
            this(new ItemStack(ing1), ing2, new ItemStack(result));
        }

        private DailyTrade(Material ing1, Material ing2, Material result) {
            this(new ItemStack(ing1), new ItemStack(ing2), new ItemStack(result));
        }

        private DailyTrade(Material ing1, Material result) {
            this(new ItemStack(ing1), new ItemStack(result));
        }

        private DailyTrade(ItemStack ing1, Material result) {
            this(ing1, new ItemStack(result));
        }

        private DailyTrade(Material ing1, ItemStack result) {
            this(new ItemStack(ing1), result);
        }

        private DailyTrade(ItemStack ing1, ItemStack ing2, ItemStack result) {
            this(ing1, ing2, result, Integer.MAX_VALUE);
        }

        private DailyTrade(ItemStack ing1, Material ing2, ItemStack result) {
            this(ing1, new ItemStack(ing2), result);
        }

        private DailyTrade(ItemStack ing1, ItemStack result) {
            this(ing1, (ItemStack) null, result);
        }

        private DailyTrade(ItemStack ing1, ItemStack result, int maxUses) {
            this(ing1, null, result, maxUses);
        }

        public final ItemStack getResult() {
            return this.result;
        }

        public final MerchantRecipe getRecipe() {
            return this.recipe;
        }

        private static Random rand = new Random();

        public static List<DailyTrade> pickRandom(int amount) {
            List<DailyTrade> list = new ArrayList<>();

            List<DailyTrade> picked = new ArrayList<>(valuesList());

            for (int i = 0; i < amount; i++) {
                int select = rand.nextInt(picked.size());
                list.add(picked.get(select));
                picked.remove(select);
            }

            return list;
        }

        public static List<MerchantRecipe> pickRandomRecipes(int amount) {
            List<MerchantRecipe> recipes = new ArrayList<>();

            for (DailyTrade d : pickRandom(amount)) recipes.add(d.getRecipe());

            return recipes;
        }

        public static List<DailyTrade> valuesList() {
            return Arrays.asList(values());
        }

    }

    public static Merchant genDailyTrades() {
        Merchant merchant = Bukkit.createMerchant("NoobySMP Daily Trades");
        
        merchant.setRecipes(DailyTrade.pickRandomRecipes(TRADE_COUNT));

        return merchant;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(Messages.PLAYER_ONLY_CMD);
            return false;
        }
        
        PlayerConfig config = new PlayerConfig(p);
        
        Merchant newM = Bukkit.createMerchant("NoobySMP Daily Trades");
        List<MerchantRecipe> recipes = DAILY_TRADES.getRecipes();
        
        List<MerchantRecipe> newRecipes = new ArrayList<>();
        
        for (MerchantRecipe r : recipes) {
        	if (SMPMaterial.getByItem(r.getResult()) != null) {
        		if (SMPMaterial.getByItem(r.getResult()).getLevelUnlocked() <= config.getLevel()) newRecipes.add(r);
        	}
        	
        	if (AbilityItem.getByItem(r.getResult()) != null) {
        		if (AbilityItem.getByItem(r.getResult()).getLevelUnlocked() <= config.getLevel()) newRecipes.add(r);
        	}
        }
        
        newM.setRecipes(newRecipes);
        
        p.openMerchant(newM, true);
        p.playSound(p, Sound.ENTITY_VILLAGER_TRADE, 3F, 1F);
        return false;
    }



}

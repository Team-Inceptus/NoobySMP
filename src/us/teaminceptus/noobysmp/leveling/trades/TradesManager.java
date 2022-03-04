package us.teaminceptus.noobysmp.leveling.trades;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.leveling.LevelingManager.LevelHolder;
import us.teaminceptus.noobysmp.util.Generator;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.PlayerConfig;
import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

public class TradesManager implements Listener {
    
    protected SMP plugin;

    public TradesManager(SMP plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static final Map<UUID, Player> requests = new HashMap<>();

    public static class TradeHolder extends CancelHolder {

        public final Player p1;
        public final Player p2;

        public TradeHolder(Player p1, Player p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    public static class TradeInstance {

        private static final IllegalArgumentException NO_PLAYER = new IllegalArgumentException("player is not included in instance");

        public static final int[] P1_SLOTS = {
            10, 11, 12, 19, 20, 21, 28, 29, 30, 37, 38, 39
        };

        public static final int[] P2_SLOTS = {
            14, 15, 16, 23, 24, 25, 32, 33, 34, 41, 42, 43
        };

        private static final List<TradeInstance> activeTrades = new ArrayList<>();

        private final TradeHolder holder;
        private final Inventory inv;

        public boolean p1Accepted;
        public boolean p2Accepted;

        public TradeInstance(Inventory inv, TradeHolder holder) throws IllegalArgumentException {
            this.holder = holder;
            if (!(inv.getHolder() instanceof TradeHolder)) throw new IllegalArgumentException("inv holder is not TradeHolder");
            this.inv = inv;

            this.p1Accepted = false;
            this.p2Accepted = false;

            activeTrades.add(this);
        }

        public TradeInstance(Inventory inv, TradeHolder holder, boolean p1Accepted, boolean p2Accepted) throws IllegalArgumentException {
            this.holder = holder;
            if (!(inv.getHolder() instanceof TradeHolder)) throw new IllegalArgumentException("inv holder is not TradeHolder");
            this.inv = inv;

            this.p1Accepted = p1Accepted;
            this.p2Accepted = p2Accepted;

            activeTrades.add(this);
        }

        public static TradeInstance createTrade(Player p1, Player p2) {
            TradeHolder holder = new TradeHolder(p1, p2);
            Inventory inv = getTradeInventory(p1, p2, holder);
            
            p1.openInventory(inv);
            p1.playSound(p1, Sound.ENTITY_VILLAGER_TRADE, 3F, 0.5F);
            
            p2.openInventory(inv);
            p2.playSound(p2, Sound.ENTITY_VILLAGER_TRADE, 3F, 0.5F);

            return new TradeInstance(inv, holder);
        }

        public static boolean isInTrade(Player p) {
            return from(p) == null;
        }

        public TradeHolder getHolder() {
            return this.holder;
        }

        public static TradeInstance from(Player p) {
            for (TradeInstance i : activeTrades) {
                if (i.holder.p1.getUniqueId().equals(p.getUniqueId())) return i;
                if (i.holder.p2.getUniqueId().equals(p.getUniqueId())) return i;
            }

            return null;
        }

        public boolean hasEnded() {
            return !(holder.p1.getOpenInventory().getTopInventory() instanceof LevelHolder && holder.p2.getOpenInventory().getTopInventory() instanceof LevelHolder);
        }

        public List<ItemStack> getTrades(Player p) throws IllegalArgumentException {
            List<ItemStack> trades = new ArrayList<>();
            if (p.getUniqueId().equals(holder.p1.getUniqueId())) {
                for (int i : P1_SLOTS) {
                    if (inv.getItem(i) == null) continue;
                    trades.add(inv.getItem(i)); 
                }
            } else if (p.getUniqueId().equals(holder.p2.getUniqueId())) {
                for (int i : P2_SLOTS) {
                    if (inv.getItem(i) == null) continue;
                    trades.add(inv.getItem(i));
                }
            } else throw NO_PLAYER;

            return trades;
        }

        public void setTradeContents(Player target, List<ItemStack> contents) throws IllegalArgumentException {
            if (holder.p1.getUniqueId().equals(target.getUniqueId())) {
                int index = 0;
                for (int i : P1_SLOTS) {
                    if (inv.getItem(i) == null) continue;
                    inv.setItem(i, null);
                    inv.setItem(i, contents.get(index));
                    index++;
                }
            } else if (holder.p2.getUniqueId().equals(target.getUniqueId())) {
                int index = 0;
                for (int i : P2_SLOTS) {
                    if (inv.getItem(i) == null) continue;
                    inv.setItem(i, null);
                    inv.setItem(i, contents.get(index));
                    index++;
                }
            } else throw NO_PLAYER;
        }

        public void removeTrade(Player p, ItemStack chosen) throws IllegalArgumentException {
            List<ItemStack> trades = getTrades(p);
            trades.remove(chosen);
            setTradeContents(p, trades);

            holder.p1.playSound(holder.p1, Sound.ENTITY_VILLAGER_NO, 3F, 1F);
            holder.p2.playSound(holder.p2, Sound.ENTITY_VILLAGER_NO, 3F, 1F);
        }

        public static boolean canTrade(Player p) {
            return new PlayerConfig(p).getLevel() >= 5;
        }

        public void addTrade(Player p, ItemStack chosen) throws IllegalArgumentException {
            List<ItemStack> trades = getTrades(p);
            trades.add(chosen);
            setTradeContents(p, trades);

            holder.p1.playSound(holder.p1, Sound.ENTITY_VILLAGER_TRADE, 3F, 1F);
            holder.p2.playSound(holder.p2, Sound.ENTITY_VILLAGER_TRADE, 3F, 1F);
        }

        public void acceptTrade() {
            holder.p1.closeInventory();
            holder.p2.closeInventory();

            if (getTrades(holder.p2).size() > 0) {
                Collection<ItemStack> leftovers = holder.p1.getInventory().addItem(getTrades(holder.p2).toArray(new ItemStack[0])).values();
                if (leftovers.size() > 0) {
                    for (ItemStack item : leftovers) holder.p1.getWorld().dropItemNaturally(holder.p1.getLocation(), item);
                    holder.p1.sendMessage(ChatColor.YELLOW + "You have some leftovers that didn't fit in your inventory! We'll place those next to you.");
                }
            }

            if (getTrades(holder.p1).size() > 0) {
                Collection<ItemStack> leftovers = holder.p2.getInventory().addItem(getTrades(holder.p1).toArray(new ItemStack[0])).values();
                if (leftovers.size() > 0) {
                    for (ItemStack item : leftovers) holder.p2.getWorld().dropItemNaturally(holder.p2.getLocation(), item);
                    holder.p2.sendMessage(ChatColor.YELLOW + "You have some leftovers that didn't fit in your inventory! We'll place those next to you.");
                }
            }
            
            holder.p1.playSound(holder.p1, Sound.ENTITY_VILLAGER_YES, 3F, 1F);
            holder.p2.playSound(holder.p2, Sound.ENTITY_VILLAGER_YES, 3F, 1F);
        }

        public void markAccepted(Player p) {
            if (holder.p1.getUniqueId().equals(p.getUniqueId())) {
                this.p1Accepted = true;
                ItemStack display = Items.itemBuilder(inv.getItem(3)).setName("Accepted").setType(Material.GREEN_TERRACOTTA).addGlint().build();
                inv.setItem(3, display);
            } else if (holder.p2.getUniqueId().equals(p.getUniqueId())) {
                this.p2Accepted = true;
                ItemStack display = Items.itemBuilder(inv.getItem(7)).setName("Accepted").setType(Material.GREEN_TERRACOTTA).addGlint().build();
                inv.setItem(7, display);
            }

            if (p1Accepted && p2Accepted) {
                acceptTrade();
            } else {
                holder.p1.playSound(holder.p1, Sound.ENTITY_VILLAGER_TRADE, 3F, 1F);
                holder.p2.playSound(holder.p2, Sound.ENTITY_VILLAGER_TRADE, 3F, 1F);
            }
        }

        public void cancelTrade(Player clicker) {
            holder.p1.closeInventory();
            holder.p2.closeInventory();

            if (getTrades(holder.p1).size() > 0) {
                Collection<ItemStack> leftovers = holder.p1.getInventory().addItem(getTrades(holder.p1).toArray(new ItemStack[0])).values();
                if (leftovers.size() > 0) {
                    for (ItemStack item : leftovers) holder.p1.getWorld().dropItemNaturally(holder.p1.getLocation(), item);
                    holder.p1.sendMessage(ChatColor.YELLOW + "You have some leftovers that didn't fit in your inventory! We'll place those next to you.");
                }
            }

            if (getTrades(holder.p2).size() > 0) {
                Collection<ItemStack> leftovers = holder.p2.getInventory().addItem(getTrades(holder.p2).toArray(new ItemStack[0])).values();
                if (leftovers.size() > 0) {
                    for (ItemStack item : leftovers) holder.p2.getWorld().dropItemNaturally(holder.p2.getLocation(), item);
                    holder.p2.sendMessage(ChatColor.YELLOW + "You have some leftovers that didn't fit in your inventory! We'll place those next to you.");
                }
            }

            if (holder.p1.getUniqueId().equals(clicker.getUniqueId())) {
                holder.p2.sendMessage(ChatColor.RED + holder.p1.getName() + " has cancelled the trade!");
                holder.p1.sendMessage(ChatColor.RED + "You have cancelled the trade!");
            } else if (holder.p2.getUniqueId().equals(clicker.getUniqueId())) {
                holder.p1.sendMessage(ChatColor.RED + holder.p2.getName() + " has cancelled the trade!");
                holder.p2.sendMessage(ChatColor.RED + "You have cancelled the trade!");
            } else throw NO_PLAYER;
        }
    }

    public static Inventory getTradeInventory(Player p1, Player p2, TradeHolder holder) {
        PlayerConfig c1 = new PlayerConfig(p1);
        PlayerConfig c2 = new PlayerConfig(p2);

        Inventory inv = Generator.genGUI(54, "Trades between " + c1.getDisplayName() + ChatColor.RESET + " and " + c2.getDisplayName(), holder);
        
        for (int i = 13; i < 40; i += 9) inv.setItem(i, Items.noName(Material.STICK));

        ItemStack head1 = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta m1 = (SkullMeta) head1.getItemMeta();
        m1.setOwningPlayer(p1);
        m1.setDisplayName(c1.getDisplayName());
        head1.setItemMeta(m1);

        ItemStack head2 = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta m2 = (SkullMeta) head1.getItemMeta();
        m2.setOwningPlayer(p1);
        m2.setDisplayName(c2.getDisplayName());
        head2.setItemMeta(m2);

        inv.setItem(2, head1);
        inv.setItem(6, head2);

        ItemStack status = Items.itemBuilder(Material.BLUE_CONCRETE).setName(ChatColor.DARK_AQUA + "Pending").setLocalizedName("pending").build();

        inv.setItem(3, status);
        inv.setItem(7, status);

        ItemStack cancelButton = Items.itemBuilder(Material.RED_CONCRETE).setName(ChatColor.RED + "Cancel Trade").setLocalizedName("cancel").build();
        ItemStack acceptButton = Items.itemBuilder(Material.GREEN_CONCRETE).setName(ChatColor.GREEN + "Accept Trade").setLocalizedName("accept").build();

        inv.setItem(48, cancelButton);
        inv.setItem(50, acceptButton);

        return inv;
    }

    // Handling Methods
    
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (!(TradeInstance.isInTrade(p))) return;
        if (e.getCurrentItem() == null) return;
        if (!(TradeInstance.isInTrade(p))) return;
        ItemStack clickedItem = e.getCurrentItem();
        String id = Items.getLocalization(clickedItem);

        TradeInstance trade = TradeInstance.from(p);
        if (trade.hasEnded()) return;

        if (e.getClickedInventory() instanceof PlayerInventory) {
            trade.addTrade(p, clickedItem);
        } else {
            if (id.equalsIgnoreCase("no_name")) return;
            if (Arrays.asList(Items.NON_COLLECTIBLES).contains(clickedItem)) return;

            if (id.equalsIgnoreCase("cancel")) {
                trade.cancelTrade(p);
            } else if (id.equalsIgnoreCase("accept")) {
                trade.markAccepted(p);  
            } else {
                trade.removeTrade(p, clickedItem);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player p)) return;
        if (!(TradeInstance.isInTrade(p))) return;

        TradeInstance trade = TradeInstance.from(p);
        trade.cancelTrade(p);
    }

    // Prevent Drops

    
    @EventHandler
    public void onDrop(EntityDropItemEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;

        e.getItemDrop().getPersistentDataContainer().set(new NamespacedKey(plugin, "owner"), PersistentDataType.STRING, p.getUniqueId().toString());
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;

        try {
            if (!(e.getItem().getPersistentDataContainer().get(new NamespacedKey(plugin, "owner"), PersistentDataType.STRING)).equals(p.getUniqueId().toString())) e.setCancelled(true);
        } catch (NullPointerException err) {
            // do nothing
        }
    }

}

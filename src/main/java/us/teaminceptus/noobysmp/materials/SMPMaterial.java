package us.teaminceptus.noobysmp.materials;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableMap;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.SMPMaterial.CleanOutput.MaterialOutput;
import us.teaminceptus.noobysmp.util.BlockPDC;
import us.teaminceptus.noobysmp.util.Items;
import us.teaminceptus.noobysmp.util.Queryable;
import us.teaminceptus.noobysmp.util.SMPColor;

/**
 * Class used for Custom Materials inside the SMP.
 * Utility Items (i.e. tables, items with inventory) are not included.
 * Items with Abilities are used in {@link AbilityItem}
 */
public enum SMPMaterial implements Queryable {
	
	RUBY(0, Material.DIAMOND, "Ruby"),
	RUBY_SWORD(0, Material.DIAMOND_SWORD, "Ruby Sword", genAttack(6.5, 0.5, 0.7)),
	RUBY_AXE(0, Material.DIAMOND_AXE, "Ruby Axe", genAttack(8, 0.6, 0.6)),
	RUBY_SHOVEL(0, Material.DIAMOND_SHOVEL, "Ruby Shovel"),
	RUBY_HOE(0, Material.DIAMOND_HOE, "Ruby Hoe"),
	RUBY_PICKAXE(0, Material.DIAMOND_PICKAXE, "Ruby Pickaxe"),
	RUBY_HELMET(0, Material.DIAMOND_HELMET, "Ruby Helmet", genArmor(2.5, 1.5, 1)),
	RUBY_CHESTPLATE(0, Material.DIAMOND_CHESTPLATE, "Ruby Chestplate", genArmor(7, 1.5, 1)),
	RUBY_LEGGINGS(0, Material.DIAMOND_LEGGINGS, "Ruby Leggings", genArmor(5, 1.5, 1)),
	RUBY_BOOTS(0, Material.DIAMOND_BOOTS, "Ruby Boots", genArmor(2, 1.5, 1)),
	RUBY_ORE(0, Material.DIAMOND_ORE, "Ruby Ore"),
	DEEPSLATE_RUBY_ORE(0, Material.DEEPSLATE_DIAMOND_ORE, "Deepslate Ruby Ore"),
	RUBY_BLOCK(0, Material.DIAMOND_BLOCK, "Ruby Block"),

	COPPER_PICKAXE(0, Material.GOLDEN_PICKAXE, "Copper Pickaxe", genTool(3, 0)),
	COPPER_SWORD(0, Material.GOLDEN_SWORD, "Copper Sword", genAttack(5, 0, 0.5), genTool(3, 0)),
	COPPER_AXE(0, Material.GOLDEN_AXE, "Copper Axe", genAttack(7, 0, 0.4), genTool(3, 0)),
	COPPER_SHOVEL(0, Material.GOLDEN_SHOVEL, "Copper Shovel", genTool(3, 0)),
	COPPER_HOE(0, Material.GOLDEN_HOE, "Copper Hoe", genTool(3, 0)),

	EMERALD_SWORD(0, Material.DIAMOND_SWORD, "Emerald Sword", genAttack(7, 0.2, 0.5)),
	EMERALD_BOW(0, Material.BOW, "Emerald Bow", genBow(4, 1, false, false)),
	EMERALD_TRIDENT(0, Material.TRIDENT, "Emerald Trident", genAttack(8, 0.3, 0.5)),

	BLACKSTONE_HELMET(0, Material.LEATHER_HELMET, "Blackstone Helmet", genArmor(3.5, 0.5, 0), genTool(3, 0), rgb("121212")),
	BLACKSTONE_CHESTPLATE(0, Material.LEATHER_CHESTPLATE, "Blackstone Chestplate", genArmor(6, 0.5, 0), genTool(3, 0), rgb("121212")),
	BLACKSTONE_LEGGINGS(0, Material.LEATHER_LEGGINGS, "Blackstone Leggings", genArmor(4.5, 0.5, 0), genTool(3, 0), rgb("121212")),
	BLACKSTONE_BOOTS(0, Material.LEATHER_BOOTS, "Blackstone Boots", genArmor(3, 0.5, 0), genTool(3, 0), rgb("121212")),
	
	QUARTZ_HELMET(0, Material.IRON_HELMET, "Quartz Helmet", genArmor(4.5, 0.5, 0.5), genTool(1, 0)),
	QUARTZ_CHESTPLATE(0, Material.IRON_CHESTPLATE, "Quartz Boots", genArmor(4.5, 0.5, 0.5), genTool(1, 0)),
	QUARTZ_LEGGINGS(0, Material.IRON_LEGGINGS, "Quartz Boots", genArmor(4.5, 0.5, 0.5), genTool(1, 0)),
	QUARTZ_BOOTS(0, Material.IRON_BOOTS, "Quartz Boots", genArmor(4.5, 0.5, 0.5), genTool(1, 0)),
	
	TINY_EXPERIENCE_BAG(0, "Tiny Experience Bag", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmMwODNmNjk3NDkzZmViNDZiMjQ0NDQ2OWU5OGU1MzE1YzQ3ZGI0YjM0YTdjZGJjNjJhNjUwNjI1Mzk3YWI1ZSJ9fX0="),
	// Level 1
	ENDERITE_ORE(1, Material.IRON_BLOCK, "Enderite Ore"),
	ENDER_FRAGMENT(1, Material.IRON_NUGGET, "Ender Fragment"),
	ENDERITE(1, Material.IRON_INGOT, "Enderite"),
	ENDERITE_SWORD(1, Material.NETHERITE_SWORD, "Enderite Sword", genAttack(10, 0.2, 0.5)),
	ENDERITE_AXE(1, Material.NETHERITE_AXE, "Enderite Axe", genAttack(11, 0.3, 0.35), genTool(4, 1)),
	ENDERITE_PICKAXE(1, Material.NETHERITE_PICKAXE, "Enderite Pickaxe", genTool(4, 1)),
	ENDERITE_SHOVEL(1, Material.NETHERITE_SHOVEL, "Enderite Shovel", genTool(4, 1)),
	ENDERITE_HOE(1, Material.NETHERITE_HOE, "Enderite Hoe", genTool(4, 1)),
	ENDERITE_BOW(1, Material.BOW, "Enderite Bow", genBow(7, 2, true, false)),
	ENDERITE_TRIDENT(1, Material.TRIDENT, "Enderite Trident", genAttack(11, 0.25, 0.4)),
	ENDERITE_HELMET(1, Material.NETHERITE_HELMET, "Enderite Helmet", genArmor(4, 2, 2)),
	ENDERITE_CHESTPLATE(1, Material.NETHERITE_CHESTPLATE, "Enderite Chestplate", genArmor(8, 2, 2)),
	ENDERITE_LEGGINGS(1, Material.NETHERITE_LEGGINGS, "Enderite Leggings", genArmor(7, 2, 2)),
	ENDERITE_BOOTS(1, Material.NETHERITE_BOOTS, "Enderite Boots", genArmor(4, 2, 2)),
	ENDERITE_ELYTRA(1, Material.ELYTRA, "Enderite Elytra", genArmor(6, 2, 2), genTool(2, 0)),
	
	RAW_ENCHANT(1, Material.LIGHT_GRAY_DYE, "Raw Enchant", glint()),
	// Level 2 - 4
	ENCHANTED_GOLD(2, Material.GOLD_INGOT, "Enchanted Gold", glint()),
	ENCHANTED_GOLD_BLOCK(2, Material.GOLD_BLOCK, "Enchanted Gold Block", glint()),
	GOLDEN_ENCHANT(2, Material.YELLOW_DYE, "Golden Enchant", glint()),
	
	ENCHANTED_DIAMOND(3, Material.DIAMOND, "Enchanted Diamond", glint()),
	ENCHANTED_DIAMOND_BLOCK(3, Material.DIAMOND_BLOCK, "Enchanted Diamond Block", glint()),
	DIAMOND_ENCHANT(3, Material.LIGHT_BLUE_DYE, "Diamond Enchant", glint()),
	
	NETHERITE_STAR(4, Material.NETHER_STAR, "Netherite Star"),
	NETHERITE_BEACON(4, Material.BEACON, "Netherite Beacon"),
	
	AMETHYST_HELMET(4, Material.LEATHER_HELMET, "Amethyst Helmet", genArmor(6, 1, 0.5), genTool(4, 0), rgb("6909a0")),
	AMETHYST_CHESTPLATE(4, Material.LEATHER_CHESTPLATE, "Amethyst Chestplate", genArmor(9, 1, 0.5), genTool(4, 0), rgb("6909a0")),
	AMETHYST_LEGGINGS(4, Material.LEATHER_LEGGINGS, "Amethyst Leggings", genArmor(8, 1, 0.5), genTool(4, 0), rgb("6909a0")),
	AMETHYST_BOOTS(4, Material.LEATHER_BOOTS, "Amethyst Boots", genArmor(5, 1, 0.5), genTool(4, 0), rgb("6909a0")),
	
	// Level 5 - 15

	SMALL_EXPERIENCE_BAG(5, "Small Experience Bag", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQxMGFkODhjOTZjZmFmM2Q3Y2I4ZmViODlkZTQ2ZTIxMzg4MmEyZjFlYjRiMjIyODg0YzZlNWRkMjY0ZjE3NyJ9fX0="),
	UNCHARGED_ESSENCE(5, Material.GRAY_DYE, "Uncharged Essence"),
	CHARGED_ESSENCE(5, Material.WHITE_DYE, "Charged Essence"),
	
	SUPER_NETHERITE_HELMET(6, Material.NETHERITE_HELMET, "Super Netherite Helmet", genArmor(11, 3, 3), genTool(7, 0)),
	SUPER_NETHERITE_CHESTPLATE(6, Material.NETHERITE_CHESTPLATE, "Super Netherite Chestplate", genArmor(14, 3, 3), genTool(7, 0)),
	SUPER_NETHERITE_LEGGINGS(6, Material.NETHERITE_LEGGINGS, "Super Netherite Leggings", genArmor(13, 3, 3), genTool(7, 0)),
	SUPER_NETHERITE_BOOTS(6, Material.NETHERITE_BOOTS, "Super Netherite Boots", genArmor(10, 3, 3), genTool(7, 0)),
	SUPER_NETHERITE_SWORD(6, Material.NETHERITE_SWORD, "Super Netherite Sword", genAttack(12, 0.4, 0.5), genTool(7, 0)),
	SUPER_NETHERITE_TRIDENT(6, Material.TRIDENT, "Super Netherite Trident", genAttack(12, 0.5, 0.5), genTool(7, 0)),
	
	ENCHANTED_AMETHYST_SHARD(7, Material.AMETHYST_SHARD, "Enchanted Amethyst Shard"),
	ENCHANTED_AMETHYST_BLOCK(7, Material.AMETHYST_BLOCK, "Enchanted Amethyst Block"),
	AMETHYST_ENCHANT(7, Material.PURPLE_DYE, "Amethyst Enchant", glint()),
	
	EMERALD_INGOT(8, Material.EMERALD, "Emerald Ingot", glint()),
	EMERALD_NUGGET(8, Material.GOLD_NUGGET, "Emerald Nugget", glint()),
	COMPRESSED_EMERALD_BLOCK(8, Material.EMERALD_BLOCK, "Compressed Emerald Block", glint()),
	
	EMERALD_HELMET(9, Material.LEATHER_HELMET, "Emerald Helmet", genArmor(20, 5, 4), genTool(10, 0), Color.LIME),
	EMERALD_CHESTPLATE(9, Material.LEATHER_CHESTPLATE, "Emerald Chestplate", genArmor(32, 5, 4), genTool(10, 0), Color.LIME),
	EMERALD_LEGGINGS(9, Material.LEATHER_LEGGINGS, "Emerald Leggings", genArmor(28, 5, 4), genTool(10, 0), Color.LIME),
	EMERALD_BOOTS(9, Material.LEATHER_BOOTS, "Emerald Boots", genArmor(16, 5, 4), genTool(10, 0), Color.LIME),
	
	EMERALD_CROSSBOW(10, Material.CROSSBOW, "Emerald Crossbow", genCrossbow(10, 3, true)),
	
	RAW_SCROLL(11, Material.PAPER, "Raw Scroll", glint()),

	REDSTONE_HELMET(12, Material.LEATHER_HELMET, "Redstone Helmet", genArmor(25, 6, 4), genTool(12, 0), Color.RED),
	REDSTONE_CHESTPLATE(12, Material.LEATHER_CHESTPLATE, "Redstone Chestplate", genArmor(40, 6, 4), genTool(12, 0), Color.RED),
	REDSTONE_LEGGINGS(12, Material.LEATHER_LEGGINGS, "Redstone Leggings", genArmor(35, 6, 4), genTool(12, 0), Color.RED),
	REDSTONE_BOOTS(12, Material.LEATHER_BOOTS, "Redstone Boots", genArmor(20, 6, 4), genTool(12, 0), Color.RED),
	REDSTONE_AXE(12, Material.DIAMOND_AXE, "Redstone Axe", genAttack(15, 0.6, 0.6), genTool(12, 5)),
	REDSTONE_PICKAXE(12, Material.DIAMOND_PICKAXE, "Redstone Pickaxe", genTool(12, 5)),
	REDSTONE_SHOVEL(12, Material.DIAMOND_SHOVEL, "Redstone Shovel", genTool(12, 5)),
	REDSTONE_HOE(12, Material.DIAMOND_HOE, "Redstone Hoe", genTool(12, 5)),
	REDSTONE_BOW(12, Material.BOW, "Redstone Bow", genBow(15, 3, true, true)),
	
	STAR_HELMET(14, Material.LEATHER_HELMET, "Star Helmet", genArmor(27, 6.5, 4), genTool(13, 0), Color.WHITE),
	STAR_CHESTPLATE(14, Material.LEATHER_CHESTPLATE, "Star Chestplate", genArmor(42, 6.5, 4), genTool(13, 0), Color.WHITE),
	STAR_LEGGINGS(14, Material.LEATHER_LEGGINGS, "Star Leggings", genArmor(37, 6.5, 4), genTool(13, 0), Color.WHITE),
	STAR_BOOTS(14, Material.LEATHER_BOOTS, "Star Boots", genArmor(22, 6.5, 4), genTool(13, 0), Color.WHITE),
	// Level 15 - 30
	STAR_SCYTHE(15, Material.IRON_HOE, "Star Scythe", genAttack(25, 0.8, 0.65), genTool(14, 6)),
	STAR_PICKAXE(15, Material.IRON_PICKAXE, "Star Pickaxe", genTool(14, 6)),
	STAR_AXE(15, Material.IRON_AXE, "Star Axe", genAttack(25, 0.9, 0.5), genTool(14, 6)),
	STAR_SHOVEL(15, Material.IRON_SHOVEL, "Star Shovel", genTool(14, 6)),
	STAR_TRIDENT(15, Material.TRIDENT, "Star Trident", genAttack(25, 0.9, 0.65), genTool(14, 6)),
	STAR_BEACON(15, Material.BEACON, "Star Beacon"),
	
	MEDIUM_EXPERIENCE_BAG(16, "Medium Experience Bag", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjkzYmUxODE1YjZjMmExODQ2NDRiYTgzYmQ1ODdjNzc4YTUwMDU3Y2ZjODdkNjdkZTIyZjY2NDQxNTY4ZTA4YyJ9fX0="),

	ENCHANTED_OBSIDIAN(17, Material.OBSIDIAN, "Enchanted Obsidian", glint()),
	OBSIDIAN_AXE(17, Material.NETHERITE_AXE, "Obsidian Axe", genAttack(29, 0.8, 0.7), genTool(18, 8)),
	OBSIDIAN_PICKAXE(17, Material.NETHERITE_PICKAXE, "Obsidian Pickaxe", genTool(18, 8)),
	OBSIDIAN_SWORD(17, Material.NETHERITE_SWORD, "Obsidian Sword", genTool(18, 8)),
	OBSIDIAN_HOE(17, Material.NETHERITE_HOE, "Obsidian Hoe", genTool(18, 8)),
	OBSIDIAN_SHOVEL(17, Material.NETHERITE_SHOVEL, "Obsidian Shovel", genTool(18, 8)),
	OBSIDIAN_BOW(17, Material.BOW, "Obsidian Bow", genBow(29, 5, true, true)),
	
	GRAPHENE(20, Material.GRAY_DYE, "Graphene", glint()),
	GRAPHENE_DUST(20, Material.GUNPOWDER, "Graphene Dust", glint()),
	GRAPHENE_HELMET(20, Material.LEATHER_HELMET, "Graphene Helmet", genArmor(30, 8, 6), genTool(22, 0), Color.GRAY),
	GRAPHENE_CHESTPLATE(20, Material.LEATHER_CHESTPLATE, "Graphene Chestplate", genArmor(48, 8, 6), genTool(22, 0), Color.GRAY),
	GRAPHENE_LEGGINGS(20, Material.LEATHER_LEGGINGS, "Graphene Leggings", genArmor(48, 8, 6), genTool(22, 0), Color.GRAY),
	GRAPHENE_BOOTS(20, Material.LEATHER_BOOTS, "Graphene Boots", genArmor(48, 8, 6), genTool(22, 0), Color.GRAY),
	GRAPHENE_SWORD(20, Material.STONE_SWORD, "Graphene Sword", genAttack(40, 1, 0.85), genTool(35, 0)),
	GRAPHENE_AXE(20, Material.STONE_AXE, "Graphene Axe", genAttack(40, 1.1, 0.7), genTool(35, 11)),
	GRAPHENE_PICKAXE(20, Material.STONE_PICKAXE, "Graphene Pickaxe", genTool(35, 11)),
	GRAPHENE_SHOVEL(20, Material.STONE_SHOVEL, "Graphene Shovel", genTool(35, 11)),
	GRAPHENE_HOE(20, Material.STONE_HOE, "Graphene Hoe", genTool(35, 11)),
	GRAPHENE_TRIDENT(20, Material.TRIDENT, "Graphene Trident", genAttack(40, 1.1, 0.85), genTool(35, 11)),

	BEDROCK_INGOT(22, Material.NETHERITE_INGOT, "Bedrock Ingot", glint()),
	COMPRESSED_BEDROCK(22, Material.BEDROCK, "Compressed Bedrock", glint()),
	
	BEDROCK_HELMET(22, Material.LEATHER_HELMET, "Bedrock Helmet", genArmor(60, 10, 7), genTool(45, 0), SMPColor.DARK_GRAY),
	BEDROCK_CHESTPLATE(22, Material.LEATHER_CHESTPLATE, "Bedrock Chestplate", genArmor(96, 10, 7), genTool(45, 0), SMPColor.DARK_GRAY),
	BEDROCK_LEGGINGS(22, Material.LEATHER_LEGGINGS, "Bedrock Leggings", genArmor(84, 10, 7), genTool(45, 0), SMPColor.DARK_GRAY),
	BEDROCK_BOOTS(22, Material.LEATHER_BOOTS, "Bedrock Boots", genArmor(48, 10, 7), genTool(45, 0), SMPColor.DARK_GRAY),
	
	LARGE_EXPERIENCE_BAG(23, "Large Experience Bag", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWY1YzFhNjc4YmZhMTViOTBiNmE4YjgzZGEzMzlmZmZmNTY3YWMyYWI0MTljMjhmMDQyMjc1OWIxY2Q1NDIwOCJ9fX0="),
	
	// Natural Items beyond Level 25 are collected from the Titan Dimension and its counterparts
	TITAN_STONE(25, Material.STONE, "Titan Stone", glint()),
	TITAN_DEEPSLATE(25, Material.DEEPSLATE, "Titan Deepslate", glint()),

	GREEN_MATTER(25, "Green Matter", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjg0MzgwYWQ0ZGMzODhmNTYxMjU0Yzg0MDlmYTcwNGQ0NDc0N2ViNmU1ZmVhY2JhMzQzZTdjMjQzY2ZhYzZhMSJ9fX0="),
	YELLOW_MATTER(25, "Yellow Matter", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE0NjljZTY3ZGZjMjg0MzNhZWE4YTQ1YWI4MTZlNTFiZDM5YmE5ZWI4MTVkMjI1NzllNzc2OThkYTBiZjI5NSJ9fX0="),
	BLUE_MATTER(25, "Blue Matter", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU1MmY3OTYwZmYzY2VjMmY1MTlhNjM1MzY0OGM2ZTMzYmM1MWUxMzFjYzgwOTE3Y2YxMzA4MWRlY2JmZjI0ZCJ9fX0="),
	BROWN_MATTER(25, "Brown Matter", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDliNWViM2RiNzJkMjdlZjc4YTg3ZjhkMjZiMjJkNGQxYWY4Yzg3MGJiYzFkZGViN2NkNGQxNDFiZDkxMGIyIn19fQ=="),
	BLACK_MATTER(25, "Black Matter", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgzNzFkMDFlYzE2ZmI2ODY3MGViNzIwNjE0OTcwYTE5MWNjOTZmOGEwMjZhMDFkODEzYzUzMTZjOWYzMmIwNyJ9fX0="),
	GRAY_MATTER(25, "Gray Matter", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2ZmU0MTdmMTU2NGE4ZWYyYWY4YmMyMDAwMmZmMDFiMTkxOTU0MTk0YWZjYzJhNjgwYTlkYTQ0YjE1Yzc2NyJ9fX0="),
	GOLDEN_MATTER(25, "Golden Matter", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODJjZGUwNjhlOTlhNGY5OGMzMWY4N2I0Y2MwNmJlMTRiMjI5YWNhNGY3MjgxYTQxNmM3ZTJmNTUzMjIzZGI3NCJ9fX0="),
	
	HARDENED_OAK_LOG(25, Material.OAK_LOG, "Hardened Oak Log", glint()),
	HARDENED_SPRUCE_LOG(25, Material.SPRUCE_LOG, "Hardened Spruce Log", glint()),
	HARDENED_BIRCH_LOG(25, Material.BIRCH_LOG, "Hardened Birch Log", glint()),
	HARDENED_DARK_OAK_LOG(25, Material.DARK_OAK_LOG, "Hardened Dark Oak Log", glint()),
	HARDENED_JUNGLE_LOG(25, Material.JUNGLE_LOG, "Hardened Jungle Log", glint()),
	HARDENED_ACACIA_LOG(25, Material.ACACIA_LOG, "Hardened Acacia Log", glint()),
	
	BEDROCK_ORE(25, Material.COAL_ORE, "Bedrock Ore", glint()),
	DEEPSLATE_BEDROCK_ORE(25, Material.DEEPSLATE_COAL_ORE, "Deepslate Bedrock Ore", glint()),
	
	AMBER_ORE(26, Material.COPPER_ORE, "Amber Ore", glint()),
	DEEPSLATE_AMBER_ORE(26, Material.DEEPSLATE_COPPER_ORE, "Deepslate Amber Ore", glint()),
	AMBER(26, Material.RAW_COPPER, "Amber", glint()),
	AMBER_BLOCK(26, Material.RAW_COPPER_BLOCK, "Block of Amber", glint()),
	CUT_AMBER(26, Material.COPPER_INGOT, "Cut Amber", glint()),
	CUT_AMBER_BLOCK(26, Material.COPPER_BLOCK, "Block of Cut Amber", glint()),
	AMBER_HELMET(26, Material.LEATHER_HELMET, "Amber Helmet", genArmor(52, 11, 7), genTool(48, 0), rgb("ffd700")),
	AMBER_CHESTPLATE(26, Material.LEATHER_CHESTPLATE, "Amber Chestplate", genArmor(82, 11, 7), genTool(48, 0), rgb("ffd700")),
	AMBER_LEGGINGS(26, Material.LEATHER_LEGGINGS, "Amber Leggings", genArmor(72, 11, 7), genTool(48, 0), rgb("ffd700")),
	AMBER_BOOTS(26, Material.LEATHER_CHESTPLATE, "Amber Boots", genArmor(42, 11, 7), genTool(48, 0), rgb("ffd700")),
	AMBER_BOW(26, Material.BOW, "Amber Bow", genBow(40, 11, true, true), genTool(48, 0)),
	
	APATITE_ORE(28, Material.IRON_ORE, "Apatite Ore", glint()),
	DEEPSLATE_APATITE_ORE(28, Material.DEEPSLATE_IRON_ORE, "Deepslate Apatite Ore", glint()),
	APATITE_CRYSTAL(28, Material.QUARTZ, "Apatite Crystal", glint()),
	APATITE_CRYSTAL_BLOCK(28, Material.QUARTZ_BLOCK, "Apatite Crystal Block", glint()),
	APATITE(28, Material.IRON_INGOT, "Apatite", glint()),
	APATITE_BLOCK(28, Material.IRON_BLOCK, "Apatite Block", glint()),
	APATITE_SWORD(28, Material.IRON_SWORD, "Apatite Sword", genAttack(50, 1.2, 0.9), genTool(53, 0)),
	APATITE_AXE(28, Material.IRON_AXE, "Apatite Axe", genAttack(50, 1.3, 0.85), genTool(53, 14)),
	APATITE_PICKAXE(28, Material.IRON_PICKAXE, "Apatite Pickaxe", genTool(53, 14)),
	APATITE_SHOVEL(28, Material.IRON_SHOVEL, "Apatite Shovel", genTool(53, 14)),
	APATITE_HOE(28, Material.IRON_HOE, "Apatite Hoe", genTool(53, 14)),
	
	HUGE_EXPERIENCE_BAG(29, "Huge Experience Bag", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzFmZTdhYzJlNWEzNzYyMDE0YzY4NDA3ZTdiZGM3OGY0N2I1ZjM5OTU5ZWM3MzkwNzA0ZTQ2ZGI4MGRjYzNhNCJ9fX0="),
	
	// Level 30+
	APATITE_AMBER_SCYTHE(30, Material.GOLDEN_HOE, "Apatite-Amber Scythe", genAttack(55, 1.3, 0.9), genTool(58, 17)),

	JADE(33, Material.LAPIS_LAZULI, "Jade", glint()),
	JADE_ORE(33, Material.LAPIS_ORE, "Jade Ore", glint()),
	DEEPSLATE_JADE_ORE(33, Material.DEEPSLATE_LAPIS_ORE, "Deepslate Jade Ore", glint()),
	JADE_BLOCK(33, Material.LAPIS_BLOCK, "Block of Jade", glint()),
	JADE_HELMET(33, Material.LEATHER_HELMET, "Jade Helmet", genArmor(60, 13, 8), genTool(60, 0), Color.AQUA),
	JADE_CHESTPLATE(33, Material.LEATHER_CHESTPLATE, "Jade Chestplate", genArmor(96, 13, 8), genTool(60, 0)),
	JADE_LEGGINGS(33, Material.LEATHER_LEGGINGS, "Jade Leggings", genArmor(84, 13, 8), genTool(60, 0), Color.AQUA),
	JADE_BOOTS(33, Material.LEATHER_BOOTS, "Jade Boots", genArmor(48, 13, 8), genTool(60, 0), Color.AQUA),

	TOPAZ(35, Material.RAW_GOLD, "Topaz", glint()),
	TOPAZ_ORE(35, Material.GOLD_ORE, "Topaz Ore", glint()),
	DEEPSLATE_TOPAZ_ORE(35, Material.DEEPSLATE_GOLD_ORE, "Deepslate Topaz Ore"),
	TOPAZ_BLOCK(35, Material.RAW_GOLD_BLOCK, "Block of Topaz", glint()),
	CUT_TOPAZ(35, Material.GOLD_INGOT, "Cut Topaz", glint()),
	CUT_TOPAZ_BLOCK(35, Material.GOLD_BLOCK, "Block of Cut Topaz", glint()),
	TOPAZ_HELMET(35, Material.GOLDEN_HELMET, "Topaz Helmet", genArmor(75, 15, 9), genTool(70, 0)),
	TOPAZ_CHESTPLATE(35, Material.GOLDEN_CHESTPLATE, "Topaz Chestplate", genArmor(120, 15, 9), genTool(70, 0)),
	TOPAZ_LEGGINGS(35, Material.GOLDEN_LEGGINGS, "Topaz Leggings", genArmor(105, 15, 9), genTool(70, 0)),
	TOPAZ_BOOTS(35, Material.GOLDEN_BOOTS, "Topaz Boots", genArmor(60, 15, 9), genTool(70, 0)),
	TOPAZ_CROSSBOW(35, Material.CROSSBOW, "Topaz Crossbow", genCrossbow(20, 4, true)),
	TOPAZ_BEACON(35, Material.BEACON, "Topaz Beacon"),

	TOPAZ_JADE(36, Material.DIAMOND, "Topaz-Jade", glint()),
	TOPAZ_JADE_SWORD(36, Material.DIAMOND_SWORD, "Topaz-Jade Sword", genAttack(67, 1.6, 1), genTool(82, 0)),
	TOPAZ_JADE_AXE(36, Material.DIAMOND_AXE, "Topaz-Jade Axe", genAttack(67, 1.7, 0.9), genTool(82, 19)),
	TOPAZ_JADE_PICKAXE(36, Material.DIAMOND_PICKAXE, "Topaz-Jade Pickaxe", genTool(82, 19)),
	TOPAZ_JADE_SHOVEL(36, Material.DIAMOND_SHOVEL, "Topaz-Jade Shovel", genTool(82, 19)),
	TOPAZ_JADE_HOE(36, Material.DIAMOND_HOE, "Topaz-Jade Hoe", genTool(82, 19)),
	TOPAZ_JADE_BOW(36, Material.BOW, "Topaz-Jade Bow", genBow(67, 13, true, true), genTool(82, 19)),
	
	SAPPHIRE(40, Material.DIAMOND, "Sapphire", glint()),
	SAPPHIRE_ORE(40, Material.DIAMOND_ORE, "Sapphire Ore", glint()),
	DEEPSLATE_SAPPHIRE_ORE(40, Material.DEEPSLATE_DIAMOND_ORE, "Deepslate Sapphire Ore", glint()),
	SAPPHIRE_HELMET(40, Material.DIAMOND_HELMET, "Sapphire Helmet", genArmor(92, 17, 11), genTool(87, 0)),
	SAPPHIRE_CHESTPLATE(40, Material.DIAMOND_CHESTPLATE, "Sapphire Chestplate", genArmor(146, 17, 11), genTool(87, 0)),
	SAPPHIRE_LEGGINGS(40, Material.DIAMOND_LEGGINGS, "Sapphire Leggings", genArmor(128, 17, 11), genTool(87, 0)),
	SAPPHIRE_BOOTS(40, Material.DIAMOND_BOOTS, "Sapphire Boots", genArmor(74, 17, 11), genTool(87, 0)),
	SAPPHIRE_TRIDENT(40, Material.TRIDENT, "Sapphire Trident", genAttack(75, 1.8, 1), genTool(87, 0)),

	QARDITE(44, Material.EMERALD, "Qardite", glint()),
	QARDITE_ORE(44, Material.EMERALD_ORE, "Qardite Ore", glint()),
	DEEPSLATE_QARDITE_ORE(44, Material.DEEPSLATE_EMERALD_ORE, "Deepslate Qardite Ore", glint()),
	QARDITE_BLOCK(44, Material.EMERALD_BLOCK, "Block of Qardite", glint()),
	QARDITE_BOW(44, Material.BOW, "Qardite Bow", genBow(91, 23, true, true), unbreak()),
	
	// Mob & Boss Drops & Craftables from them
	// Drops will ALWAYS be 0

	AQUATIC_CORE(0, "Aquatic Core", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTZkMWMxOWIwZGFiZGY3MzYwZmJkMThkZjlkZmQxYzYxNWRhMmY4ZDJjODRmYzQyMTY1NjVkOWM1ZGQifX19"),
	NETHER_CORE(0, "Nether Core", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQyNTMyNzVlMWI3NzU3MTMwYTJkNjM3ZDA3ZjUyMmZjYzY2ODZkYzgwNDM0NmYwZTcxNjY4ZDVjZTZkNTg5MSJ9fX0="),
	END_CORE(0, "End Core", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODEyNTFkODE0YmRlYThmYzQ4ODFhZWI2ZTdkMWY0OGEyZWRmOWNkYmE5YWI0ZTNmYTdmNzM1OWM1YjkyIn19fQ=="),
	LIFE_CORE(0, "Life Core", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTVjYmYzOTVlOTNlYmZlZTk0YTllZDYzNDVkMTE3ZDQ2YTYzYzczNTBmNDhlNjZiNzU0ZTMyMzFlN2YzZTMifX19"),
	
	ZOMBIE_SWORD(0, Material.WOODEN_SWORD, "Zombie Sword", genAttack(6, 0, 0.5)),
	END_BOW(0, Material.BOW, "End Bow", genBow(4, 1, true, false), genTool(1, 0)),
	
	THIEF_HELMET(0, Material.IRON_HELMET, "Thief's Helmet", genArmor(6, 1, 1)),

	ENCHANTED_PEARL(0, Material.ENDER_PEARL, "Enchanted Pearl", glint()),
	ENCHANTED_ENDER_EYE(0, Material.ENDER_EYE, "Enchanted Eye of Ender", glint()),

	AQUATIC_CROWN(0, Material.LEATHER_HELMET, "Aquatic Crown", genArmor(25, 20, 3), genTool(15, 0), Color.AQUA),

	REDSTONE_CROSSBOW(0, Material.CROSSBOW, "Redstone Crossbow", genCrossbow(55, 4, true), genTool(30, 0)),
	POWERED_CHESTPLATE(0, Material.LEATHER_CHESTPLATE, "Powered Chestplate", genArmor(10, 7, 0.5), genTool(7, 0)),
	
	DAMAGED_WITHERING_BOOTS(0, Material.LEATHER_BOOTS, "Damaged Withering Boots", genArmor(25, 12, 2), genTool(16, 0), SMPColor.DARK_GRAY),
	
	BLAZESABER(0, Material.GOLDEN_SWORD, "BlazeSaber", genAttack(40, 7, 1), genTool(60, 0)),

	MAGIC_DUST(0, Material.BLAZE_POWDER, "Magic Dust", glint()),

	GUARDIAN_TRIDENT(0, Material.TRIDENT, "Guardian Trident", genAttack(21, 9, 1), genTool(16, 0)),
	// Craftables & Other
	AQUATIC_AXE(2, Material.DIAMOND_AXE, "Aquatic Axe", genAttack(7, 0.5, 1), genTool(2, 4)),
	AQUATIC_PICKAXE(2, Material.DIAMOND_PICKAXE, "Aquatic Pickaxe", genTool(2, 4)),
	AQUATIC_SHOVEL(2, Material.DIAMOND_SHOVEL, "Aquatic Shovel", genTool(2, 4)),
	AQUATIC_HOE(2, Material.DIAMOND_HOE, "Aquatic Hoe", genTool(2, 4)),

	END_CROSSBOW(3, Material.CROSSBOW, "End Crossbow", genCrossbow(10, 3, true), genTool(4, 0)),

	PATCHED_WITHERING_BOOTS(6, Material.LEATHER_BOOTS, "Patched Withering Boots", genArmor(35, 21, 4), genTool(27, 0)),

	MAGIC_RUBY_SWORD(9, Material.DIAMOND_SWORD, "Magic Ruby Sword", genAttack(55, 12, 1), genTool(100, 0)),

	ELECTRICIY_ROD(12, Material.LIGHTNING_ROD, "Electricity Rod", glint()),
	SPONGE_DUST(12, Material.YELLOW_DYE, "Sponge Dust", glint()),

	SUPER_SLIMEBALL(14, Material.SLIME_BALL, "Super Slimeball", glint()),

	REPAIRED_WITHERING_BOOTS(17, Material.LEATHER_BOOTS, "Repaired Withering Boots", genArmor(61, 39, 11), genTool(41, 0)),
	CHARGED_NETHER_CORE(17, "Charged Nether Core", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQyNTMyNzVlMWI3NzU3MTMwYTJkNjM3ZDA3ZjUyMmZjYzY2ODZkYzgwNDM0NmYwZTcxNjY4ZDVjZTZkNTg5MSJ9fX0="),
	
	TRINITE_SHARD(18, Material.AMETHYST_SHARD, "Trinite Shard", glint()),

	DRILL(20, Material.NETHERITE_PICKAXE, "Drill", genTool(100, 35)),

	MAGIC_ENDERITE_SWORD(21, Material.NETHERITE_SWORD, "Magic Enderite Sword", genAttack(71, 15, 1), genTool(100, 0)),
	CHARGED_END_CORE(21, "Charged End Core", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODEyNTFkODE0YmRlYThmYzQ4ODFhZWI2ZTdkMWY0OGEyZWRmOWNkYmE5YWI0ZTNmYTdmNzM1OWM1YjkyIn19fQ=="),
	
	FLOTATION_MOLD(23, Material.LIGHT_BLUE_DYE, "Flotation Mold", glint()),
	
	TRINITE_BLOCK(24, Material.IRON_BLOCK, "Trinite Block", glint()),
	
	COMPRESSED_TRINITE_BLOCK(27, Material.IRON_BLOCK, "Compressed Trinite Block", glint()),

	REINFORCED_TRINITE(30, Material.IRON_BLOCK, "Trinite Block", glint()),

	IMPROVED_WITHERING_BOOTS(32, Material.LEATHER_BOOTS, "Improved Withering Boots", genArmor(93, 55, 26), genTool(77, 0)),
	TRINITE_CHESTPLATE(32, Material.IRON_CHESTPLATE, "Trinite Chestplate", genArmor(112, 23, 1), genTool(100, 0)),

	TITAN_CORE(39, "Titan Core", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWMxZmU4MzlhOTA4YTA3MWNjNjNmMjY2ZmQ2MTQyYTEwNjA3M2ZkMjY5ZWRhMTJjMzJjMjM5ZjRkYmQwMzIyZiJ9fX0="),
	
	MAGIC_GRAPHENE_SWORD(43, Material.STONE_SWORD, "Magic Graphene Sword", genAttack(97, 15, 1), genTool(100, 0)),
	MAGIC_GUARDIAN_TRIDENT(43, Material.TRIDENT, "Magic Guardian Trident", genAttack(79, 17, 1), genTool(100, 0)),

	SUPER_WITHERING_BOOTS(50, Material.NETHERITE_BOOTS, "Super Withering Boots", genArmor(105, 89, 46), genTool(100, 0)),

	MAGIC_APATITE_SWORD(63, Material.IRON_ORE, "Magic Apatite Sword", genAttack(119, 15, 1), genTool(100, 0)),

	MAGIC_TOPAZ_JADE_SWORD(79, Material.DIAMOND_SWORD, "Magic Topaz-Jade Sword", genAttack(142, 15, 1), genTool(100, 0)),
	
	MAGIC_TRINITE_CHESTPLATE(101, Material.IRON_CHESTPLATE, "Magic Trinite Chestplate", genArmor(147, 24, 1), genTool(100, 0)),
	;
	
	{
		Queryable.register(this);
	}
	
	private static int idCounter = 1;
	
	public static final Map<SMPMaterial, SMPMaterial> ORE_DROPS = ImmutableMap.<SMPMaterial, SMPMaterial>builder()
			.put(SMPMaterial.RUBY_ORE, SMPMaterial.RUBY)
			.put(SMPMaterial.DEEPSLATE_RUBY_ORE, SMPMaterial.RUBY)
			.put(SMPMaterial.ENDERITE_ORE, SMPMaterial.ENDER_FRAGMENT)
			// Titan
			.put(SMPMaterial.BEDROCK_ORE, SMPMaterial.BEDROCK_INGOT)
			.put(SMPMaterial.DEEPSLATE_BEDROCK_ORE, SMPMaterial.BEDROCK_INGOT)
			.put(SMPMaterial.AMBER_ORE, SMPMaterial.AMBER)
			.put(SMPMaterial.DEEPSLATE_AMBER_ORE, SMPMaterial.AMBER)
			.put(SMPMaterial.APATITE_ORE, SMPMaterial.APATITE)
			.put(SMPMaterial.DEEPSLATE_APATITE_ORE, SMPMaterial.APATITE)
			.put(SMPMaterial.JADE_ORE, SMPMaterial.JADE)
			.put(SMPMaterial.DEEPSLATE_JADE_ORE, SMPMaterial.JADE)
			.put(SMPMaterial.TOPAZ_ORE, SMPMaterial.TOPAZ)
			.put(SMPMaterial.DEEPSLATE_TOPAZ_ORE, SMPMaterial.TOPAZ)
			.put(SMPMaterial.SAPPHIRE_ORE, SMPMaterial.SAPPHIRE)
			.put(SMPMaterial.QARDITE_ORE, SMPMaterial.QARDITE)
			.put(SMPMaterial.DEEPSLATE_QARDITE_ORE, SMPMaterial.QARDITE)
			.build();
	
	private final String localization;
	
	private final String name;
	private ItemStack item;
	private final int levelUnlocked;
	private int customModelId;

	private final ChatColor cc;
	
	public static SMPMaterial matchEnum(String name) {
		try {
			return SMPMaterial.valueOf(name.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	public final SMPMaterial getOreDrops() throws IllegalArgumentException {
		if (!(ORE_DROPS.containsKey(this))) throw new IllegalArgumentException(this.name() + " is not included in ORE_DROPS");
		
		return ORE_DROPS.get(this);
	}
	
	public void setBlock(Block b) {
		setBlock(b.getLocation());
	}
	
	/**
	 * This will silently fail if the material is not a block.
	 * @param loc Location to use
	 */
	public void setBlock(Location loc) {
		if (!(this.item.getType().isBlock())) return;
		SMP plugin = JavaPlugin.getPlugin(SMP.class);
		
		Block b = loc.getWorld().getBlockAt(loc);
		b.setType(this.item.getType());
		
		PersistentDataContainer c = new BlockPDC(b);
		
		c.set(new NamespacedKey(plugin, "type"), PersistentDataType.STRING, this.localization);
	}
	
	public void setBlock(World w, int x, int y, int z) {
		setBlock(new Location(w, x, y, z));
	}
	
	private static HashMap<Attribute, AttributeModifier> genAttack(double attack, double knockback, double speed) {
		HashMap<Attribute, AttributeModifier> map = new HashMap<>();

		if (attack > 0) map.put(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("", attack, Operation.ADD_NUMBER));
		if (knockback > 0) map.put(Attribute.GENERIC_ATTACK_KNOCKBACK, new AttributeModifier("", knockback, Operation.ADD_NUMBER));
		if (speed > 0) map.put(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier("", speed, Operation.ADD_NUMBER));

		return map;
	}

	public static Color rgb(String hex) {
		return Color.fromRGB(Integer.valueOf(hex.substring(0, 2), 16), 
												 Integer.valueOf(hex.substring(2, 4), 16), 
												 Integer.valueOf(hex.substring(4, 6), 16));
	}
	
	private static HashMap<Enchantment, Integer> glint() {
		return new HashMap<>(ImmutableMap.<Enchantment, Integer>builder().put(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build());
	}

	private static HashMap<Enchantment, Integer> genTool(int unbreaking, int efficiency) {
		HashMap<Enchantment, Integer> map = new HashMap<>();

		if (unbreaking > 0) map.put(Enchantment.DURABILITY, unbreaking);
		if (efficiency > 0) map.put(Enchantment.DIG_SPEED, efficiency);
		
		return map;
	}
	
	private static HashMap<Enchantment, Integer> unbreak() {
		return genTool(100, 0);
	}
	
	public final int getID() {
		return this.customModelId;
	}

	static {
		for (SMPMaterial m : values()) {
			ItemStack item = m.getItem();
			ItemMeta meta = item.getItemMeta();
			meta.setCustomModelData(idCounter);
			item.setItemMeta(meta);
			m.item = item;

			m.customModelId = idCounter;
			idCounter++;
		}
	}

	private static HashMap<Attribute, AttributeModifier> genArmor(double armor, double toughness, double knockback) {
		HashMap<Attribute, AttributeModifier> map = new HashMap<>();

		if (armor > 0) map.put(Attribute.GENERIC_ARMOR, new AttributeModifier("", armor, Operation.ADD_NUMBER));
		if (toughness > 0) map.put(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier("", toughness, Operation.ADD_NUMBER));
		if (knockback > 0) map.put(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier("", knockback, Operation.ADD_NUMBER));

		return map;
	}

	private static HashMap<Enchantment, Integer> genBow(int power, int punch, boolean infinity, boolean flame) {
		HashMap<Enchantment, Integer> map = new HashMap<>();

		if (power > 0) map.put(Enchantment.ARROW_DAMAGE, power);
		if (punch > 0) map.put(Enchantment.ARROW_KNOCKBACK, punch);
		if (infinity) map.put(Enchantment.ARROW_INFINITE, 1);
		if (flame) map.put(Enchantment.ARROW_FIRE, 1);
		
		return map;
	}
	
	private static HashMap<Enchantment, Integer> genCrossbow(int piercing, int quickCharge, boolean multishot) {
		HashMap<Enchantment, Integer> map = new HashMap<>();

		if (quickCharge > 0) map.put(Enchantment.ARROW_DAMAGE, quickCharge);
		if (piercing > 0) map.put(Enchantment.ARROW_KNOCKBACK, piercing);
		if (multishot) map.put(Enchantment.MULTISHOT, 1);
		
		return map;
	}
	
	SMPMaterial(int level, Material original, String name) {
		if (level < 5) this.cc = ChatColor.WHITE;
		else if (level >= 5 && level < 15) this.cc = ChatColor.AQUA;
		else if (level >= 15 && level < 30) this.cc = ChatColor.LIGHT_PURPLE;
		else if (level > 30) this.cc = ChatColor.GOLD;
		else this.cc = ChatColor.WHITE;
		
		this.levelUnlocked = level;
		this.localization = name.toLowerCase().replace(" ", "_");
		
		ItemStack item = new ItemStack(original);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(cc + name);
		meta.setLocalizedName(name.toLowerCase().replace(" ", "_"));
		meta.addItemFlags(ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		this.item = item;

		this.name = name;
	}

	SMPMaterial(int level, Material original, String name, Map<Attribute, AttributeModifier> modifiers, Map<Enchantment, Integer> enchants) {
		if (level < 5) this.cc = ChatColor.WHITE;
		else if (level >= 5 && level < 15) this.cc = ChatColor.AQUA;
		else if (level >= 15 && level < 30) this.cc = ChatColor.LIGHT_PURPLE;
		else if (level > 30) this.cc = ChatColor.GOLD;
		else this.cc = ChatColor.WHITE;
		
		this.levelUnlocked = level;
		this.localization = name.toLowerCase().replace(" ", "_");
		
		ItemStack item = new ItemStack(original);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(cc + name);
		meta.setLocalizedName(name.toLowerCase().replace(" ", "_"));
		if (modifiers != null)
			for (Attribute a : modifiers.keySet()) {
				meta.addAttributeModifier(a, modifiers.get(a));
			}
		if (enchants != null)
			for (Enchantment e : enchants.keySet()) {
				meta.addEnchant(e, enchants.get(e), true);
			}
		
		if (meta.getEnchantLevel(Enchantment.DURABILITY) >= 100) {
			meta.removeEnchant(Enchantment.DURABILITY);
			meta.setUnbreakable(true);
		}
		
		meta.addItemFlags(ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		this.item = item;

		this.name = name;
	} 

	SMPMaterial(int level, Material original, String name, Map<Attribute, AttributeModifier> modifiers, Map<Enchantment, Integer> enchants, Color clr) {
		this(level, original, name, modifiers, enchants);

		LeatherArmorMeta meta = (LeatherArmorMeta) this.item.getItemMeta();
		meta.setColor(clr);
		this.item.setItemMeta(meta);
	}

	SMPMaterial(int level, Material original, String name, HashMap<Enchantment, Integer> enchants, Color clr) {
		this(level, original, name, null, enchants, clr);
	}

	SMPMaterial(int level, Material original, String name, Map<Attribute, AttributeModifier> modifiers, Color clr) {
		this(level, original, name, modifiers, null, clr);
	}

	SMPMaterial(int level, Material original, String name, Map<Attribute, AttributeModifier> modifiers) {
		this(level, original, name, modifiers, (Map<Enchantment, Integer>) null);
	}

	SMPMaterial(int level, Material original, String name, HashMap<Enchantment, Integer> enchants) {
		this(level, original, name, null, enchants);
	}
	
	@SafeVarargs
	SMPMaterial(int level, Material original, String name, HashMap<Enchantment, Integer>... enchants) {
		this(level, original, name, enchants[0]);
		
		for (int i = 1; i < enchants.length; i++) {
			this.item.addUnsafeEnchantments(enchants[i]);
		}
	}
	
	SMPMaterial(int level, String name, String value) {
		if (level < 5) this.cc = ChatColor.WHITE;
		else if (level >= 5 && level < 15) this.cc = ChatColor.AQUA;
		else if (level >= 15 && level < 30) this.cc = ChatColor.DARK_PURPLE;
		else if (level >= 30 && level < 50) this.cc = ChatColor.GOLD;
		else if (level > 50) this.cc = ChatColor.LIGHT_PURPLE;
		else this.cc = ChatColor.WHITE;

		this.levelUnlocked = level;
		this.localization = name.toLowerCase().replace(" ", "_");
		this.name = name;
		
		ItemStack item = Items.getHead(value);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(cc + name);
		meta.setLocalizedName(this.localization);
		meta.addItemFlags(ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		
		this.item = item;
	}

	public static ItemStack getItem(String localization) {
		return getByLocalization(localization).getItem();
	}

	public static SMPMaterial getByLocalization(String localization) {
		for (SMPMaterial m : values()) {
			if (m.localization.equals(localization)) return m;
		}

		return null;
	}

	public static SMPMaterial getByItem(ItemStack item) {
		if (!(item.hasItemMeta())) return null;
		if (!(item.getItemMeta().hasLocalizedName())) return null;

		return getByLocalization(item.getItemMeta().getLocalizedName());
	}
	
	public interface CleanOutput {
		
		Material getMaterial();
		
		int getAmount();
		
		class MaterialOutput implements CleanOutput {
			
			private final int amount;
			private final Material m;
			
			public MaterialOutput(Material m, int amount) {
				this.amount = amount;
				this.m = m;
			}
			
			public MaterialOutput(Material m) {
				this.m = m;
				this.amount = 1;
			}
			
			@Override
			public Material getMaterial() {
				return this.m;
			}

			@Override
			public int getAmount() {
				return this.amount;
			}
			
		}
	}
	
	public static Map<Material, CleanOutput> getMaterialCleanables() {
		return ImmutableMap.<Material, CleanOutput>builder()
				.put(Material.GOLD_INGOT, new MaterialOutput(Material.RAW_GOLD))
				.put(Material.IRON_INGOT, new MaterialOutput(Material.RAW_IRON))
				.put(Material.COPPER_INGOT, new MaterialOutput(Material.RAW_COPPER))
				.put(Material.NETHERITE_INGOT, new MaterialOutput(Material.NETHERITE_SCRAP, 4))
				
				.build();
	}

	public final String getName() {
		return this.name;
	}
	
	public final String getDisplayName() {
		return this.cc + this.name;
	}
	
	public final ItemStack getItem() {
		return getItem(1);
	}
	
	public final String getLocalization() {
		return this.localization;
	}

	public final int getLevelUnlocked() {
		return this.levelUnlocked;
	}

	public final ItemStack getItem(int amount) {
		if (amount < 1 || amount > 127) throw new IllegalArgumentException("Amount cannot be less than 1 or bigger than 127");

		ItemStack newItem = item;
		newItem.setAmount(amount);

		return newItem;
	}

	@Override
	public QueryID queryId() {
		return new QueryID("smpmaterial", name().toLowerCase());
	}

	@Override
	public final ItemStack genInfo() {
		ItemStack info = Queryable.super.genInfo();
		ItemMeta meta = info.getItemMeta();
		meta.setCustomModelData(this.customModelId);
		info.setItemMeta(meta);

		return info;
	}

	
}
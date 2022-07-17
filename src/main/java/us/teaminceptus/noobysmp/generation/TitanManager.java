package us.teaminceptus.noobysmp.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R1.block.CraftBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.world.level.block.state.BlockState;
import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.generation.biomes.TitanBiome;
import us.teaminceptus.noobysmp.materials.SMPMaterial;
import us.teaminceptus.noobysmp.util.SMPUtil;

public class TitanManager implements Listener {

	protected final SMP plugin;
	
	public TitanManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public static final Map<ChatColor, SMPMaterial> MATTER_DROPS = ImmutableMap.<ChatColor, SMPMaterial>builder()
	.put(ChatColor.GREEN, SMPMaterial.GREEN_MATTER)
	.put(ChatColor.DARK_GREEN, SMPMaterial.GREEN_MATTER)

	.put(ChatColor.GRAY, SMPMaterial.GRAY_MATTER)
	.put(ChatColor.DARK_GRAY, SMPMaterial.GRAY_MATTER)

	.put(ChatColor.GOLD, SMPMaterial.YELLOW_MATTER)
	.put(ChatColor.YELLOW, SMPMaterial.YELLOW_MATTER)

	.put(ChatColor.BLUE, SMPMaterial.BLUE_MATTER)
	.put(ChatColor.DARK_BLUE, SMPMaterial.BLUE_MATTER)
	.put(ChatColor.AQUA, SMPMaterial.BLUE_MATTER)
	.put(ChatColor.DARK_AQUA, SMPMaterial.BLUE_MATTER)

	.put(ChatColor.BLACK, SMPMaterial.BLACK_MATTER)
	.build();

	@EventHandler
	public void onInit(WorldInitEvent e) {
		if (!(e.getWorld().getName().equalsIgnoreCase("world_titan"))) return;

		e.getWorld().getPopulators().add(new TitanBiomePopulator());
	}

	public static class TitanBiomePopulator extends BlockPopulator {

		@Override
		public void populate(WorldInfo info, Random r, int x, int z, LimitedRegion region) {
			for (int y = info.getMinHeight(); y < info.getMaxHeight(); y++) {
				TitanBiome replaceable = TitanBiome.getReplaceable(region.getBiome(x, y, z));

				replaceable.setBiome(new Location(Bukkit.getWorld(info.getName()), x, y, z), false);
			}
		}

	}

	/**
	 * Will return {@link TitanManager#REPLACE_DATA} if it contains, else will use NMS to calculate the colored matter.
	 * @param m Material to get
	 * @return SMPMaterial replaceable
	 */
	public static SMPMaterial getReplaceable(Block b) {
		if (BlockManager.TITAN_REPLACEABLES.containsKey(b.getType())) {
			return BlockManager.TITAN_REPLACEABLES.get(b.getType());
		}

		BlockState nmsState = ((CraftBlock) b).getNMS();
		Color color = Color.fromRGB(nmsState.getBlock().defaultMaterialColor().col);

		ChatColor nearestColor = SMPUtil.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
		if (MATTER_DROPS.containsKey(nearestColor)) return MATTER_DROPS.get(nearestColor);
		
		return SMPMaterial.GRAY_MATTER;
	}

	/**
	 * Used for async updates in setting chunks 
	 *
	 */
	public static class AsyncLocation {
		
		private final String worldName;
		private final double x;
		private final double y;
		private final double z;
		
		public AsyncLocation(String name, double x, double y, double z) {
			this.worldName = name;
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		/**
		 * Must be called during a Synchronous Call to become thread-safe.
		 * @return Synchronous Location 
		 */
		public Location toLocation() {
			return new Location(Bukkit.getWorld(worldName), x, y, z);
		}
		
		public String getWorldName() {
			return this.worldName;
		}
		
		public double getX() {
			return this.x;
		}
		
		public double getY() {
			return this.y;
		}
		
		public double getZ() {
			return this.z;
		}
		
	}
	
	public record AsyncBiomeSelection(AsyncLocation loc, TitanBiome biome) {}

	private void updateBlocks(List<AsyncLocation> selected) {
		new BukkitRunnable() {
			public void run() {
				for (AsyncLocation aloc : selected) {
					Location loc = aloc.toLocation();
					
					SMPMaterial replaced = BlockManager.TITAN_REPLACEABLES.get(loc.getBlock().getType());
					replaced.setBlock(loc);
				}
			}
		}.runTask(plugin);
	}
	
	private void updateBiomes(List<AsyncBiomeSelection> selected) {
		new BukkitRunnable() {
			public void run() {
				for (AsyncBiomeSelection s : selected) {
					s.biome().setBiome(s.loc().toLocation(), false);
				}
			}
		}.runTask(plugin);
	}
	
	@EventHandler
	public void onLoad(ChunkLoadEvent e) {
		ChunkSnapshot snap = e.getChunk().getChunkSnapshot(true, true, true);
		
		new BukkitRunnable() {
			public void run() {
				List<AsyncLocation> locs = new ArrayList<>();
				List<AsyncBiomeSelection> biomes = new ArrayList<>();
				for (int x = 0; x < 16; x++) {
					for (int z = 0; z < 16; z++) {
						for (int y = -64; y < snap.getHighestBlockYAt(x, z); y++) {
							AsyncLocation loc = new AsyncLocation(snap.getWorldName(), x + (snap.getX() * 16), y, z + (snap.getZ() * 16));
							if (e.isNewChunk()) {
								Material m = snap.getBlockType(x, y, z);
								if (BlockManager.TITAN_REPLACEABLES.containsKey(m)) {
									locs.add(loc);
								}
							}
							
							
							Biome b = snap.getBiome(x, y, z);
							biomes.add(new AsyncBiomeSelection(loc, TitanBiome.getReplaceable(b)));
						}
					}
				}
				
				updateBlocks(locs);
				updateBiomes(biomes);
			}
		}.runTaskAsynchronously(plugin);
	}

}

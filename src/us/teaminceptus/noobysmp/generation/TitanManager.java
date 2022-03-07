package us.teaminceptus.noobysmp.generation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

public class TitanManager implements Listener {

	protected SMP plugin;
	
	public TitanManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
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
	
	@EventHandler
	public void onLoad(ChunkLoadEvent e) {
		ChunkSnapshot snap = e.getChunk().getChunkSnapshot(true, true, true);
		
		new BukkitRunnable() {
			public void run() {
				List<AsyncLocation> locs = new ArrayList<>();
				for (int x = 0; x < 16; x++) {
					for (int z = 0; z < 16; z++) {
						for (int y = -64; y < snap.getHighestBlockYAt(x, z); y++) {
							Material m = snap.getBlockType(x, y, z);
							if (BlockManager.TITAN_REPLACEABLES.containsKey(m)) {
								locs.add(new AsyncLocation(snap.getWorldName(), x + (snap.getX() * 16), y, z + (snap.getZ() * 16)));
							}
						}
					}
				}
				
				updateBlocks(locs);
			}
		}.runTaskAsynchronously(plugin);
	}

}

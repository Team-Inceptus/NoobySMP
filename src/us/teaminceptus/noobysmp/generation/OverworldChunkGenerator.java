package us.teaminceptus.noobysmp.generation;

import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import com.google.common.collect.ImmutableMap;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

public class OverworldChunkGenerator implements Listener {
	
	protected SMP plugin;
	
	public OverworldChunkGenerator(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	private static Random r = new Random();
	
	private void setVein(Block b, SMPMaterial m) {
		m.setBlock(b);
		
		for (BlockFace f : BlockFace.values()) {
			if (b.getRelative(f).getType() == b.getType()) {
				setVein(b.getRelative(f), m);
			}
		}
	}
	
	public static final Map<SMPMaterial, ReplaceData> REPLACE_DATA = ImmutableMap.<SMPMaterial, ReplaceData>builder()
			.put(SMPMaterial.RUBY_ORE, new ReplaceData(25, Material.DIAMOND_ORE))
			.put(SMPMaterial.DEEPSLATE_RUBY_ORE, new ReplaceData(25, Material.DEEPSLATE_DIAMOND_ORE))
			
			.build();
	
	public static class ReplaceData {
		
		private final int chance;
		private final Material toReplace;
		private final boolean ore;
		
		public ReplaceData(int chance, Material toReplace) {
			this.chance = chance;
			this.toReplace = toReplace;
			this.ore = false;
		}
		
		public ReplaceData(int chance, Material toReplace, boolean ore) {
			this.chance = chance;
			this.toReplace = toReplace;
			this.ore = ore;
		}
		
		public final int getChance() {
			return this.chance;
		}
		
		public final Material getReplace() {
			return this.toReplace;
		}
		
		public final boolean isOre() {
			return this.ore;
		}
		
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e) {
		if (!(e.isNewChunk())) return;
		
		Chunk c = e.getChunk();
		World w = c.getWorld();
		if (!(w.getName().equalsIgnoreCase("world"))) return;
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = w.getMinHeight(); y < w.getMaxHeight(); y++) {
					Block b = c.getBlock(x, y, z);
					Material m = b.getType();
					
					for (SMPMaterial sm : REPLACE_DATA.keySet()) {
						ReplaceData data = REPLACE_DATA.get(sm);
						
						if (data.getReplace() == m && r.nextInt(100) < data.getChance()) {
							if (data.isOre()) setVein(b, sm);
							else sm.setBlock(b);
						}
						
						break;
					}
				}
			}
		}
		
		
	}
	
}

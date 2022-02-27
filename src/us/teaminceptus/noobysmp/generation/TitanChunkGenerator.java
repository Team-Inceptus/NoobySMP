package us.teaminceptus.noobysmp.generation;

import java.util.Random;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import us.teaminceptus.noobysmp.SMP;

public class TitanChunkGenerator extends ChunkGenerator {
	
	protected SMP plugin;
	
	public TitanChunkGenerator(SMP plugin) {
		this.plugin = plugin;
	}
	
	public void generateNoise(WorldInfo info, Random r, int chunkX, int chunkZ, ChunkData chunk) {
		
	}
	
	public boolean shouldGenerateStructures() { return false; }
	
}

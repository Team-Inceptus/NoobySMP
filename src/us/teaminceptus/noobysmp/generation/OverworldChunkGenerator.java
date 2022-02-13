package us.teaminceptus.noobysmp.generation;

import org.bukkit.generator.ChunkGenerator;

import us.teaminceptus.noobysmp.SMP;

public class OverworldChunkGenerator extends ChunkGenerator {
	
	protected SMP plugin;
	
	public OverworldChunkGenerator(SMP plugin) {
		this.plugin = plugin;
	}
	
}

package us.teaminceptus.noobysmp.generation.biomes;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeBuilder;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import net.minecraft.world.level.biome.MobSpawnSettings;
import us.teaminceptus.noobysmp.SMP;

public enum TitanBiome {

	// TODO create titan biomes
	
	;
	
	private final String name;

	private final boolean frozen;
	private final String waterColor;
	private final String fogColor;
	private final String skyColor;
	private final String grassColor;
	private final String foliageColor;

	private Biome nmsBiome;
	private ResourceKey<Biome> resourceKey;
	
	private TitanBiome(String name, boolean snow, String waterColor, String fogColor, String skyColor, String grassColor, String foliageColor) {
		this.name = name;
		this.frozen = snow;
		this.waterColor = waterColor;
		this.fogColor = fogColor;
		this.skyColor = skyColor;
		this.grassColor = grassColor;
		this.foliageColor = foliageColor;
	}

	// Fetching Methods

	public final boolean isFrozen() {
		return this.frozen;
	}

	private static final Color fromHex(String hex) {
		return Color.fromRGB(
			Integer.parseInt(hex.substring(0, 2), 16),
			Integer.parseInt(hex.substring(2, 4), 16),
			Integer.parseInt(hex.substring(4, 6), 16)
		);
	}

	public final Color getWaterColor() { // Used in both water color and water fog color
		return fromHex(this.waterColor);
	}

	public final Color getFogColor() {
		return fromHex(this.fogColor);
	}

	public final Color getSkyColor() {
		return fromHex(this.skyColor);
	}

	public final Color getGrassColor() {
		return fromHex(this.grassColor);
	}

	public final Color getFoliageColor() { // Foliage is colors of leaves, fines, and more
		return fromHex(this.foliageColor);
	}
	
	public final String getName() {
		return this.name;
	}

	public final Biome getNMSBiome() {
		return this.nmsBiome;
	}

	public final ResourceKey<Biome> getResourceKey() {
		return this.resourceKey;
	}

	// Registry & Setting Methods
	
	public static void registerBiomes() throws Exception {
		SMP plugin = JavaPlugin.getPlugin(SMP.class);

		ResourceKey<Registry<Biome>> REGISTRY_KEY = Registry.BIOME_REGISTRY;
		DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
		
		for (TitanBiome biome : values()) { 
			ResourceKey<Biome> key = ResourceKey.<Biome>create(REGISTRY_KEY, new ResourceLocation("noobysmp", biome.name.toLowerCase().replace(' ', '_')));
			biome.resourceKey = key;
	
			ResourceKey<Biome> oldKey = ResourceKey.<Biome>create(REGISTRY_KEY, new ResourceLocation("minecraft", "forest"));
			WritableRegistry<Biome> registrywritable = server.registryAccess().ownedRegistryOrThrow(REGISTRY_KEY);
			Biome forestbiome = registrywritable.get(oldKey);
			
			BiomeBuilder builder = new Biome.BiomeBuilder();
			builder.biomeCategory(forestbiome.getBiomeCategory());
			builder.precipitation(forestbiome.getPrecipitation());
			
			Field biomeSettingMobsField = Biome.class.getDeclaredField("mobSettings");
			biomeSettingMobsField.setAccessible(true);
			MobSpawnSettings biomeSettingMobs = (MobSpawnSettings) biomeSettingMobsField.get(forestbiome);
			builder.mobSpawnSettings(biomeSettingMobs);	
			
			Field biomeSettingGenField = Biome.class.getDeclaredField("generationSettings");
			biomeSettingGenField.setAccessible(true);
			BiomeGenerationSettings biomeSettingGen = (BiomeGenerationSettings) biomeSettingGenField.get(forestbiome);
			builder.generationSettings(biomeSettingGen);
	
			builder.downfall(0.8F);
			builder.temperature(0.7F);
			if (biome.frozen) builder.temperatureAdjustment(Biome.TemperatureModifier.FROZEN);
			else builder.temperatureAdjustment(Biome.TemperatureModifier.NONE);
			
			BiomeSpecialEffects.Builder effectbuilder = new BiomeSpecialEffects.Builder();
			effectbuilder.grassColorModifier(GrassColorModifier.NONE);
			effectbuilder.fogColor(Integer.parseInt(biome.fogColor, 16));
			effectbuilder.waterFogColor(Integer.parseInt(biome.fogColor, 16));
			effectbuilder.skyColor(Integer.parseInt(biome.skyColor, 16));
			effectbuilder.waterColor(Integer.parseInt(biome.waterColor, 16));
			
			effectbuilder.foliageColorOverride(Integer.parseInt(biome.foliageColor, 16));
			effectbuilder.grassColorOverride(Integer.parseInt(biome.grassColor, 16));
	
			builder.specialEffects(effectbuilder.build());

			biome.nmsBiome = builder.build();
		}

		plugin.getLogger().info("Registered " + Integer.toString(TitanBiome.values().length) + " Titan Biomes");
	}

	public void setBiome(Chunk c) {
		Level w = ((CraftWorld) c.getWorld()).getHandle();
		
		for (int x = 0; x <= 15; x++) {
			for (int z = 0; z <= 15; z++) {
				for(int y = 0; y <= c.getWorld().getMaxHeight(); y++) {
					setBiome(c.getX() * 16 + x, y, c.getZ() * 16 + z, w, nmsBiome);
				}
			}
		}
		updateChunksForAll(c);
	}

	public void setBiome(Location loc) {
		Level w = ((CraftWorld) loc.getWorld()).getHandle();

		setBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), w, nmsBiome);
		updateChunksForAll(loc.getChunk());
	}

	public static void setBiome(Chunk c, TitanBiome biome) {
		biome.setBiome(c);
	}	

	public static void setBiome(Location loc, TitanBiome biome) {
		biome.setBiome(loc);
	}

	private void setBiome(int x, int y, int z, Level w, Biome biome) {
		BlockPos pos = new BlockPos(x, 0, z);

		if (w.isLoaded(pos)) {
			net.minecraft.world.level.chunk.LevelChunk chunk = w.getChunkAt(pos);
			if (chunk != null) { 	
				chunk.setBiome(x >> 2, y >> 2, z >> 2, biome);
			}
		}
	}

	private static void updateChunksForAll(Chunk chunk) {
		// net.minecraft.world.level.chunk.LevelChunk c = ((CraftChunk)chunk).getHandle();
		for (Player player : chunk.getWorld().getPlayers()) {
				if (player.isOnline()) {
						if((player.getLocation().distance(chunk.getBlock(0, 0, 0).getLocation()) < (Bukkit.getServer().getViewDistance() * 16))) {
								// FIXME why ((CraftPlayer) player).getHandle().connection.send(new ClientboundLevelChunkWithLightPacket(c, , null, null, true));
						}
				}
		}
	}
	
}
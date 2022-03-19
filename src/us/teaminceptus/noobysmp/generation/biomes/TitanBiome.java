package us.teaminceptus.noobysmp.generation.biomes;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_18_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Lifecycle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome.BiomeBuilder;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import us.teaminceptus.noobysmp.SMP;

public enum TitanBiome {

	// null in waterColor, skyColor, fogColor will set to default
	// Use Null in last arg for foliage same as grass
	
	WITHERED_PLAINS("Withered Plains", false, "6b7077", "1e1e1e", null, "3d3d3d", null),
	WITHERED_SWAMP("Withered Swamp", false, "5e5846", "1e1e1e", null, "2d2d2d", null),
	WITHERED_BEACH("Withered Beach", false, "797f87", "1e1e1e", null, "4d4d4d", null),
	WITHERED_FIELDS("Withered Fields", false, "6b7077", "1e1e1e", null, "2b2b2b", null),
	
	WITHERED_FOREST("Withered Forest", false, "6b7077", "1e1e1e", null, "4d4d4d", null),
	BLOSSOM_FOREST("Blossom Forest", false, null, null, null, "f405fc", null),
	COLD_BLOSSOM_FOREST("Cold Blossom Forest", false, null, null, null, "6a00ce", null),
	STEAMING_BLOSSOM_FOREST("Steaming Blossom Forest", false, null, null, null, "ce0060", null),
	NAVY_FOREST("Navy Forest", false, null, null, null, "000acc", "2a35fc"),
	
	LIME_PEAKS("Lime Peaks", false, null, null, null, "c1fc2a", null),
	FROZEN_LIME_PEAKS("Frozen Lime Peaks", false, null, null, null, "2afc58", null),
	
	BLUE_SAVANNA("Blue Savanna", false, "96f1ff", null, null, "03a8c1", null),
	STEAMING_BLUE_SAVANNA("Steaming Blue Savanna", false, "75ecff", null, null, "048ea3", null),
	
	STEAMING_JUNGLE("Steaming Jungle", false, null, null, "a1adfc", "e80909", "ff7f7f"),
	BOILING_JUNGLE("Boiling Jungle", false, null, null, "8998f9", "d60a0a", "fc6a6a"),
	
	BOILING_MUSHROOM_FIELDS("Boiling Mushroom Fields", false, "020475", "ffbfd8", null, "f92c7e", null),
	
	YELLOW_TUNDRA("Yellow Tundra", true, "fff247", "fcf69c", null, "d8ca06", "fff23f"), 
	
	PINK_DESERT("Pink Desert", false, "ffa5de", null, null, "ff8cd4", null),
	PURPLE_DESERT("Purple Desert", false, "c2a5ff", null, null, "da8cff", null),
	
	GREEN_RIVER("Green River", false, "a0f7c0", null, "bffcd6", OCEAN_GRASS(), null),
	GREEN_OCEAN("Green Ocean", false, "a0f7c0", null, "bffcd6", OCEAN_GRASS(), null),
	STEAMING_GREEN_OCEAN("Steaming Green Ocean", false, "02d64f", null, "bffcd6", OCEAN_GRASS(), null),
	COLD_GREEN_OCEAN("Cold Green Ocean", false, "bcffd5", null, "bffcd6", OCEAN_GRASS(), null),
	FROZEN_GREEN_OCEAN("Frozen Green Ocean", true, "d3ffe3", null, "bffcd6", OCEAN_GRASS(), null),
	
	;
	
	public static final String PLAINS_GRASS() { return "91BD59"; }
	public static final String DESERT_GRASS() { return "BFB755"; }
	public static final String JUNGLE_GRASS() { return "59C93C"; }
	public static final String OCEAN_GRASS() { return "8EB971"; };
	
	private final String name;

	private final boolean frozen;
	private final String waterColor;
	private final String fogColor;
	private final String skyColor;
	private final String grassColor;
	private final String foliageColor;

	private net.minecraft.world.level.biome.Biome nmsBiome;
	private ResourceKey<net.minecraft.world.level.biome.Biome> resourceKey;

	private TitanBiome(String name, boolean snow, String waterColor, String fogColor, String skyColor, String grassColor, String foliageColor) {
		this.name = name;
		this.frozen = snow;
		this.waterColor = (waterColor == null ? "3F76E4" : waterColor);
		this.fogColor = (fogColor == null ? "C0D8FF" : fogColor);
		this.skyColor = (skyColor == null ? "78A7FF" : skyColor);
		this.grassColor = grassColor;
		this.foliageColor = (foliageColor == null ? grassColor : foliageColor);
	}
	
	public static final TitanBiome getReplaceable(Biome b) {
		for (TitanBiome t : REPLACEABLES.keySet()) {
			if (Arrays.asList(REPLACEABLES.get(t)).contains(b)) {
				return t;
			}
		}
		
		return TitanBiome.WITHERED_PLAINS;
	}
	
	public static final Map<TitanBiome, Biome[]> REPLACEABLES = ImmutableMap.<TitanBiome, Biome[]>builder()
			.put(TitanBiome.WITHERED_PLAINS, new Biome[] {
				Biome.PLAINS,
				Biome.SUNFLOWER_PLAINS,
			})
			.put(TitanBiome.WITHERED_SWAMP, new Biome[] { Biome.SWAMP})
			.put(TitanBiome.WITHERED_BEACH, new Biome[] {
				Biome.BEACH,
				Biome.SNOWY_BEACH
			})
			.put(TitanBiome.WITHERED_FOREST, new Biome[] { Biome.BIRCH_FOREST})
			.put(TitanBiome.BLOSSOM_FOREST, new Biome[] { Biome.FOREST })
			.put(TitanBiome.COLD_BLOSSOM_FOREST, new Biome[] { Biome.WINDSWEPT_FOREST })
			.put(TitanBiome.STEAMING_BLOSSOM_FOREST, new Biome[] { Biome.FLOWER_FOREST })
			.put(TitanBiome.NAVY_FOREST, new Biome[] { Biome.DARK_FOREST })
			.put(TitanBiome.LIME_PEAKS, new Biome[] {
				Biome.STONY_PEAKS,
				Biome.JAGGED_PEAKS
			})
			.put(TitanBiome.FROZEN_LIME_PEAKS, new Biome[] {
					Biome.FROZEN_PEAKS,
					Biome.SNOWY_SLOPES
			})
			.put(TitanBiome.BLUE_SAVANNA, new Biome[] {
				Biome.SAVANNA,
				Biome.SAVANNA_PLATEAU,
			})
			.put(TitanBiome.STEAMING_BLUE_SAVANNA, new Biome[] {
				Biome.WINDSWEPT_SAVANNA
			})
			.put(TitanBiome.STEAMING_JUNGLE, new Biome[] {
				Biome.JUNGLE,
				Biome.BAMBOO_JUNGLE
			})
			.put(TitanBiome.BOILING_JUNGLE, new Biome[] { Biome.SPARSE_JUNGLE })
			.put(TitanBiome.BOILING_MUSHROOM_FIELDS, new Biome[] { Biome.MUSHROOM_FIELDS })
			.put(TitanBiome.YELLOW_TUNDRA, new Biome[] { Biome.SNOWY_TAIGA })
			.put(TitanBiome.PINK_DESERT, new Biome[] { Biome.DESERT })
			.put(TitanBiome.PURPLE_DESERT, new Biome[] {
				Biome.BADLANDS,
				Biome.ERODED_BADLANDS,
				Biome.WOODED_BADLANDS
			})
			.put(TitanBiome.GREEN_RIVER, new Biome[] {
				Biome.RIVER,
				Biome.FROZEN_RIVER
			})
			.put(TitanBiome.GREEN_OCEAN, new Biome[] {
				Biome.OCEAN,
				Biome.LUKEWARM_OCEAN,
				Biome.DEEP_OCEAN
			})
			.put(TitanBiome.STEAMING_GREEN_OCEAN, new Biome[] {
				Biome.DEEP_LUKEWARM_OCEAN,
				Biome.WARM_OCEAN
			})
			.put(TitanBiome.COLD_GREEN_OCEAN, new Biome[] {
				Biome.COLD_OCEAN,
				Biome.DEEP_COLD_OCEAN
			})
			.put(TitanBiome.FROZEN_GREEN_OCEAN, new Biome[] {
				Biome.DEEP_FROZEN_OCEAN,
				Biome.FROZEN_OCEAN,
			})
			
			.build();
	
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

	public final net.minecraft.world.level.biome.Biome getNMSBiome() {
		return this.nmsBiome;
	}

	public final ResourceKey<net.minecraft.world.level.biome.Biome> getResourceKey() {
		return this.resourceKey;
	}

	// Registry & Setting Methods
	
	public static void changeRegistryLock(boolean isLocked) {
		DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
        MappedRegistry<net.minecraft.world.level.biome.Biome> materials = (MappedRegistry<net.minecraft.world.level.biome.Biome>) server.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY);
        try {
	        Field isFrozen = materials.getClass().getDeclaredField("bL");
	        isFrozen.setAccessible(true);
	        isFrozen.set(materials, isLocked);  
        } catch (Exception e) {
        	JavaPlugin.getPlugin(SMP.class).getLogger().info("Error changing biome lock to " + isLocked);
        	e.printStackTrace();
        }
	}
	
	public static void registerBiomes() throws Exception {
		SMP plugin = JavaPlugin.getPlugin(SMP.class);
		
		changeRegistryLock(false);
		
		WritableRegistry<net.minecraft.world.level.biome.Biome> registrywritable = (WritableRegistry<net.minecraft.world.level.biome.Biome>) ((CraftServer) Bukkit.getServer()).getServer().registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY);
		for (TitanBiome biome : values()) { 
			ResourceKey<net.minecraft.world.level.biome.Biome> key = ResourceKey.<net.minecraft.world.level.biome.Biome>create(Registry.BIOME_REGISTRY, new ResourceLocation("noobysmp", biome.name.toLowerCase().replace(' ', '_')));
			biome.resourceKey = key;
			
			ResourceKey<net.minecraft.world.level.biome.Biome> oldKey = Biomes.FOREST;
			
			net.minecraft.world.level.biome.Biome forestbiome = registrywritable.get(oldKey);
			if (registrywritable.containsKey(key)) continue;
			
			
			BiomeBuilder builder = new net.minecraft.world.level.biome.Biome.BiomeBuilder();
			builder.biomeCategory(BiomeCategory.NONE);
			builder.precipitation(forestbiome.getPrecipitation());
			
			Field biomeSettingMobsField = net.minecraft.world.level.biome.Biome.class.getDeclaredField("k");
			biomeSettingMobsField.setAccessible(true);
			MobSpawnSettings biomeSettingMobs = (MobSpawnSettings) biomeSettingMobsField.get(forestbiome);
			builder.mobSpawnSettings(biomeSettingMobs);	
			
			Field biomeSettingGenField = net.minecraft.world.level.biome.Biome.class.getDeclaredField("j");
			biomeSettingGenField.setAccessible(true);
			BiomeGenerationSettings biomeSettingGen = (BiomeGenerationSettings) biomeSettingGenField.get(forestbiome);
			builder.generationSettings(biomeSettingGen);
	
			builder.downfall(0.8F);
			builder.temperature(0.7F);
			if (biome.frozen) builder.temperatureAdjustment(net.minecraft.world.level.biome.Biome.TemperatureModifier.FROZEN);
			else builder.temperatureAdjustment(net.minecraft.world.level.biome.Biome.TemperatureModifier.NONE);
			
			BiomeSpecialEffects.Builder effectbuilder = new BiomeSpecialEffects.Builder();
			effectbuilder.grassColorModifier(GrassColorModifier.NONE);
			effectbuilder.fogColor(Integer.parseInt(biome.fogColor, 16));
			effectbuilder.waterFogColor(Integer.parseInt(biome.fogColor, 16));
			effectbuilder.skyColor(Integer.parseInt(biome.skyColor, 16));
			effectbuilder.waterColor(Integer.parseInt(biome.waterColor, 16));
			
			effectbuilder.foliageColorOverride(Integer.parseInt(biome.foliageColor, 16));
			effectbuilder.grassColorOverride(Integer.parseInt(biome.grassColor, 16));
	
			builder.specialEffects(effectbuilder.build());

			net.minecraft.world.level.biome.Biome nmsbiome = builder.build();
			biome.nmsBiome = nmsbiome;
			
			registrywritable.register(key, nmsbiome, Lifecycle.stable());
		}
		
		changeRegistryLock(true);
		plugin.getLogger().info("Registered " + Integer.toString(TitanBiome.values().length) + " Titan Biomes");
	}

	public void setBiome(Chunk c, boolean packet) {
		Level w = ((CraftWorld) c.getWorld()).getHandle();
		
		for (int x = 0; x <= 15; x++) {
			for (int z = 0; z <= 15; z++) {
				for(int y = 0; y <= c.getWorld().getMaxHeight(); y++) {
					setBiome(c.getX() * 16 + x, y, c.getZ() * 16 + z, w, this);
				}
			}
		}
		if (packet) updateChunksForAll(c);
	}
	
	public void setBiome(Chunk c) {
		setBiome(c, true);
	}

	public void setBiome(Location loc, boolean packet) {
		Level w = ((CraftWorld) loc.getWorld()).getHandle();

		setBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), w, this);
		if (packet) updateChunksForAll(loc.getChunk());
	}
	
	public void setBiome(Location loc) {
		setBiome(loc, true);
	}

	public static void setBiome(Chunk c, TitanBiome biome, boolean packet) {
		biome.setBiome(c, packet);
	}
	
	public static void setBiome(Chunk c, TitanBiome biome) {
		setBiome(c, biome, true);
	}

	public static void setBiome(Location loc, TitanBiome biome, boolean packet) {
		biome.setBiome(loc, packet);
	}
	
	public static void setBiome(Location loc, TitanBiome biome) {
		setBiome(loc, biome, true);
	}

	private void setBiome(int x, int y, int z, Level w, TitanBiome tbiome) {
		BlockPos pos = new BlockPos(x, 0, z);
		
		if (w.isLoaded(pos)) {
			net.minecraft.world.level.chunk.LevelChunk chunk = w.getChunkAt(pos);
			if (chunk != null) { 	
				chunk.setBiome(x >> 2, y >> 2, z >> 2, Holder.direct(tbiome.nmsBiome));
			}
		}
	}

	private static void updateChunksForAll(Chunk chunk) {
		net.minecraft.world.level.chunk.LevelChunk c = ((CraftChunk)chunk).getHandle();
		for (Player player : chunk.getWorld().getPlayers()) {
			if (player.isOnline()) {
				if((player.getLocation().distance(chunk.getBlock(0, 0, 0).getLocation()) < (Bukkit.getServer().getViewDistance() * 16))) {
					((CraftPlayer) player).getHandle().connection.send(new ClientboundLevelChunkWithLightPacket(c, c.getLevel().getLightEngine(), null, null, true));
				}
			}
		}
	}
	
}
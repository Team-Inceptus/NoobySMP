package us.teaminceptus.noobysmp.generation.biomes;

public enum TitanBiome {

	// TODO create titan biomes
	SOUL_GRASSLANDS("Soul Grasslands");
	
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
		return new Color(
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
	
	private static void registerBiomes() {
		SMP plugin = JavaPlugin.getPlugin(SMP.class);

		RegistryKey REGISTRY_KEY = Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY;
		DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
		
		for (TitanBiome biome : values()) { 
			ResourceKey<Biome> key = ResourceKey.create(REGISTRY_KEY, new ResourceLocation("noobysmp", biome.name.toLowerCase().replace(' ', '_')));
			biome.resourceKey = key;
	
			ResourceKey<Biome> oldKey = ResourceKey.create(REGISTRY_KEY, new ResourceLocation("minecraft", "forest"));
			WritableRegistry<Biome> registrywritable = server.registryAccess().ownedRegistryOrThrow(REGISTRY_KEY);
			Biome forestbiome = registrywritable.register(oldKey);
			
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

		plugin.getLogger().info("Registered " + Integer.toString(TitamBiome.values()) + " Titan Biomes");
	}

	public void setBiome(Chunk c) {
		World w = ((CraftWorld) c.getWorld()).getHandle();
		
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
		World w = ((CraftWorld) loc.getWorld()).getHandle();

		setBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), w, nmsBiome);
		updateChunksForAll(loc.getChunk());
	}

	public static void setBiome(Chunk c, TitanBiome biome) {
		biome.setBiome(c);
	}

	public static void setBiome(Location loc, TitanBiome biome) {
		biome.setBiome(loc);
	}

	private void setBiome(int x, int y, int z, World w, Biome biome) {
		BlockPos pos = new BlockPos(x, 0, z);

		if (w.isLoaded(pos)) {
		 net.minecraft.world.level.chunk.LevelChunk chunk = w.getChunkAtWorldCoords(pos);
		 if (chunk != null) {
	 
				chunk.getBiomeIndex().setBiome(x >> 2, y >> 2, z >> 2, biome);
				chunk.markDirty();
		 }
		}
	}

	private static void updateChunksForAll(Chunk chunk) {
		net.minecraft.world.level.chunk.LevelChunk c = ((CraftChunk)chunk).getHandle();
		for (Player player : chunk.getWorld().getPlayers()) {
				if (player.isOnline()) {
						if((player.getLocation().distance(chunk.getBlock(0, 0, 0).getLocation()) < (Bukkit.getServer().getViewDistance() * 16))) {
								((CraftPlayer) player).getHandle().playerConnection.sendPacket(new ClientboundLevelChunkPacketData(c));
						}
				}
		}
	}
	
}
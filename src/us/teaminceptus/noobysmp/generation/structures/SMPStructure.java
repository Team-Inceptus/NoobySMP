package us.teaminceptus.noobysmp.generation.structures;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.structure.Structure;

public enum SMPStructure {
    
    // TODO Create structure schematics
    PYRAMID("Pyramid", null, new StructureData("world", 1, Biome.DESERT)),
    PILLAGER_CAPITAL("Pillager Capital", null, new StructureData("world", 1, 10000, Biome.BADLANDS))
    ;

    public static record StructureData(String world, int chance, int intScale, int yLevel, Biome biome) {
        public StructureData(String world, int chance, int intScale, Biome biome) {
            this(world, chance, intScale, -65, biome);
        }

        public StructureData(String world, int chance, Biome biome) {
            this(world, chance, 1000, biome);
        }
    };

    private final String name;
    private final StructureData data;

    private File schematic;
    private Structure structure;


    private SMPStructure(String name, File schematic, StructureData data) {
        this.name = name;
        this.data = data;
        try {
            this.schematic = schematic;
            this.structure = Bukkit.getStructureManager().loadStructure(schematic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final String getName() {
        return this.name;
    }

    public final File getSchematic() {
        return this.schematic;
    }

    public final Structure getStructure() {
        return this.structure;
    }

    public final StructureData getData() {
        return this.data;
    }
}

package us.teaminceptus.noobysmp.util;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import us.teaminceptus.noobysmp.SMP;

public class BlockPDC implements PersistentDataContainer {

    private static final Pattern KEY_REGEX = Pattern.compile("^x(\\d+)y(-?\\d+)z(\\d+)$");
    private static final int CHUNK_MIN = 0;
    private static final int CHUNK_MAX = 15;
    private static boolean hasWorldInfoGetMinHeightMethod;
    private final PersistentDataContainer pdc;
    private final Chunk chunk;
    private final NamespacedKey key;

    private static SMP plugin;
    
    static {
        try {
            Class.forName("org.bukkit.generator.WorldInfo");
            hasWorldInfoGetMinHeightMethod = true;
        } catch (ClassNotFoundException e) {
            hasWorldInfoGetMinHeightMethod = false;
        }
    }
    
    public BlockPDC(Block block) {
    	plugin = JavaPlugin.getPlugin(SMP.class);
    	
        this.chunk = block.getChunk();
        this.key = new NamespacedKey(plugin, getOldKey(block));
        this.pdc = getPersistentDataContainer();
    }

    public static List<Block> getBlocksWithCustomData(Chunk chunk) {
        NamespacedKey dummy = new NamespacedKey(plugin, "dummy");
        PersistentDataContainer chunkPDC = chunk.getPersistentDataContainer();
        return chunkPDC.getKeys().stream()
                .filter(key -> key.getNamespace().equals(dummy.getNamespace()))
                .map(key -> getBlockFromKey(key, chunk))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static Block getBlockFromKey(NamespacedKey key, Chunk chunk) {
        Matcher matcher = KEY_REGEX.matcher(key.getKey());
        if(!matcher.matches()) return null;
        int x = Integer.parseInt(matcher.group(1));
        int y = Integer.parseInt(matcher.group(2));
        int z = Integer.parseInt(matcher.group(3));
        if((x < CHUNK_MIN || x > CHUNK_MAX)
            || (z < CHUNK_MIN || z > CHUNK_MAX)
            || (y < getWorldMinHeight(chunk.getWorld()) || y > chunk.getWorld().getMaxHeight() - 1)) return null;
        return chunk.getBlock(x,y,z);
    }

    private static int getWorldMinHeight(World world) {
        if(hasWorldInfoGetMinHeightMethod) {
            return world.getMinHeight();
        } else {
            return 0;
        }
    }

    private static String getOldKey(Block block) {
        int x = block.getX() & 0x000F;
        int y = block.getY();
        int z = block.getZ() & 0x000F;
        return String.format("x%dy%dz%d", x, y, z);
    }


    public void clear() {
        pdc.getKeys().forEach(pdc::remove);
        save();
    }

    private PersistentDataContainer getPersistentDataContainer() {
        PersistentDataContainer chunkPDC = chunk.getPersistentDataContainer();
        PersistentDataContainer blockPDC;
        if (chunkPDC.has(key, PersistentDataType.TAG_CONTAINER)) {
            blockPDC = chunkPDC.get(key, PersistentDataType.TAG_CONTAINER);
            assert blockPDC != null;
            return blockPDC;
        }
        blockPDC = chunkPDC.getAdapterContext().newPersistentDataContainer();
        return blockPDC;
    }

    private void save() {
        if (pdc.isEmpty()) {
            chunk.getPersistentDataContainer().remove(key);
        } else {
            chunk.getPersistentDataContainer().set(key, PersistentDataType.TAG_CONTAINER, pdc);
        }
    }

    @Override
    public <T, Z> void set(NamespacedKey namespacedKey, PersistentDataType<T, Z> persistentDataType, Z z) {
        pdc.set(namespacedKey, persistentDataType, z);
        save();
    }

    @Override
    public <T, Z> boolean has(NamespacedKey namespacedKey, PersistentDataType<T, Z> persistentDataType) {
        return pdc.has(namespacedKey, persistentDataType);
    }

    public <T, Z> Z get(NamespacedKey namespacedKey, PersistentDataType<T, Z> persistentDataType) {
        return pdc.get(namespacedKey, persistentDataType);
    }

    public <T, Z> Z getOrDefault(NamespacedKey namespacedKey, PersistentDataType<T, Z> persistentDataType, Z z) {
        return pdc.getOrDefault(namespacedKey, persistentDataType, z);
    }

    @Override
    public Set<NamespacedKey> getKeys() {
        return pdc.getKeys();
    }

    @Override
    public void remove(NamespacedKey namespacedKey) {
        pdc.remove(namespacedKey);
        save();
    }

    @Override
    public boolean isEmpty() {
        return pdc.isEmpty();
    }

    @Override
    public PersistentDataAdapterContext getAdapterContext() {
        return pdc.getAdapterContext();
    }
}
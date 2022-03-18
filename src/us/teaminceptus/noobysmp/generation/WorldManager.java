package us.teaminceptus.noobysmp.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.generation.BlockManager.ReplaceData;
import us.teaminceptus.noobysmp.generation.TitanManager.AsyncLocation;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

public class WorldManager implements Listener {
    
    protected SMP plugin;

    public WorldManager(SMP plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public static record AsyncBlockSelection(AsyncLocation loc, SMPMaterial type) {};

    private void updateBlocks(List<AsyncBlockSelection> selection) {
        new BukkitRunnable() {
            public void run() {
                for (AsyncBlockSelection s : selection) {
                    s.type().setBlock(s.loc().toLocation());
                }
            }
        }.runTask(plugin);
    }

    private static Random r = new Random();

    @EventHandler
    public void onLoad(ChunkLoadEvent e) {
        if (!(e.isNewChunk())) return;
        Chunk c = e.getChunk();
        if (c.getWorld().getName().equalsIgnoreCase("world_titan")) return;

        ChunkSnapshot snap = c.getChunkSnapshot(true, true, true);

        new BukkitRunnable() {
            public void run() {
                List<AsyncBlockSelection> selection = new ArrayList<>();

                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < snap.getHighestBlockYAt(x, z); y++) {
                            Material m = snap.getBlockType(x, y, z);
                            if (BlockManager.REPLACEABLES.get(m) == null) continue;
                            ReplaceData data = BlockManager.REPLACEABLES.get(m);
                            
                            AsyncLocation loc = new AsyncLocation(snap.getWorldName(), x, y, z);

                            if (r.nextInt(data.intScale()) < data.chance()) {
                                selection.add(new AsyncBlockSelection(loc, data.replace()));
                            }
                        }
                    }
                }

                updateBlocks(selection);
            }
        }.runTaskAsynchronously(plugin);
    }

}

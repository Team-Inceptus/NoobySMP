package us.teaminceptus.noobysmp.generation.structures;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.generation.structures.SMPStructure.StructureData;

public class StructureManager implements Listener {
    
    protected SMP plugin;

    public StructureManager(SMP plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private static Random r = new Random();
    
    @EventHandler
    public void onLoad(ChunkLoadEvent e) {
        if (!(e.isNewChunk())) return;
        Chunk c = e.getChunk();
        
        for (SMPStructure s : SMPStructure.values()) {
            StructureData data = s.getData();
            if (!(data.world().equals(c.getWorld().getName()))) continue;
            if (!(r.nextInt(data.intScale()) < data.chance())) continue;
            World w = Bukkit.getWorld(data.world());

            int rX = r.nextInt(16);
            int rZ = r.nextInt(16);
            int y = (data.yLevel() == -65 ? w.getHighestBlockYAt(rX, rZ) : data.yLevel());

            s.getStructure().place(new Location(w, rX, y, rZ), true, StructureRotation.values()[r.nextInt(StructureRotation.values().length)], Mirror.NONE, -1, Math.min(1F, 0.5F + r.nextFloat()), r);
        }
    }

}

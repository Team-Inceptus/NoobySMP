package us.teaminceptus.noobysmp.conquest;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.util.entities.ConquestEntity;

public class ConquestManager implements Listener {
	
	protected SMP plugin;
	
	public ConquestManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onSpawn(EntitySpawnEvent e) {
		if (!(e.getEntity() instanceof LivingEntity en)) return;
		if (!(en.getWorld().getName().contains("world_conquest"))) return;
		e.setCancelled(true);
		
		switch (e.getEntityType()) {
			case SHEEP: {
				ConquestEntity<Sheep> rubySheep = ConquestEntity.spawnRubySheep(e.getLocation());
				rubySheep.getEntity().setColor(DyeColor.RED);
 				break;
			}
			default: {
				break;
			}
		}
		
	}
	
	private static Random r = new Random();
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		LivingEntity en = e.getEntity();
		if (!(en.getWorld().getName().contains("world_conquest"))) return;
		e.setDroppedExp(r.nextInt(20) + 10);
		
		
	}
}

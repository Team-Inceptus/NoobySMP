package us.teaminceptus.noobysmp.conquest;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.conquest.entities.ConquestEntity;

public class ConquestManager implements Listener {
	
	protected SMP plugin;
	
	public ConquestManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		LivingEntity en = e.getEntity();
		Location loc = e.getLocation();
		if (!(en.getWorld().getName().equalsIgnoreCase("world_conquest"))) return;
		e.setCancelled(true);
		
		if (e.getSpawnReason() != SpawnReason.NATURAL) return;

		World cW = en.getWorld();
		int entityCount = cW.getLivingEntities().stream().filter(len -> !(len instanceof Player)).toList().size();

		if (entityCount >= 20) return;

		for (Class<? extends ConquestEntity<?>> clazz : ConquestEntity.CLASS_LIST) {
			if (clazz.isAnnotationPresent(ConquestSpawnable.class)) {
				ConquestSpawnable s = clazz.getAnnotation(ConquestSpawnable.class);

				if (s.value() == e.getEntityType()) {
					try {
						clazz.getConstructor(Location.class).newInstance(loc);
					} catch (Exception err) {
						plugin.getLogger().info("Error spawning entity " + clazz.getName());
						err.printStackTrace();
					}
					break;
				}
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

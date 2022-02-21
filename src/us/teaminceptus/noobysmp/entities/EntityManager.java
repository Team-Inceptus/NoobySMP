package us.teaminceptus.noobysmp.entities;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.entities.bosses.SMPBoss;

public class EntityManager implements Listener {

	protected SMP plugin;
	
	public EntityManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onSpawn(EntitySpawnEvent e) {
		if (!(e.getEntity() instanceof LivingEntity en)) return;
		
		if (en.getCustomName() != null && SMPBoss.BOSS_NAME_LIST.contains(en.getCustomName())) return;
		
		for (Class<? extends SMPEntity<? extends LivingEntity>> clazz : SMPEntity.CLASS_LIST) {
			if (clazz.isAnnotationPresent(Spawnable.class)) {
				Spawnable a = clazz.getDeclaredAnnotationsByType(Spawnable.class)[0];
				
				if (e.getEntityType() == a.type() && SMPEntity.r.nextInt(100) < a.spawnChance()) {
					e.setCancelled(true);
					try {
						clazz.getDeclaredConstructors()[0].newInstance(e.getLocation());
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | SecurityException err) {
						err.printStackTrace();
					}
					break;
				}
			}
		}
	}

}

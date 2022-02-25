package us.teaminceptus.noobysmp.entities;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.entities.bosses.SMPBoss;
import us.teaminceptus.noobysmp.entities.titan.TitanSpawnable;

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
		
		if (!(e.getEntity().getWorld().getName().contains("titan"))) {
			for (Class<? extends SMPEntity<? extends LivingEntity>> clazz : SMPEntity.CLASS_LIST) {
				if (clazz.isAnnotationPresent(Spawnable.class)) {
					Spawnable a = clazz.getDeclaredAnnotationsByType(Spawnable.class)[0];
					
					if (e.getEntityType() == a.type() && SMPEntity.r.nextInt(100) < a.spawnChance()) {
						e.setCancelled(true);
						try {
							clazz.getDeclaredConstructors()[0].newInstance(e.getLocation());
						} catch (Exception err) {
							err.printStackTrace();
						}
						break;
					}
				}
			}
		} else {
			for (Class<? extends SMPEntity<? extends LivingEntity>> clazz : SMPEntity.TITAN_CLASS_LIST) {
				if (clazz.isAnnotationPresent(TitanSpawnable.class)) {
					TitanSpawnable a = clazz.getDeclaredAnnotationsByType(TitanSpawnable.class)[0];

					if (e.getEntityType() == a.replace()) {
						e.setCancelled(true);
						try {
							clazz.getDeclaredConstructors()[0].newInstance(e.getLocation());
						} catch (Exception err) {
							err.printStackTrace();
						}
					}
				}	
			}
		}
	}

}

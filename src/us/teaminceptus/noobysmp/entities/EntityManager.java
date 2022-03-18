package us.teaminceptus.noobysmp.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.entities.bosses.SMPBoss;
import us.teaminceptus.noobysmp.entities.titan.TitanSpawnable;
import us.teaminceptus.noobysmp.materials.AbilityItem;
import us.teaminceptus.noobysmp.materials.SMPMaterial;

public class EntityManager implements Listener {

	protected SMP plugin;
	
	public EntityManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	private static Random r = new Random();

	public static List<EntityDropData> getDrops(EntityType type) {
		List<EntityDropData> data = new ArrayList<>();
		
		data.add(new EntityDropData(SMPMaterial.LIFE_CORE, 10));

		switch (type) {
			case ENDERMAN: {
				data.add(new EntityDropData(SMPMaterial.END_CORE));
				data.add(new EntityDropData(SMPMaterial.CHARGED_END_CORE, 10));

				data.add(new EntityDropData(SMPMaterial.ENCHANTED_PEARL.getItem(r.nextInt(4) + 1), 50));
				data.add(new EntityDropData(SMPMaterial.ENCHANTED_ENDER_EYE, 10));
				break;
			}
			case ENDERMITE: {
				data.add(new EntityDropData(SMPMaterial.END_CORE));
				break;
			}
			case ENDER_DRAGON: {
				data.add(new EntityDropData(SMPMaterial.END_CORE.getItem(r.nextInt(8) + 8)));
				data.add(new EntityDropData(SMPMaterial.CHARGED_END_CORE));

				data.add(new EntityDropData(SMPMaterial.ENCHANTED_PEARL.getItem(r.nextInt(8) + 8), 50));
				data.add(new EntityDropData(SMPMaterial.ENCHANTED_ENDER_EYE.getItem(r.nextInt(3) + 1), 10));

				data.add(new EntityDropData(AbilityItem.SCROLL_DRAGONS, 20));
				break;
			}

			case DROWNED:
			case DOLPHIN:
			case COD:
			case PUFFERFISH:
			case SALMON:
			case TROPICAL_FISH: {
				data.add(new EntityDropData(SMPMaterial.AQUATIC_CORE));
				break;
			}

			case WITHER_SKELETON:
			case BLAZE:
			case ZOMBIFIED_PIGLIN:
			case PIGLIN:
			case ZOGLIN:
			case HOGLIN:
			case PIGLIN_BRUTE:
			case STRIDER:
			case GHAST: {
				data.add(new EntityDropData(SMPMaterial.NETHER_CORE));
				data.add(new EntityDropData(SMPMaterial.CHARGED_NETHER_CORE, 10));
				break;
			}

			default: {
				break;
			}
		}

		return data;
	}

	public static record EntityDropData(ItemStack drop, int intScale, int chance) {
		public EntityDropData(ItemStack drop, int chance) {
			this(drop, 100, chance);
		}

		public EntityDropData(ItemStack drop) {
			this(drop, 100);
		}

		public EntityDropData(SMPMaterial drop, int intScale, int chance) {
			this(drop.getItem(), intScale, chance);
		}

		public EntityDropData(SMPMaterial drop, int chance) {
			this(drop.getItem(), 100, chance);
		}

		public EntityDropData(SMPMaterial drop) {
			this(drop.getItem(), 100);
		}

		public EntityDropData(AbilityItem drop, int intScale, int chance) {
			this(drop.getItem(), intScale, chance);
		}

		public EntityDropData(AbilityItem drop, int chance) {
			this(drop.getItem(), 100, chance);
		}

		public EntityDropData(AbilityItem drop) {
			this(drop.getItem(), 100);
		}
	}
	
	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		LivingEntity en = e.getEntity();
		
		if (en.getCustomName() != null && SMPBoss.BOSS_NAME_LIST.contains(en.getCustomName())) return;
		
		if (!(e.getEntity().getWorld().getName().contains("titan"))) {
			for (Class<? extends SMPEntity<? extends LivingEntity>> clazz : SMPEntity.CLASS_LIST) {
				if (clazz.isAnnotationPresent(Spawnable.class)) {
					Spawnable a = clazz.getAnnotation(Spawnable.class);
					
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
			e.setCancelled(true);
			
			for (Class<? extends SMPEntity<? extends LivingEntity>> clazz : SMPEntity.TITAN_CLASS_LIST) {
				if (clazz.isAnnotationPresent(TitanSpawnable.class)) {
					TitanSpawnable a = clazz.getAnnotation(TitanSpawnable.class);
					
					if (e.getEntityType() == a.value()) {
						try {
							clazz.getConstructor(Location.class).newInstance(e.getLocation());
						} catch (Exception err) {
							err.printStackTrace();
						}
					}
				}	
			}
		}
	}

}

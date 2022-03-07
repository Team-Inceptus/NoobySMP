package us.teaminceptus.noobysmp.entities.bosses.npc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import us.teaminceptus.noobysmp.SMP;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Experience;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Defensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.MinionSpawn;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Offensive;
import us.teaminceptus.noobysmp.entities.bosses.attacks.Attacks.Repeated;

public class NPCManager implements Listener {
    
    protected SMP plugin;

    public NPCManager(SMP plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    // Boss Managers

    @EventHandler
    public void onSpawn(NPCSpawnEvent e) {
        NPC npc = e.getNPC();

        if (NPCBoss.getByUUID(npc.getUniqueId()) == null) return;

        NPCBoss boss = NPCBoss.getByUUID(npc.getUniqueId());

        for (Method m : boss.getClass().getDeclaredMethods()) {
            m.setAccessible(true);

            if (m.isAnnotationPresent(Repeated.class)) {
                Repeated r = m.getAnnotation(Repeated.class);

                new BukkitRunnable() {
                    public void run() {
                        if (npc.getEntity().isDead()) cancel();
                        if (!(npc.isSpawned())) cancel();
                        
                        try {
                            m.invoke(boss);
                        } catch (Exception e) {
                            plugin.getLogger().info("Error executing repeated ability " + m.getName() + " in class " + boss.getClass().getName());
                            e.printStackTrace();
                        }
                    }
                }.runTaskTimer(plugin, r.value(), r.value());
            }
        }
    }

    private static Random r = new Random();

    @EventHandler
    public void onDamageDefensive(NPCDamageByEntityEvent e) {
        NPC npc = e.getNPC();
        if (!(e.getDamager() instanceof Player p)) return;

        if (NPCBoss.getByUUID(npc.getUniqueId()) == null) return;

        NPCBoss boss = NPCBoss.getByUUID(npc.getUniqueId());

        for (Method m : boss.getClass().getDeclaredMethods()) {
            m.setAccessible(true);

            if (m.isAnnotationPresent(Defensive.class)) {
                Defensive d = m.getAnnotation(Defensive.class);

                if (r.nextInt(100) < d.chance()) {
                    try {
                        m.invoke(boss, p);
                    } catch (Exception err) {
                        plugin.getLogger().info("Error executing ability " + m.getName() + " in " + boss.getClass().getName());
                        err.printStackTrace();
                    }
                }
            }
        }

        try {
            Constructor<?> constructor = boss.getClass().getDeclaredConstructor(Location.class);
        
            for (Annotation a : constructor.getDeclaredAnnotations()) {
                if (a instanceof MinionSpawn spawn) {
                    if (r.nextInt(100) < spawn.chance()) {
                        npc.getEntity().getWorld().spawnEntity(npc.getEntity().getLocation(), spawn.type());
                    }
                }
            }
        } catch (Exception err) {
            plugin.getLogger().info("Error getting annotations for constructor in " + boss.getClass().getName());
            err.printStackTrace();
        }
    }

    @EventHandler
    public void onDamageOffensive(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;

        if (NPCBoss.getByUUID(e.getDamager().getUniqueId()) == null) return;

        NPCBoss boss = NPCBoss.getByUUID(e.getDamager().getUniqueId());

        for (Method m : boss.getClass().getDeclaredMethods()) {
            m.setAccessible(true);
            if (m.isAnnotationPresent(Offensive.class)) {
                Offensive o = m.getAnnotation(Offensive.class);

                if (r.nextInt(100) < o.chance()) {
                    try {
                        m.invoke(boss, p);
                    } catch (Exception err) {
                        plugin.getLogger().info("Error executing offensive ability " + m.getName() + " in class " + boss.getClass().getName());
                        err.printStackTrace();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(NPCDeathEvent e) {
        NPC npc = e.getNPC();

        if (NPCBoss.getByUUID(npc.getUniqueId()) == null) return;

        NPCBoss boss = NPCBoss.getByUUID(npc.getUniqueId());

        try {
            Constructor<?> constr = boss.getClass().getConstructor(Location.class);
            
            for (Annotation a : constr.getAnnotations()) {
                if (a instanceof Experience expr) {
                    e.setDroppedExp(expr.value());
                }
            }
        } catch (Exception err) {
            plugin.getLogger().info("Error executing death event in class" + boss.getClass().getName());
            err.printStackTrace();
        }
    }

}

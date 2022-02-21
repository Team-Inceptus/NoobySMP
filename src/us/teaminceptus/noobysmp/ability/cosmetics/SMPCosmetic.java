package us.teaminceptus.noobysmp.ability.cosmetics;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Particle;

public enum SMPCosmetic {
	
	
	GRAY_AURA(5, "Gray Aura", createCircle(Particle.SMOKE_NORMAL, 3)),
	FIRE_TRIANGLE(5, "Fire Triange", createShape(Particle.FLAME, 3, 3)),
	
	SPORES(6, "Spores", createShape(Particle.CRIMSON_SPORE, 4, 2.5)),
	
	SOUL_FIRE_TRIANGLE(7, "Soul Fire Triange", createShape(Particle.SOUL_FIRE_FLAME, 3, 3)),
	
	LAVA_SQUARE(9, "Lava Square", createShape(Particle.DRIP_LAVA, 4, 3.5)),
	
	SOULKEEPER(10, "Soulkeeper", createCircle(Particle.SOUL)),
	GREEN_AURA(10, "Green Aura", createCircle(Particle.SNEEZE, 3)),
	
	SLIME(13, "Slime", createShape(Particle.SLIME, 8, 3), createCircle(Particle.COMPOSTER, 3)),
	
	WIZARD(17, "Wizard", createCircle(Particle.PORTAL, 3)),
	
	AQUATIC(20, "Aquatic", createShape(Particle.BUBBLE_COLUMN_UP, 5, 2), createCircle(Particle.DRIP_WATER, 5.5)),
	
	GLOW(22, "Glow", createSphere(Particle.GLOW, 3)),
	
	WARRIOR(26, "Warrior", createCircle(Particle.CRIT, 3.5), createCircle(Particle.CRIT, 4)),
	
	WHITE_AURA(29, "White Aura", createCircle(Particle.FALLING_NECTAR, 5), createCircle(Particle.FALLING_NECTAR, 4)),
	
	MAGIC_WARRIOR(33, "Magic Warrior", createCircle(Particle.CRIT_MAGIC, 3.5), createCircle(Particle.CRIT_MAGIC, 4), createCircle(Particle.CRIT_MAGIC, 4.5)),
	
	MAGIC(37, "Magic", createCircle(Particle.ENCHANTMENT_TABLE, 3), createCircle(Particle.ENCHANTMENT_TABLE, 3.5), createCircle(Particle.ENCHANTMENT_TABLE, 4))
	
	
	;
	
	private final int levelUnlocked;
	private final String name;
	private final Consumer<Location>[] effects;
	
	@SafeVarargs
	private SMPCosmetic(int levelUnlocked, String name, Consumer<Location>... effects) {
		this.levelUnlocked = levelUnlocked;
		this.name = name;
		this.effects = effects;
	}
	
	private static Consumer<Location> createCircle(Particle part) {
		return createCircle(part, 1);
	}
	
	private static Consumer<Location> createCircle(Particle part, double radius) {
		return (loc -> {
			int points = 10;
			for (int i = 0; i < points; i++) {
				double angle = 2 * Math.PI * i / points;
				loc.add(radius * Math.sin(angle), 0, radius * Math.cos(angle));
				loc.getWorld().spawnParticle(part, loc, 5);
				loc.subtract(radius * Math.sin(angle), 0, radius * Math.cos(angle));
				
			}
		});
	}
	
	private static Consumer<Location> createShape(Particle part, int points, double radius) {
		return (loc -> {
			for (int i = 0; i < points; i++) {
				double angle = Math.toRadians(360.0 / points * i);
				double nextAngle = Math.toRadians(360.0 / points * i);
				
				double x = Math.cos(angle) * radius;
				double z = Math.sin(angle) * radius;
				double x2 = Math.cos(nextAngle) * radius;
				double z2 = Math.sin(nextAngle) * radius;
				double deltaX = x2 - x;
				double deltaZ = z2 - 2;
				double distance = Math.sqrt((deltaX - x) * (deltaX - x) + (deltaZ - z) * (deltaZ - z));
				
				for (double d = 0; d < distance - 0.1; d += 0.1) {
					loc.add(x + deltaX * d, 0, x + deltaZ * d);
					loc.getWorld().spawnParticle(part, loc, 5, 0, 0, 0, 0);
					loc.subtract(x + deltaX * d, 0, z + deltaZ * d);
				}
			}
		});
	}
	
	private static Consumer<Location> createSphere(Particle part, double radius) {
		return (loc -> {
			for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
				double sradius = Math.sin(i) * radius;
				double y = Math.cos(i);
				for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
					double x = Math.cos(a) * sradius;
					double z = Math.sin(a) * sradius;
					loc.add(x, y, z);
					loc.getWorld().spawnParticle(part, loc, 5);
					loc.subtract(x, y, z);
				}
			}
		});
	}
	
	public int getLevelUnlocked() {
		return this.levelUnlocked;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Consumer<Location>[] getEffects() {
		return this.effects;
	}
	
	public void createEffect(Location loc) {
		for (Consumer<Location> c : this.effects) c.accept(loc);
	}
}

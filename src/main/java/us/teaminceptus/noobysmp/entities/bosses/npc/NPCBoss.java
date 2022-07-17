package us.teaminceptus.noobysmp.entities.bosses.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.mcmonkey.sentinel.SentinelTrait;

import com.google.common.collect.ImmutableList;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import us.teaminceptus.noobysmp.entities.bosses.BossSetup.Icon;
import us.teaminceptus.noobysmp.entities.bosses.SMPBoss;

public abstract class NPCBoss {
    
    protected final NPC npc;

    private final Map<ItemStack, Integer> drops;
    private final Location loc;

    private final UUID uuid;

    private final SentinelTrait sentinel;
    private final SkinTrait skin;

    protected static final Random r = new Random();

    public static final List<Class<? extends NPCBoss>> NPC_BOSS_LIST = ImmutableList.<Class<? extends NPCBoss>>builder()
    .add(Netherman.class)
    .add(DrownedCreeper.class)
    .add(ShulkerWizard.class)
    .build();

    public NPCBoss(String name, Location loc, double maxHealth, Map<ItemStack, Integer> drops, NPCSkin npcskin) {
        this.drops = drops;
        this.loc = loc;
        
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);

        this.uuid = npc.getUniqueId();

        SentinelTrait trait = npc.getOrAddTrait(SentinelTrait.class);
        this.sentinel = trait;

        trait.setHealth(maxHealth);
        trait.armor = SMPBoss.attributes(maxHealth).get(Attribute.GENERIC_ARMOR);
        trait.attackRate = 1;
        trait.reach = 2.5;
        trait.speed = 0.12;
        trait.respawnMe = null;
        trait.respawnTime = -1;
        trait.addTarget("players");

        SkinTrait skin = npc.getOrAddTrait(SkinTrait.class);
        this.skin = skin;

        skin.setSkinName(name.toLowerCase().replace(' ', '_'));
        skin.setTexture(npcskin.getValue(), npcskin.getSignature());

        this.npc = npc;
    }
    
    public static Class<? extends NPCBoss> getByIcon(Material icon) {
    	for (Class<? extends NPCBoss> npcClass : NPC_BOSS_LIST) {
    		if (!(npcClass.isAnnotationPresent(Icon.class))) continue;
    		if (npcClass.getAnnotation(Icon.class).value() == icon) return npcClass;
    	}
    	
    	return null;
    }

    /**
     * Identical to {@link NPC#getUniqueId()}
     * @return UUID of this NPCBoss
     */
    public UUID getUniqueID() {
        return this.uuid;
    }

    public enum NPCSkin {

        // TODO
        DROWNED_CREEPER("", ""),
        SHULKER_WIZARD("", ""),
        BLAZE_SKELETON("", ""),
        NETHERMAN("", ""),
        CLAYS("", "")
        ;

        private final String value;
        private final String signature;

        NPCSkin(String value, String signature) {
            this.value = value;
            this.signature = signature;            
        }

        public final String getValue() {
            return this.value;
        }

        public final String getSignature() {
            return this.signature;
        }



    }

    public SentinelTrait getSentinel() {
        return this.sentinel;
    }

    public SkinTrait getSkinTrait() {
        return this.skin;
    }

    public NPC getNPC() {
        return this.npc;
    }

    public Location getSpawnLocation() {
        return this.loc;
    }

    public Map<ItemStack, Integer> getDrops() {
        return this.drops;
    }

    public static final List<NPCBoss> SPAWNED_NPCS = new ArrayList<>();

    public void spawn() {
        npc.spawn(loc);
        SPAWNED_NPCS.add(this);
    }

    public static NPCBoss getByUUID(UUID uuid) {
        for (NPCBoss b : SPAWNED_NPCS) {
            if (b.uuid.equals(uuid)) return b;
        }

        return null;
    }

}

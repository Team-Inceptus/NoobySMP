package us.teaminceptus.noobysmp.util.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * This event is fired every tick, for use in {@link SMPTag}
 *
 */
public class PlayerTickEvent extends PlayerEvent {
	
	public static HandlerList HANDLERS = new HandlerList();
	
	public PlayerTickEvent(Player p) {
		super(p);
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

}

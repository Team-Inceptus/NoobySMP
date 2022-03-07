package us.teaminceptus.noobysmp.materials;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;

import us.teaminceptus.noobysmp.SMP;

public class TagsManager implements Listener {
	
	protected SMP plugin;
	
	public TagsManager(SMP plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
        RegisteredListener registeredListener = new RegisteredListener(this, (listener, event) -> onEvent(event), EventPriority.NORMAL, plugin, false);
        for (HandlerList handler : HandlerList.getHandlerLists())
            handler.register(registeredListener);
	}
	
	
	public void onEvent(Event e) {
		for (SMPTag<?> tag : SMPTag.getByType(e.getClass())) {
			tag.run(e);
		}
	}

}

package us.teaminceptus.noobysmp.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public interface Queryable {
	
	public static final List<Queryable> ALL = new ArrayList<>();
	
	ItemStack generateInformation();
	
	public static void register(Queryable q) {
		ALL.add(q);
		
	}
	
	public static @interface QueryDescription {
		
		String[] value();
		
	}
	
}

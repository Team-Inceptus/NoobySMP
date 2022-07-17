package us.teaminceptus.noobysmp.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import us.teaminceptus.noobysmp.util.inventoryholder.CancelHolder;

public interface Queryable extends ItemHolder, Unlockable {
	
	List<Queryable> ALL = new ArrayList<>();

	QueryID queryId();

	record QueryID(String type, String value) {}

	default ItemStack genInfo() {
		try {
			final Field found;

			if (this instanceof Enum<?> e) found = this.getClass().getDeclaredField(e.name());
			else found = null;

			if (found == null) return null;

			char[] charArr = found.getName().toCharArray();
			boolean foundSpace = true;

			for (int i = 0; i < charArr.length; i++) {
				if (Character.isLetter(charArr[i])) {
					if (foundSpace) {
						charArr[i] = Character.toUpperCase(charArr[i]);
						foundSpace = false;
					}
				} else {
					foundSpace = true;
				}
			}

			String name = String.valueOf(charArr);

			ItemStack item = new ItemStack(this.getItem().getType());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(!(this.getItem().getItemMeta().hasDisplayName()) ? ChatColor.AQUA + name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase() : this.getItem().getItemMeta().getDisplayName());
			
			List<String> info = new ArrayList<>();
			
			final String id;
			
			if (this instanceof Enum<?> e) id = e.name();
			else id = (this.getItem().getItemMeta().hasLocalizedName() ? this.getItem().getItemMeta().getLocalizedName() : this.getItem().getType().name());
			
			info.add(ChatColor.DARK_GRAY + "ID: " + id);
			
			if (this.getLevelUnlocked() > 0) {
				info.add(ChatColor.DARK_GRAY + "Level Unlocked: " + this.getLevelUnlocked());
			}

			if (found.isAnnotationPresent(QueryDescription.class)) {
				info.add(" ");
				QueryDescription desc = found.getAnnotation(QueryDescription.class);
				for (String s : desc.value()) {
					info.add(ChatColor.GRAY + s);
				}
			}
			
			meta.setLore(info);
			item.setItemMeta(meta);
			return item;
		} catch (Exception e) {
			return null;
		}
	}

	default Inventory getQueryInventory() {
		Inventory inv = Generator.genGUI(27, "Query Information", new CancelHolder());

		for (int i = 10; i < 17; i++) inv.setItem(i, Items.Inventory.GUI_PANE);
		
		inv.setItem(13, genInfo());

		return inv;
	}
	
	static void register(Queryable q) {
		ALL.add(q);	
	}

	static Queryable getById(QueryID id) {
		for (Queryable q : ALL) if (q.queryId().type.equals(id.type) && q.queryId().value.equals(id.value)) return q;
		return null;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
    @interface QueryDescription {
		
		String[] value();
		
	}
	
}

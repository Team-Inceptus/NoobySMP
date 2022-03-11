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
	
	public static final List<Queryable> ALL = new ArrayList<>(); 

	QueryID queryId();

	public record QueryID(String type, String value) {}

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

			ItemHolder holder = (ItemHolder) this;

			ItemStack item = new ItemStack(holder.getItem().getType());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(!(holder.getItem().getItemMeta().hasDisplayName()) ? ChatColor.AQUA + name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase() : holder.getItem().getItemMeta().getDisplayName());
			
			List<String> info = new ArrayList<>();
			info.add(ChatColor.DARK_GRAY + "ID: " + (holder.getItem().getItemMeta().hasLocalizedName() ? holder.getItem().getItemMeta().getLocalizedName().toUpperCase() : holder.getItem().getType().name()));
			
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

			item.setItemMeta(meta);
			return item;
		} catch (Exception e) {
			return null;
		}
	}

	default Inventory getQueryInventory() {
		Inventory inv = Generator.genGUI(27, "Query Info - " + this.queryId().value.toUpperCase(), new CancelHolder());

		for (int i = 10; i < 16; i++) inv.setItem(i, Items.Inventory.GUI_PANE);
		
		inv.setItem(4, genInfo());

		return inv;
	}
	
	public static void register(Queryable q) {
		ALL.add(q);	
	}

	public static Queryable getById(QueryID id) {
		for (Queryable q : ALL) if (q.queryId().type.equals(id.type) && q.queryId().value.equals(id.value)) return q;
		return null;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface QueryDescription {
		
		String[] value();
		
	}
	
}

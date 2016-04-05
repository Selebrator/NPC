package de.selebrator.fetcher;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherArmorBuilder extends ItemBuilder {

	protected LeatherArmorMeta meta;

	private Color color;

	public LeatherArmorBuilder(Material type, int amount, short damage, String name, Color color) {
		super(type, amount, damage, name);
		this.color = color;
	}

	@Override
	public ItemStack build() {
		if(type != null) {
			item = new ItemStack(type, amount, damage);
			meta = (LeatherArmorMeta) item.getItemMeta();
			meta.setDisplayName(name);
			meta.setColor(color);
			item.setItemMeta(meta);
			return item;
		}
		return null;
	}
}

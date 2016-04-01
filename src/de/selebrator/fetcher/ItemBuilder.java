package de.selebrator.fetcher;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {

	protected ItemStack item;
	protected ItemMeta meta;

	protected Material type;
	protected int amount;
	protected short damage;
	protected String name;

	public ItemBuilder(Material type, int amount, short damage, String name) {
		this.type = type;
		this.amount = amount;
		this.damage = damage;
		this.name = name;
	}

	public ItemStack build() {
		if(type != null) {
			item = new ItemStack(type, amount, damage);
			meta = item.getItemMeta();
			meta.setDisplayName(name);
			item.setItemMeta(meta);
			return item;
		}
		return null;
	}
}

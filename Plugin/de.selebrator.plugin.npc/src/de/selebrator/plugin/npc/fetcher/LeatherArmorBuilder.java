package de.selebrator.plugin.npc.fetcher;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherArmorBuilder extends ArmorBuilder {

	protected LeatherArmorMeta meta;

	private Color color;

	public LeatherArmorBuilder(EquipmentSlot slot, int amount, short damage, String name, Color color) {
		super(slot, Material.LEATHER, amount, damage, name);
		this.color = color;
	}

	public LeatherArmorBuilder(EquipmentSlot slot, String name, Color color) {
		this(slot, 1, (short) 0, name, color);
	}

	public LeatherArmorBuilder(EquipmentSlot slot, Color color) {
		this(slot, null, color);
	}

	@Override
	public ItemStack build() {
		if(type != null) {
			item = new ItemStack(type, amount, damage);
			meta = (LeatherArmorMeta) item.getItemMeta();
			if(name != null) { meta.setDisplayName(name); }
			meta.setColor(color);
			item.setItemMeta(meta);
			return item;
		}
		return null;
	}
}

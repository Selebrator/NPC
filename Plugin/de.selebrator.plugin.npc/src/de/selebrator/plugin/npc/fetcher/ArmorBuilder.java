package de.selebrator.plugin.npc.fetcher;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;

import java.util.*;

public class ArmorBuilder extends ItemBuilder {

	private static Map<EquipmentSlot, String> slots = new HashMap<>();
	private static Map<Material, String> materials = new HashMap<>();

	static {
		slots.put(EquipmentSlot.HEAD, "HELMET");
		slots.put(EquipmentSlot.CHEST, "CHESTPLATE");
		slots.put(EquipmentSlot.LEGS, "LEGGINGS");
		slots.put(EquipmentSlot.FEET, "BOOTS");

		materials.put(Material.LEATHER, "LEATHER");
		materials.put(Material.GOLD_INGOT, "GOLD");
		materials.put(Material.FIRE, "CHAINMAIL");
		materials.put(Material.IRON_INGOT, "IRON");
		materials.put(Material.DIAMOND, "DIAMOND");
	}

	public ArmorBuilder(EquipmentSlot slot, Material type, int amount, short damage, String name) {
		super(Material.valueOf(materials.get(type) + "_" + slots.get(slot)), amount, damage, name);
	}

	public ArmorBuilder(EquipmentSlot slot, Material type, String name) {
		this(slot, type, 1, (short) 0, name);
	}

	public ArmorBuilder(EquipmentSlot slot, Material type) {
		this(slot, type, null);
	}
}

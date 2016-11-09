package de.selebrator.npc.inventory;

import de.selebrator.npc.FakePlayer;
import de.selebrator.npc.NPC;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class FakeEquipment {
	private FakePlayer holder;
	private ItemStack mainHand;
	private ItemStack offHand;
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;

	public FakeEquipment(FakePlayer holder) {
		this.holder = holder;
	}

	// ##### MAIN HAND #####
	public ItemStack getMainHand() {
		return this.mainHand;
	}

	public void setMainHand(ItemStack mainHand) {
		this.set(EquipmentSlot.HAND, mainHand);
	}

	// ##### OFF HAND #####
	public ItemStack getOffHand() {
		return this.offHand;
	}

	public void setOffHand(ItemStack offHand) {
		this.set(EquipmentSlot.OFF_HAND, offHand);
	}

	// ##### HELMET #####
	public ItemStack getHelmet() {
		return this.helmet;
	}

	public void setHelmet(ItemStack helmet) {
		this.set(EquipmentSlot.HEAD, helmet);
	}

	// ##### CHESTPLATE #####
	public ItemStack getChestplate() {
		return this.chestplate;
	}

	public void setChestplate(ItemStack chestplate) {
		this.set(EquipmentSlot.CHEST, chestplate);
	}

	// ##### LEGGINGS #####
	public ItemStack getLeggings() {
		return this.leggings;
	}

	public void setLeggings(ItemStack leggings) {
		this.set(EquipmentSlot.LEGS, leggings);
	}

	// ##### BOOTS #####
	public ItemStack getBoots() {
		return this.boots;
	}

	public void setBoots(ItemStack boots) {
		this.set(EquipmentSlot.FEET, boots);
	}

	// ##### ARMOR #####
	public ItemStack[] getArmor() {
		return new ItemStack[] { this.helmet, this.chestplate, this.leggings, this.boots };
	}

	public void setArmor(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
		setArmor(new ItemStack[] { helmet, chestplate, leggings, boots });
	}

	public void setArmor(ItemStack[] armor) {
		this.setHelmet(armor[0]);
		this.setChestplate(armor[1]);
		this.setLeggings(armor[2]);
		this.setBoots(armor[3]);
	}

	// ##### GENERAL #####
	public ItemStack get(EquipmentSlot slot) {
		switch(slot) {
			case HAND:
				return this.mainHand;
			case OFF_HAND:
				return this.offHand;
			case FEET:
				return this.boots;
			case LEGS:
				return this.leggings;
			case CHEST:
				return this.chestplate;
			case HEAD:
				return this.helmet;
		}
		throw new IllegalArgumentException();
	}

	public void set(EquipmentSlot slot, ItemStack item) {
		switch(slot) {
			case HAND:
				this.mainHand = item;
				break;
			case OFF_HAND:
				this.offHand = item;
				break;
			case FEET:
				this.boots = item;
				break;
			case LEGS:
				this.leggings = item;
				break;
			case CHEST:
				this.chestplate = item;
				break;
			case HEAD:
				this.helmet = item;
				break;
		}
		holder.equip(slot, item);
	}

	public void clear(EquipmentSlot slot) {
		set(slot, null);
	}

	public void clear() {
		for(EquipmentSlot slot : EquipmentSlot.values()) {
			this.set(slot, null);
		}
	}

	public NPC getHolder() {
		return this.holder;
	}
}

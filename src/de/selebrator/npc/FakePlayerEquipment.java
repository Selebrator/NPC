package de.selebrator.npc;

import org.bukkit.inventory.ItemStack;

public class FakePlayerEquipment {
	private FakePlayer holder;
	private ItemStack mainHand;
	private ItemStack offHand;
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;

	public FakePlayerEquipment(FakePlayer holder) {
		this.holder = holder;
	}

	// ##### MAIN HAND #####
	public ItemStack getMainHand() {
		return this.mainHand;
	}

	public void setMainHand(ItemStack mainHand) {
		this.set(EnumEquipmentSlot.MAIN_HAND, mainHand);
	}

	// ##### OFF HAND #####
	public ItemStack getOffHand() {
		return this.offHand;
	}

	public void setOffHand(ItemStack offHand) {
		this.set(EnumEquipmentSlot.OFF_HAND, offHand);
	}

	// ##### HELMET #####
	public ItemStack getHelmet() {
		return this.helmet;
	}

	public void setHelmet(ItemStack helmet) {
		this.set(EnumEquipmentSlot.HELMET, helmet);
	}

	// ##### CHESTPLATE #####
	public ItemStack getChestplate() {
		return this.chestplate;
	}

	public void setChestplate(ItemStack chestplate) {
		this.set(EnumEquipmentSlot.CHESTPLATE, chestplate);
	}

	// ##### LEGGINGS #####
	public ItemStack getLeggings() {
		return this.leggings;
	}

	public void setLeggings(ItemStack leggings) {
		this.set(EnumEquipmentSlot.LEGGINGS, leggings);
	}

	// ##### BOOTS #####
	public ItemStack getBoots() {
		return this.boots;
	}

	public void setBoots(ItemStack boots) {
		this.set(EnumEquipmentSlot.BOOTS, boots);
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
	public ItemStack get(EnumEquipmentSlot slot) {
		switch(slot) {
			case MAIN_HAND:
				return this.mainHand;
			case OFF_HAND:
				return this.offHand;
			case BOOTS:
				return this.boots;
			case LEGGINGS:
				return this.leggings;
			case CHESTPLATE:
				return this.chestplate;
			case HELMET:
				return this.helmet;
		}
		throw new IllegalArgumentException();
	}

	public void set(EnumEquipmentSlot slot, ItemStack item) {
		switch(slot) {
			case MAIN_HAND:
				this.mainHand = item;
				break;
			case OFF_HAND:
				this.offHand = item;
				break;
			case BOOTS:
				this.boots = item;
				break;
			case LEGGINGS:
				this.leggings = item;
				break;
			case CHESTPLATE:
				this.chestplate = item;
				break;
			case HELMET:
				this.helmet = item;
				break;
		}
		holder.equip(slot, item);
	}

	public NPC getHolder() {
		return this.holder;
	}
}

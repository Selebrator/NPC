package de.selebrator.npc.entity.metadata;

import org.bukkit.Material;

public interface EndermanMetadata extends MonsterMetadata {

	default Material getCarriedMaterial() {
		return Material.getMaterial(this.getCarriedId());
	}

	default int getCarriedId() {
		return this.getCarriedCombinedId() & 0b111111111111;
	}

	default int getCarriedData() {
		return this.getCarriedCombinedId() >> 12 & 0b1111;
	}

	int getCarriedCombinedId();

	default void setCarriedMaterial(Material carry) {
		this.setCarriedMaterial(carry == null ? 0 : carry.getId());
	}

	default void setCarriedMaterial(int id) {
		this.setCarriedMaterial(id, 0);
	}

	void setCarriedMaterial(int id, int data);

	boolean isScreaming();

	void setScreaming(boolean screaming);
}

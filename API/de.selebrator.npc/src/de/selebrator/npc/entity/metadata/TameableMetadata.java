package de.selebrator.npc.entity.metadata;

import static de.selebrator.npc.entity.metadata.MetadataObject.TameableAnimalFlag.*;

public interface TameableMetadata extends AnimalMetadata, Ownable {

	byte getTameableAnimalInfo();

	default boolean getTameableAnimalInfo(int target) {
		return MetadataObject.getBitmaskValue(this.getStatus(), target);
	}

	void setTameableAnimalInfo(byte value);

	default void setTameableAnimalInfo(int target, boolean state) {
		this.setStatus(MetadataObject.setBitmaskValue(this.getStatus(), target, state));
	}

	default boolean isSitting() {
		return this.getTameableAnimalInfo(SITTING);
	}

	default void setSitting(boolean state) {
		this.setTameableAnimalInfo(SITTING, state);
	}

	default boolean isAngry() {
		return this.getTameableAnimalInfo(ANGRY);
	}

	default void setAngry(boolean state) {
		this.setTameableAnimalInfo(ANGRY, state);
	}

	default boolean isTamed() {
		return this.getTameableAnimalInfo(TAMED);
	}

	default void setTamed(boolean state) {
		this.setTameableAnimalInfo(TAMED, state);
	}
}

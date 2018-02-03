package de.selebrator.npc.entity.metadata;

import static de.selebrator.npc.entity.metadata.MetadataObject.AbstractHorseFlag.*;

public interface AbstractHorseMetadata extends AnimalMetadata, Ownable {

	byte getAbstractHorseInfo();

	default boolean getAbstractHorseInfo(int target) {
		return MetadataObject.getBitmaskValue(this.getAbstractHorseInfo(), target);
	}

	void setAbstractHorseInfo(byte value);

	default void setAbstractHorseInfo(int target, boolean state) {
		this.setAbstractHorseInfo(MetadataObject.setBitmaskValue(this.getAbstractHorseInfo(), target, state));
	}

	default boolean isTamed() {
		return this.getAbstractHorseInfo(TAMED);
	}

	default void setTamed(boolean state) {
		this.setAbstractHorseInfo(TAMED, state);
	}

	default boolean isSaddled() {
		return this.getAbstractHorseInfo(SADDLED);
	}

	default void setSaddled(boolean state) {
		this.setAbstractHorseInfo(SADDLED, state);
	}

	default boolean isBred() {
		return this.getAbstractHorseInfo(BRED);
	}

	default void setBred(boolean state) {
		this.setAbstractHorseInfo(BRED, state);
	}

	default boolean isEating() {
		return this.getAbstractHorseInfo(EATING);
	}

	default void setEating(boolean state) {
		this.setAbstractHorseInfo(EATING, state);
	}

	default boolean isRearing() {
		return this.getAbstractHorseInfo(REARING);
	}

	default void setRearing(boolean state) {
		this.setAbstractHorseInfo(REARING, state);
	}

	default boolean isMouthOpen() {
		return this.getAbstractHorseInfo(MOUTH_OPEN);
	}

	default void setMouthOpen(boolean state) {
		this.setAbstractHorseInfo(MOUTH_OPEN, state);
	}
}

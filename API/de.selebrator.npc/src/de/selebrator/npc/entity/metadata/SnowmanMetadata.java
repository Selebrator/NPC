package de.selebrator.npc.entity.metadata;

import static de.selebrator.npc.entity.metadata.MetadataObject.SnowmanFlag.NO_PUMPKIN_HEAD;

public interface SnowmanMetadata extends GolemMetadata {

	byte getSnowmanInfo();

	default boolean getSnowmanInfo(int target) {
		return MetadataObject.getBitmaskValue(this.getSnowmanInfo(), target);
	}

	void setSnowmanInfo(byte value);

	default void setSnowmanInfo(int target, boolean state) {
		this.setSnowmanInfo(MetadataObject.setBitmaskValue(this.getSnowmanInfo(), target, state));
	}

	default boolean hasPumpkinHat() {
		return this.getSnowmanInfo(NO_PUMPKIN_HEAD);
	}

	default void setPumpkinHat(boolean hat) {
		this.setSnowmanInfo(NO_PUMPKIN_HEAD, hat);
	}
}

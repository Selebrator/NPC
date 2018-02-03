package de.selebrator.npc.entity.metadata;

import static de.selebrator.npc.entity.metadata.MetadataObject.BatFlag.HANGING;

public interface BatMetadata extends AmbientMetadata {

	byte getBatInfo();

	default boolean getBatInfo(int target) {
		return MetadataObject.getBitmaskValue(this.getBatInfo(), target);
	}

	void setBatInfo(byte value);

	default void setBatInfo(int target, boolean state) {
		this.setBatInfo(MetadataObject.setBitmaskValue(this.getBatInfo(), target, state));
	}

	default boolean isHanging() {
		return this.getBatInfo(HANGING);
	}

	default void setHanging(boolean hanging) {
		this.setBatInfo(HANGING, hanging);
	}
}

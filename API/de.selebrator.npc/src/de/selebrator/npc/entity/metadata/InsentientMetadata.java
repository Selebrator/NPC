package de.selebrator.npc.entity.metadata;

import static de.selebrator.npc.entity.metadata.MetadataObject.InsentientFlag.LEFT_HANDED;
import static de.selebrator.npc.entity.metadata.MetadataObject.InsentientFlag.NO_AI;

public interface InsentientMetadata extends LivingMetadata {

	byte getInsentientInfo();

	default boolean getInsentientInfo(int target) {
		return MetadataObject.getBitmaskValue(this.getInsentientInfo(), target);
	}

	void setInsentientInfo(byte value);

	default void setInsentientInfo(int target, boolean state) {
		this.setInsentientInfo(MetadataObject.setBitmaskValue(this.getInsentientInfo(), target, state));
	}

	default boolean hasAI() {
		return this.getInsentientInfo(NO_AI);
	}

	default void setAI(boolean ai) {
		this.setInsentientInfo(NO_AI, !ai);
	}

	default boolean isLeftHanded() {
		return this.getInsentientInfo(LEFT_HANDED);
	}

	default void setLeftHanded(boolean leftHanded) {
		this.setInsentientInfo(LEFT_HANDED, leftHanded);
	}
}

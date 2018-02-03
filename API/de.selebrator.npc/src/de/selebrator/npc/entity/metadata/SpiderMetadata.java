package de.selebrator.npc.entity.metadata;

import static de.selebrator.npc.entity.metadata.MetadataObject.SpiderFlag.CLIMBING;

public interface SpiderMetadata extends MonsterMetadata {

	byte getSpiderInfo();

	default boolean getSpiderInfo(int target) {
		return MetadataObject.getBitmaskValue(this.getSpiderInfo(), target);
	}

	void setSpiderInfo(byte value);

	default void setSpiderInfo(int target, boolean state) {
		this.setSpiderInfo(MetadataObject.setBitmaskValue(this.getSpiderInfo(), target, state));
	}

	default boolean isClimbing() {
		return this.getSpiderInfo(CLIMBING);
	}

	default void setClimbing(boolean climbing) {
		this.setSpiderInfo(CLIMBING, climbing);
	}
}

package de.selebrator.npc.entity.metadata;

import static de.selebrator.npc.entity.metadata.MetadataObject.VexFlag.ATTACKING;

public interface VexMetadata extends MonsterMetadata {

	byte getVexInfo();

	default boolean getVexInfo(int target) {
		return MetadataObject.getBitmaskValue(this.getVexInfo(), target);
	}

	void setVexInfo(byte value);

	default void setVexInfo(int target, boolean state) {
		this.setVexInfo(MetadataObject.setBitmaskValue(this.getVexInfo(), target, state));
	}

	default boolean isAttacking() {
		return this.getVexInfo(ATTACKING);
	}

	default void setAttacking(boolean attacking) {
		this.setVexInfo(ATTACKING, attacking);
	}
}

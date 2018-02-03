package de.selebrator.npc.entity.metadata;

import static de.selebrator.npc.entity.metadata.MetadataObject.BlazeFlag.ON_FIRE;

public interface BlazeMetadata extends MonsterMetadata {

	byte getBlazeInfo();

	default boolean getBlazeInfo(int target) {
		return MetadataObject.getBitmaskValue(this.getBlazeInfo(), target);
	}

	void setBlazeInfo(byte value);

	default void setBlazeInfo(int target, boolean state) {
		this.setBlazeInfo(MetadataObject.setBitmaskValue(this.getBlazeInfo(), target, state));
	}

	default boolean isOnFire() {
		return this.getBlazeInfo(ON_FIRE);
	}

	default void setOnFire(boolean onFire) {
		this.setBlazeInfo(ON_FIRE, onFire);
	}
}

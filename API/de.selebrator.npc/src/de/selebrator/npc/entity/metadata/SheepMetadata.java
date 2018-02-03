package de.selebrator.npc.entity.metadata;

import static de.selebrator.npc.entity.metadata.MetadataObject.WoolFlag.COLOR;
import static de.selebrator.npc.entity.metadata.MetadataObject.WoolFlag.SHEARED;

public interface SheepMetadata extends AnimalMetadata {

	byte getWoolInfo();

	default boolean getWoolInfo(int target) {
		return MetadataObject.getBitmaskValue(this.getStatus(), target);
	}

	void setWoolInfo(byte value);

	default void setWoolInfo(int target, boolean state) {
		this.setWoolInfo(MetadataObject.setBitmaskValue(this.getStatus(), target, state));
	}

	//TODO handle color with bukkit api
	default int getColorId() {
		return this.getStatus() & COLOR;
	}

	default void setColor(int id) {
		this.setWoolInfo((byte) (this.getStatus() | (id & COLOR)));
	}

	default boolean isSheared() {
		return this.getWoolInfo(SHEARED);
	}

	default void setSheared(boolean sheared) {
		this.setWoolInfo(SHEARED, sheared);
	}
}

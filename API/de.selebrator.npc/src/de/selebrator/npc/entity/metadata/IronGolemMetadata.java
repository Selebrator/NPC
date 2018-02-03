package de.selebrator.npc.entity.metadata;

import static de.selebrator.npc.entity.metadata.MetadataObject.IronGolemFlag.PLAYER_CREATED;

public interface IronGolemMetadata extends GolemMetadata {

	byte getIronGolemInfo();

	default boolean getIronGolemInfo(int target) {
		return MetadataObject.getBitmaskValue(this.getIronGolemInfo(), target);
	}

	void setIronGolemInfo(byte value);

	default void setIronGolemInfo(int target, boolean state) {
		this.setIronGolemInfo(MetadataObject.setBitmaskValue(this.getStatus(), target, state));
	}

	default boolean isPlayerCreated() {
		return this.getIronGolemInfo(PLAYER_CREATED);
	}

	default void setPlayerCreated(boolean playerCreated) {
		this.setIronGolemInfo(PLAYER_CREATED, playerCreated);
	}
}

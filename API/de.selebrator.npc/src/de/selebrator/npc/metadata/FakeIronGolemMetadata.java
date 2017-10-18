package de.selebrator.npc.metadata;

import de.selebrator.npc.entity.FakeEntity;

public class FakeIronGolemMetadata extends FakeGolemMetadata {
	private MetadataObject<Byte> playerCreated = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityIronGolem", "a", 0); //12

	public FakeIronGolemMetadata() {
		super();
	}

	public boolean isPlayerCreated() {
		return FakeEntity.getBitmaskValue(this.playerCreated, (byte) 4);
	}

	public void setPlayerCreated(boolean playerCreated) {
		FakeEntity.setBitmaskValue(this.playerCreated, (byte) 4, playerCreated);
	}
}

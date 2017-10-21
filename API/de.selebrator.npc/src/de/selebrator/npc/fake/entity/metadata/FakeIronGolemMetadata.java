package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;

public class FakeIronGolemMetadata extends FakeGolemMetadata {
	private MetadataObject<Byte> playerCreated = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityIronGolem", "a", 0); //12

	public FakeIronGolemMetadata() {
		super();
	}

	public boolean isPlayerCreated() {
		return MetadataObject.getBitmaskValue(this.playerCreated, (byte) 4);
	}

	public void setPlayerCreated(boolean playerCreated) {
		MetadataObject.setBitmaskValue(this.playerCreated, (byte) 4, playerCreated);
	}
}

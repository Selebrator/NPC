package de.selebrator.npc.metadata;

import de.selebrator.npc.entity.FakeEntity;

public class FakeSnowmanMetadata extends FakeGolemMetadata {
	private MetadataObject<Byte> pumpkinHat = new MetadataObject<>(this.getDataWatcher(), (byte) 16, "EntitySnowman", "a", 0); //12

	public FakeSnowmanMetadata() {
		super();
	}

	public boolean hasPumpkinHat() {
		return FakeEntity.getBitmaskValue(this.pumpkinHat, (byte) 4);
	}

	public void setPumpkinHat(boolean hat) {
		FakeEntity.setBitmaskValue(this.pumpkinHat, (byte) 4, hat);
	}
}

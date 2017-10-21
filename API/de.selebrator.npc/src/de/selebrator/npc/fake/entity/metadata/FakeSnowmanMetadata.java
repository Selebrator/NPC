package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;

public class FakeSnowmanMetadata extends FakeGolemMetadata {
	private MetadataObject<Byte> pumpkinHat = new MetadataObject<>(this.getDataWatcher(), (byte) 16, "EntitySnowman", "a", 0); //12

	public FakeSnowmanMetadata() {
		super();
	}

	public boolean hasPumpkinHat() {
		return MetadataObject.getBitmaskValue(this.pumpkinHat, (byte) 4);
	}

	public void setPumpkinHat(boolean hat) {
		MetadataObject.setBitmaskValue(this.pumpkinHat, (byte) 4, hat);
	}
}

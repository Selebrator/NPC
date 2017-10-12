package de.selebrator.npc.metadata;

public class FakeSnowmanMetadata extends FakeGolemMetadata {
	private MetadataObject<Byte> pumpkinHat = new MetadataObject<>(this.getDataWatcher(), (byte) 16, "EntitySnowman", "a", 0); //12

	public FakeSnowmanMetadata() {
		super();
	}

	public boolean hasPumpkinHat() {
		return FakeMetadata.getBitmaskValue(this.pumpkinHat, (byte) 4);
	}

	public void setPumpkinHat(boolean hat) {
		FakeMetadata.setBitmaskValue(this.pumpkinHat, (byte) 4, hat);
	}
}

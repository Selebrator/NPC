package de.selebrator.npc.metadata;

public class FakeBatMetadata extends FakeAmbientMetadata {
	private MetadataObject<Byte> hanging = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityBat", "a", 0); //12

	public FakeBatMetadata() {
		super();
	}

	public boolean isHanging() {
		return FakeMetadata.getBitmaskValue(this.hanging, (byte) 0);
	}

	public void setHanging(boolean hanging) {
		FakeMetadata.setBitmaskValue(this.hanging, (byte) 0, hanging);
	}
}

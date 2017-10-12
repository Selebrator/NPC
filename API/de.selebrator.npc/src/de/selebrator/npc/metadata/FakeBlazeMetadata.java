package de.selebrator.npc.metadata;

public class FakeBlazeMetadata extends FakeAmbientMetadata {
	private MetadataObject<Byte> onFire = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityBlaze", "c", 0); //12

	public FakeBlazeMetadata() {
		super();
	}

	public boolean isOnFire() {
		return FakeMetadata.getBitmaskValue(this.onFire, (byte) 0);
	}

	public void setOnFire(boolean onFire) {
		FakeMetadata.setBitmaskValue(this.onFire, (byte) 0, onFire);
	}
}
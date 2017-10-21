package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;

public class FakeBlazeMetadata extends FakeAmbientMetadata {
	private MetadataObject<Byte> onFire = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityBlaze", "c", 0); //12

	public FakeBlazeMetadata() {
		super();
	}

	public boolean isOnFire() {
		return MetadataObject.getBitmaskValue(this.onFire, (byte) 0);
	}

	public void setOnFire(boolean onFire) {
		MetadataObject.setBitmaskValue(this.onFire, (byte) 0, onFire);
	}
}
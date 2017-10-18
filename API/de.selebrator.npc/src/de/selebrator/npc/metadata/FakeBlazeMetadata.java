package de.selebrator.npc.metadata;

import de.selebrator.npc.entity.FakeEntity;

public class FakeBlazeMetadata extends FakeAmbientMetadata {
	private MetadataObject<Byte> onFire = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityBlaze", "c", 0); //12

	public FakeBlazeMetadata() {
		super();
	}

	public boolean isOnFire() {
		return FakeEntity.getBitmaskValue(this.onFire, (byte) 0);
	}

	public void setOnFire(boolean onFire) {
		FakeEntity.setBitmaskValue(this.onFire, (byte) 0, onFire);
	}
}
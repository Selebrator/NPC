package de.selebrator.npc.metadata;

import de.selebrator.npc.entity.FakeEntity;

public class FakeBatMetadata extends FakeAmbientMetadata {
	private MetadataObject<Byte> hanging = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityBat", "a", 0); //12

	public FakeBatMetadata() {
		super();
	}

	public boolean isHanging() {
		return FakeEntity.getBitmaskValue(this.hanging, (byte) 0);
	}

	public void setHanging(boolean hanging) {
		FakeEntity.setBitmaskValue(this.hanging, (byte) 0, hanging);
	}
}

package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;

public class FakeBatMetadata extends FakeAmbientMetadata {
	private MetadataObject<Byte> hanging = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityBat", "a", 0); //12

	public FakeBatMetadata() {
		super();
	}

	public boolean isHanging() {
		return MetadataObject.getBitmaskValue(this.hanging, (byte) 0);
	}

	public void setHanging(boolean hanging) {
		MetadataObject.setBitmaskValue(this.hanging, (byte) 0, hanging);
	}
}

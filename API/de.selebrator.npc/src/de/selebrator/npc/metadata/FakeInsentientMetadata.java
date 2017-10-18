package de.selebrator.npc.metadata;

import de.selebrator.npc.entity.FakeEntity;

public class FakeInsentientMetadata extends FakeLivingMetadata {
	private MetadataObject<Byte> insentientInfo = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityInsentient", "a", 0); //11

	public boolean hasAI() {
		return FakeEntity.getBitmaskValue(this.insentientInfo, (byte) 0);
	}

	public void setAI(boolean ai) {
		FakeEntity.setBitmaskValue(this.insentientInfo, (byte) 0, ai);
	}

	public boolean isLeftHanded() {
		return FakeEntity.getBitmaskValue(this.insentientInfo, (byte) 1);
	}

	public void setLeftHanded(boolean leftHanded) {
		FakeEntity.setBitmaskValue(this.insentientInfo, (byte) 1, leftHanded);
	}
}


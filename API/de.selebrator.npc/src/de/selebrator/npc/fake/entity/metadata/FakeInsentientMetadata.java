package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;

public class FakeInsentientMetadata extends FakeLivingMetadata {
	private MetadataObject<Byte> insentientInfo = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityInsentient", "a", 0); //11

	public boolean hasAI() {
		return MetadataObject.getBitmaskValue(this.insentientInfo, (byte) 0);
	}

	public void setAI(boolean ai) {
		MetadataObject.setBitmaskValue(this.insentientInfo, (byte) 0, ai);
	}

	public boolean isLeftHanded() {
		return MetadataObject.getBitmaskValue(this.insentientInfo, (byte) 1);
	}

	public void setLeftHanded(boolean leftHanded) {
		MetadataObject.setBitmaskValue(this.insentientInfo, (byte) 1, leftHanded);
	}
}


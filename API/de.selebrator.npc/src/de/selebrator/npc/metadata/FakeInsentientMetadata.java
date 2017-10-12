package de.selebrator.npc.metadata;

public class FakeInsentientMetadata extends FakeLivingMetadata {
	private MetadataObject<Byte> insentientInfo = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityInsentient", "a", 0); //11

	public boolean hasAI() {
		return FakeMetadata.getBitmaskValue(this.insentientInfo, (byte) 0);
	}

	public void setAI(boolean ai) {
		FakeMetadata.setBitmaskValue(this.insentientInfo, (byte) 0, ai);
	}

	public boolean isLeftHanded() {
		return FakeMetadata.getBitmaskValue(this.insentientInfo, (byte) 1);
	}

	public void setLeftHanded(boolean leftHanded) {
		FakeMetadata.setBitmaskValue(this.insentientInfo, (byte) 1, leftHanded);
	}
}


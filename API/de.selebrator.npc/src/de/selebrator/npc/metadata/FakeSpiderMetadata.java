package de.selebrator.npc.metadata;

public class FakeSpiderMetadata extends FakeMonsterMetadata {
	private MetadataObject<Byte> climbing = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntitySpider", "a", 0); //12

	public FakeSpiderMetadata() {
		super();
	}

	public boolean isClimbing() {
		return FakeMetadata.getBitmaskValue(this.climbing, (byte) 0);
	}

	public void setClimbing(boolean climbing) {
		FakeMetadata.setBitmaskValue(this.climbing, (byte) 0, climbing);
	}
}

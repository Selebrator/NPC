package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;

public class FakeSpiderMetadata extends FakeMonsterMetadata {
	private MetadataObject<Byte> climbing = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntitySpider", "a", 0); //12

	public FakeSpiderMetadata() {
		super();
	}

	public boolean isClimbing() {
		return MetadataObject.getBitmaskValue(this.climbing, (byte) 0);
	}

	public void setClimbing(boolean climbing) {
		MetadataObject.setBitmaskValue(this.climbing, (byte) 0, climbing);
	}
}

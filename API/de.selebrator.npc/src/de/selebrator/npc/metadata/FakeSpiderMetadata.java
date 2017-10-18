package de.selebrator.npc.metadata;

import de.selebrator.npc.entity.FakeEntity;

public class FakeSpiderMetadata extends FakeMonsterMetadata {
	private MetadataObject<Byte> climbing = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntitySpider", "a", 0); //12

	public FakeSpiderMetadata() {
		super();
	}

	public boolean isClimbing() {
		return FakeEntity.getBitmaskValue(this.climbing, (byte) 0);
	}

	public void setClimbing(boolean climbing) {
		FakeEntity.setBitmaskValue(this.climbing, (byte) 0, climbing);
	}
}

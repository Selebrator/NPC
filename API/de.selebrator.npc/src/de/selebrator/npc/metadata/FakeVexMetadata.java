package de.selebrator.npc.metadata;

import de.selebrator.npc.entity.FakeEntity;

public class FakeVexMetadata extends FakeMonsterMetadata {
	private MetadataObject<Byte> attacking = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityVex", "a", 0); //12

	public FakeVexMetadata() {
		super();
	}

	public boolean isAttacking() {
		return FakeEntity.getBitmaskValue(this.attacking, (byte) 0);
	}

	public void setAttacking(boolean attacking) {
		FakeEntity.setBitmaskValue(this.attacking, (byte) 0, attacking);
	}
}

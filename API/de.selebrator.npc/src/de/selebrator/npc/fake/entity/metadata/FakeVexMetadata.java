package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;

public class FakeVexMetadata extends FakeMonsterMetadata {
	private MetadataObject<Byte> attacking = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityVex", "a", 0); //12

	public FakeVexMetadata() {
		super();
	}

	public boolean isAttacking() {
		return MetadataObject.getBitmaskValue(this.attacking, (byte) 0);
	}

	public void setAttacking(boolean attacking) {
		MetadataObject.setBitmaskValue(this.attacking, (byte) 0, attacking);
	}
}

package de.selebrator.npc.metadata;

public class FakeVexMetadata extends FakeMonsterMetadata {
	private MetadataObject<Byte> attacking = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityVex", "a", 0); //12

	public FakeVexMetadata() {
		super();
	}

	public boolean isAttacking() {
		return FakeMetadata.getBitmaskValue(this.attacking, (byte) 0);
	}

	public void setAttacking(boolean attacking) {
		FakeMetadata.setBitmaskValue(this.attacking, (byte) 0, attacking);
	}
}

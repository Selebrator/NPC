package de.selebrator.npc.metadata;

public class FakeAbstractSkeletonMetadata extends FakeMonsterMetadata {
	private MetadataObject<Boolean> swingingArms = new MetadataObject<>(this.getDataWatcher(), false, "EntitySkeletonAbstract", "a", 0); //12

	FakeAbstractSkeletonMetadata() {
		super();
	}

	public boolean isSwingingArms() {
		return this.swingingArms.get();
	}

	public void setSwingingArms(boolean swingingArms) {
		this.swingingArms.set(swingingArms);
	}
}

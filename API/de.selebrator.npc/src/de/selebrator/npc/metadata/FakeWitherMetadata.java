package de.selebrator.npc.metadata;

public class FakeWitherMetadata extends FakeMonsterMetadata {
	private MetadataObject<Integer> centerTargetEID = new MetadataObject<>(this.getDataWatcher(), 0, "EntityWither", "a", 0); //12
	private MetadataObject<Integer> leftTargetEID = new MetadataObject<>(this.getDataWatcher(), 0, "EntityWither", "b", 0); //13
	private MetadataObject<Integer> rightTargetEID = new MetadataObject<>(this.getDataWatcher(), 0, "EntityWither", "c", 0); //14
	private MetadataObject<Integer> invulnerableTime = new MetadataObject<>(this.getDataWatcher(), 0, "EntityWither", "by", 0); //15

	public FakeWitherMetadata() {
		super();
	}

	public int getCenterTargetEID() {
		return this.centerTargetEID.get();
	}

	public void setCenterTargetEID(int entityId) {
		this.centerTargetEID.set(entityId);
	}

	public int getLeftTargetEID() {
		return this.leftTargetEID.get();
	}

	public void setLeftTargetEID(int entityId) {
		this.leftTargetEID.set(entityId);
	}

	public int getRightTargetEID() {
		return this.rightTargetEID.get();
	}

	public void setRightTargetEID(int entityId) {
		this.rightTargetEID.set(entityId);
	}

	public int getInvulnerableTime() {
		return this.invulnerableTime.get();
	}

	public void setInvulnerableTime(int time) {
		this.invulnerableTime.set(time);
	}
}

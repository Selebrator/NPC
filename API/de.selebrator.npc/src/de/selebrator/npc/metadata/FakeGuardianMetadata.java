package de.selebrator.npc.metadata;

import org.bukkit.entity.LivingEntity;

public class FakeGuardianMetadata extends FakeMonsterMetadata {
	private MetadataObject<Boolean> retractingSpikes = new MetadataObject<>(this.getDataWatcher(), false, "EntityGuardian", "bA", 0); //12
	private MetadataObject<Integer> targetEID = new MetadataObject<>(this.getDataWatcher(), 0, "EntityGuardian", "bB", 0); //13

	public FakeGuardianMetadata() {
		super();
	}

	public boolean isRetractingSpikes() {
		return this.retractingSpikes.get();
	}

	public void setRetractingSpikes(boolean retractingSpikes) {
		this.retractingSpikes.set(retractingSpikes);
	}

	public boolean hasTarget() {
		return !(this.getTargetEID() == 0);
	}

	public int getTargetEID() {
		return this.targetEID.get();
	}

	public void setTargetEID(int entityId) {
		this.targetEID.set(entityId);
	}

	public void setTarget(LivingEntity target) {
		this.setTargetEID(target.getEntityId());
	}
}

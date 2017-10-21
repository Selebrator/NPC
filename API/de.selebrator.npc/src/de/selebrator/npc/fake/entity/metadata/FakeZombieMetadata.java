package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;

public class FakeZombieMetadata extends FakeMonsterMetadata {
	private MetadataObject<Boolean> baby = new MetadataObject<>(this.getDataWatcher(), false, "EntityZombie", "bx", 0); //12
	private MetadataObject<Boolean> handsUp = new MetadataObject<>(this.getDataWatcher(), false, "EntityZombie", "bz", 2); //14

	public FakeZombieMetadata() {
		super();
	}

	public boolean isBaby() {
		return this.baby.get();
	}

	public void setBaby(boolean baby) {
		this.baby.set(baby);
	}

	public boolean areHandsUp() {
		return this.handsUp.get();
	}

	public void setHandsUp(boolean handsUp) {
		this.handsUp.set(handsUp);
	}
}

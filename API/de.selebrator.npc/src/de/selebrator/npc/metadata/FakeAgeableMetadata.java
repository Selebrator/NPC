package de.selebrator.npc.metadata;

public class FakeAgeableMetadata extends FakeCreatureMetadata {
	private MetadataObject<Boolean> baby = new MetadataObject<>(this.getDataWatcher(), false, "EntityAgeable", "bx", 0); //12

	public FakeAgeableMetadata() {
		super();
	}

	public boolean isBaby() {
		return this.baby.get();
	}

	public void setBaby(boolean baby) {
		this.baby.set(baby);
	}
}

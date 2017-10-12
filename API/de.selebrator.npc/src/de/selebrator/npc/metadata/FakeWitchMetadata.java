package de.selebrator.npc.metadata;

public class FakeWitchMetadata extends FakeMonsterMetadata {
	private MetadataObject<Boolean> drinking = new MetadataObject<>(this.getDataWatcher(), false, "EntityWitch", "c", 0); //12

	public FakeWitchMetadata() {
		super();
	}

	public boolean isDrinking() {
		return this.drinking.get();
	}

	public void setDrinking(boolean drinking) {
		this.drinking.set(drinking);
	}
}

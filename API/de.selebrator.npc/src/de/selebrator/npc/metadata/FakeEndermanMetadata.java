package de.selebrator.npc.metadata;

public class FakeEndermanMetadata extends FakeMonsterMetadata {
	//TODO add carried block
	private MetadataObject<Boolean> screaming = new MetadataObject<>(this.getDataWatcher(), false, "EntityEnderman", "by", 1); //13

	public FakeEndermanMetadata() {
		super();
	}

	public boolean isScreaming() {
		return this.screaming.get();
	}

	public void setScreaming(boolean screaming) {
		this.screaming.set(screaming);
	}
}

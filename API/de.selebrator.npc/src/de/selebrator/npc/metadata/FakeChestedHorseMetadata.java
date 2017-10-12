package de.selebrator.npc.metadata;

public class FakeChestedHorseMetadata extends FakeAbstractHorseMetadata {
	private MetadataObject<Boolean> chest = new MetadataObject<>(this.getDataWatcher(), false, "EntityHorseChestedAbstract", "bH", 0); //15

	public FakeChestedHorseMetadata() {
		super();
	}

	public boolean hasChest() {
		return this.chest.get();
	}

	public void setChest(boolean chest) {
		this.chest.set(chest);
	}
}

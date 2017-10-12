package de.selebrator.npc.metadata;

public class FakeIronGolemMetadata extends FakeGolemMetadata {
	private MetadataObject<Byte> playerCreated = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityIronGolem", "a", 0); //12

	public FakeIronGolemMetadata() {
		super();
	}

	public boolean isPlayerCreated() {
		return FakeMetadata.getBitmaskValue(this.playerCreated, (byte) 4);
	}

	public void setPlayerCreated(boolean playerCreated) {
		FakeMetadata.setBitmaskValue(this.playerCreated, (byte) 4, playerCreated);
	}
}

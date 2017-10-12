package de.selebrator.npc.metadata;

public class FakeGhastMetadata extends FakeFlyingMetadata {
	private MetadataObject<Boolean> attacking = new MetadataObject<>(this.getDataWatcher(), false, "EntityGhast", "a", 0); //12

	public FakeGhastMetadata() {
		super();
	}

	public boolean isAttracking() {
		return this.attacking.get();
	}

	public void setAttacking(boolean attacking) {
		this.attacking.set(attacking);
	}
}

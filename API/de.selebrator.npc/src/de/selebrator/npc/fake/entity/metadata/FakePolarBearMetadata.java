package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;

public class FakePolarBearMetadata extends FakeAnimalMetadata {
	private MetadataObject<Boolean> standing = new MetadataObject<>(this.getDataWatcher(), false, "EntityPolarBear", "bx", 0); //13

	public FakePolarBearMetadata() {
		super();
	}

	public boolean isStanding() {
		return this.standing.get();
	}

	public void setStanding(boolean standing) {
		this.standing.set(standing);
	}
}

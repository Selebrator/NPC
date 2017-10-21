package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;

public class FakeSlimeMetadata extends FakeInsentientMetadata {
	private MetadataObject<Integer> size = new MetadataObject<>(this.getDataWatcher(), 1, "EntitySlime", "bv", 0); //12

	public FakeSlimeMetadata() {
		super();
	}

	public int getSize() {
		return this.size.get();
	}

	public void setSize(int size) {
		this.size.set(size);
	}
}

package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;

public class FakeCreeperMetadata extends FakeMonsterMetadata {
	private MetadataObject<Integer> state = new MetadataObject<>(this.getDataWatcher(), -1, "EntityCreeper", "a", 0); //12
	private MetadataObject<Boolean> charged = new MetadataObject<>(this.getDataWatcher(), false, "EntityCreeper", "b", 1); //13
	private MetadataObject<Boolean> ignited = new MetadataObject<>(this.getDataWatcher(), false, "EntityCreeper", "c", 2); //14

	public FakeCreeperMetadata() {
		super();
	}

	public int getState() {
		return this.state.get();
	}

	public void setState(int state) {
		this.state.set(state);
	}

	public boolean isCharged() {
		return this.charged.get();
	}

	public void setCharged(boolean charged) {
		this.charged.set(charged);
	}

	public boolean idIgnited() {
		return this.ignited.get();
	}

	public void setIgnited(boolean ignited) {
		this.ignited.set(ignited);
	}
}

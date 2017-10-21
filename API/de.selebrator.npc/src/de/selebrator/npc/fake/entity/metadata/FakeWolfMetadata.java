package de.selebrator.npc.fake.entity.metadata;

import de.selebrator.npc.entity.metadata.MetadataObject;

public class FakeWolfMetadata extends FakeTameableAnimalMetadata {
	private MetadataObject<Float> tailHealth = new MetadataObject<>(this.getDataWatcher(), 20.0f, "EntityWolf", "DATA_HEALTH", 0); //15
	private MetadataObject<Boolean> begging = new MetadataObject<>(this.getDataWatcher(), false, "EntityWolf", "bC", 0); //16
	private MetadataObject<Integer> color = new MetadataObject<>(this.getDataWatcher(), 14, "EntityWolf", "bD", 0); //17

	public FakeWolfMetadata() {
		super();
	}

	public float getTailHealth() {
		return this.tailHealth.get();
	}

	public void setTailHealth(float health) {
		this.tailHealth.set(health);
	}

	public boolean isBegging() {
		return this.begging.get();
	}

	public void setBegging(boolean begging) {
		this.begging.set(begging);
	}

	public int getColor() {
		return this.color.get();
	}

	public void setColor(int color) {
		this.color.set(color);
	}
}

package de.selebrator.npc.metadata;

public class FakePigMetadata extends FakeAnimalMetadata {
	private MetadataObject<Boolean> saddle = new MetadataObject<>(this.getDataWatcher(), false, "EntityPig", "bx", 0); //13
	private MetadataObject<Integer> boostTime = new MetadataObject<>(this.getDataWatcher(), 0, "EntityPig", "by", 1); // 14

	public FakePigMetadata() {
		super();
	}

	public boolean hasSaddle() {
		return this.saddle.get();
	}

	public void setSaddle(boolean saddle) {
		this.saddle.set(saddle);
	}

	public int getBoostTime() {
		return this.boostTime.get();
	}

	public void setBoostTime(int time) {
		this.boostTime.set(time);
	}
}

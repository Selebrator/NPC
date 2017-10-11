package de.selebrator.npc.metadata;

public class FakeLivingMetadata extends FakeEntityMetadata {
	private MetadataObject<Byte> activeHand = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityLiving", "at", 0); //6
	private MetadataObject<Float> health = new MetadataObject<>(this.getDataWatcher(), 1.0f, "EntityLiving", "HEALTH", 1); //7
	private MetadataObject<Integer> potionColor = new MetadataObject<>(this.getDataWatcher(), 0, "EntityLiving", "g", 2); //8
	private MetadataObject<Boolean> potionAmbient = new MetadataObject<>(this.getDataWatcher(), false, "EntityLiving", "h", 3); //9
	private MetadataObject<Integer> arrows = new MetadataObject<>(this.getDataWatcher(), 0, "EntityLiving", "br", 4); //10

	public FakeLivingMetadata() {
		super();
	}

	public float getHealth() {
		return this.health.get();
	}

	public void setHealth(float health) {
		this.health.set(health);
	}

	public int getPotionColor() {
		return this.potionColor.get();
	}

	public void setPotionColor(int potionColor) {
		this.potionColor.set(potionColor);
	}

	public boolean isPotionAmbient() {
		return this.potionAmbient.get();
	}

	public void setPotionAmbient(boolean potionAmbient) {
		this.potionAmbient.set(potionAmbient);
	}

	public int getArrows() {
		return this.arrows.get();
	}

	public void setArrows(int arrows) {
		this.arrows.set(arrows);
	}
}

package de.selebrator.npc.metadata;

public class FakeShulkerMetadata extends FakeGolemMetadata {
	//TODO add facing direction
	//TODO add attachment position
	private MetadataObject<Byte> shieldHeight = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntityShulker", "c", 2); //14
	private MetadataObject<Byte> color = new MetadataObject<>(this.getDataWatcher(), (byte) 10, "EntityShulker", "COLOR", 3); //15

	public FakeShulkerMetadata() {
		super();
	}

	public byte getShildHeight() {
		return this.shieldHeight.get();
	}

	public void setShieldHeight(byte height) {
		this.shieldHeight.set(height);
	}

	public byte getColor() {
		return this.color.get();
	}

	public void setColor(byte color) {
		this.color.set(color);
	}
}

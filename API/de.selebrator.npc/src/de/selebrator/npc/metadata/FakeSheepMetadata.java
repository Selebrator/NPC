package de.selebrator.npc.metadata;

import de.selebrator.npc.entity.FakeEntity;

public class FakeSheepMetadata extends FakeAnimalMetadata {
	private MetadataObject<Byte> wool = new MetadataObject<>(this.getDataWatcher(), (byte) 0, "EntitySheep", "bx", 0); //13

	public FakeSheepMetadata() {
		super();
	}

	public int getColor() {
		return this.wool.get() & 0x0f;
	}

	public void setColor(int color) {
		this.wool.set((byte) (this.wool.get() | (color & 0x0f)));
	}

	public boolean isSheared() {
		return FakeEntity.getBitmaskValue(this.wool, (byte) 4);
	}

	public void setSheared(boolean sheared) {
		FakeEntity.setBitmaskValue(this.wool, (byte) 4, sheared);
	}
}

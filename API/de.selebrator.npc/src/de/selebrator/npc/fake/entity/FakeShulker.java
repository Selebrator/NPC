package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.ShulkerNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;

import static de.selebrator.npc.fake.Imports.FIELD_EntityShulker_COLOR;
import static de.selebrator.npc.fake.Imports.FIELD_EntityShulker_c;

public class FakeShulker extends FakeGolem implements ShulkerNPC {

	MetadataObject<Byte> shieldHeight;
	MetadataObject<Byte> color;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.shieldHeight = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityShulker_c, (byte) 0); //14
		this.color = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityShulker_COLOR, (byte) 10); //15
	}

	public byte getShieldHeight() {
		return this.shieldHeight.get();
	}

	public void setShieldHeight(byte height) {
		this.shieldHeight.set(height);
	}

	public byte getColorId() {
		return this.color.get();
	}

	public void setColorId(byte color) {
		this.color.set(color);
	}
}

package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.SnowmanNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;

import static de.selebrator.npc.fake.Imports.FIELD_EntitySnowman_a;

public class FakeSnowman extends FakeGolem implements SnowmanNPC {

	MetadataObject<Byte> pumpkinHat;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.pumpkinHat = new MetadataObject<>(this.getDataWatcher(), FIELD_EntitySnowman_a, (byte) 0x10); //12
	}

	@Override
	public byte getSnowmanInfo() {
		return this.pumpkinHat.get();
	}

	@Override
	public void setSnowmanInfo(byte value) {
		this.pumpkinHat.set(value);
	}
}

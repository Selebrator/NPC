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

	public boolean hasPumpkinHat() {
		return MetadataObject.getBitmaskValue(this.pumpkinHat, (byte) 4);
	}

	public void setPumpkinHat(boolean hat) {
		MetadataObject.setBitmaskValue(this.pumpkinHat, (byte) 4, hat);
	}
}

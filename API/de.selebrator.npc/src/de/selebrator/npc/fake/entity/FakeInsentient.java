package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.InsentientNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;

import static de.selebrator.npc.fake.Imports.FIELD_EntityInsentient_a;

public abstract class FakeInsentient extends FakeLiving implements InsentientNPC {

	MetadataObject<Byte> insentientInfo;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.insentientInfo = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityInsentient_a, (byte) 0); //11
	}

	public byte getInsentientInfo() {
		return this.insentientInfo.get();
	}

	public void setInsentientInfo(byte value) {
		this.insentientInfo.set(value);
	}
}

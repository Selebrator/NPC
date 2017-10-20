package de.selebrator.npc.entity;

import de.selebrator.npc.InsentientNPC;
import de.selebrator.npc.metadata.MetadataObject;

import static de.selebrator.npc.Imports.FIELD_EntityInsentient_a;

public abstract class FakeInsentient extends FakeLiving implements InsentientNPC {

	MetadataObject<Byte> insentientInfo;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.insentientInfo = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityInsentient_a, (byte) 0); //11
	}

	public boolean hasAI() {
		return FakeEntity.getBitmaskValue(this.insentientInfo, (byte) 0);
	}

	public void setAI(boolean ai) {
		FakeEntity.setBitmaskValue(this.insentientInfo, (byte) 0, ai);
	}

	public boolean isLeftHanded() {
		return FakeEntity.getBitmaskValue(this.insentientInfo, (byte) 1);
	}

	public void setLeftHanded(boolean leftHanded) {
		FakeEntity.setBitmaskValue(this.insentientInfo, (byte) 1, leftHanded);
	}
}

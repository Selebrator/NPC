package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.BatNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityBat_a;

public class FakeBat extends FakeAmbient implements BatNPC {

	MetadataObject<Byte> hanging;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.hanging = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityBat_a, (byte) 0); //12
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(6.0D);
	}

	public byte getBatInfo() {
		return this.hanging.get();
	}

	public void setBatInfo(byte value) {
		this.hanging.set(value);
	}
}

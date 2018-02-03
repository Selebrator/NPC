package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.VexNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityVex_a;

public class FakeVex extends FakeMonster implements VexNPC {

	MetadataObject<Byte> attacking;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.attacking = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityVex_a, (byte) 0); //12
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(14.0D);
		this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(4.0D);
	}

	@Override
	public byte getVexInfo() {
		return this.attacking.get();
	}

	@Override
	public void setVexInfo(byte value) {
		this.attacking.set(value);
	}
}

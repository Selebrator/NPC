package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.BlazeNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityBlaze_c;

public class FakeBlaze extends FakeMonster implements BlazeNPC {

	MetadataObject<Byte> onFire;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.onFire = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityBlaze_c, (byte) 0); //12
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(6.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		this.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(48.0D);
	}

	@Override
	public byte getBlazeInfo() {
		return this.onFire.get();
	}

	@Override
	public void setBlazeInfo(byte value) {
		this.onFire.set(value);
	}
}

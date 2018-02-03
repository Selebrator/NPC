package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.SheepNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntitySheep_bx;

public class FakeSheep extends FakeAnimal implements SheepNPC {

	MetadataObject<Byte> wool;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.wool = new MetadataObject<>(this.getDataWatcher(), FIELD_EntitySheep_bx, (byte) 0); //13
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(8.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
	}

	public byte getWoolInfo() {
		return this.wool.get();
	}

	public void setWoolInfo(byte value) {
		this.wool.set(value);
	}
}

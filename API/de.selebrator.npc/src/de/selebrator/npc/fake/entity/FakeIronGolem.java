package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.IronGolemNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityIronGolem_a;

public class FakeIronGolem extends FakeGolem implements IronGolemNPC {

	MetadataObject<Byte> playerCreated;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.playerCreated = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityIronGolem_a, (byte) 0); //12
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	}

	public byte getIronGolemInfo() {
		return this.playerCreated.get();
	}

	public void setIronGolemInfo(byte value) {
		this.playerCreated.set(value);
	}
}

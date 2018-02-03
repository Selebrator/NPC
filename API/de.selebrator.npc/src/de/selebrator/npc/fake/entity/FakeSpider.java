package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.SpiderNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntitySpider_a;

public class FakeSpider extends FakeMonster implements SpiderNPC {

	MetadataObject<Byte> climbing;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.climbing = new MetadataObject<>(this.getDataWatcher(), FIELD_EntitySpider_a, (byte) 0); //12
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(16.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
	}

	@Override
	public byte getSpiderInfo() {
		return this.climbing.get();
	}

	@Override
	public void setSpiderInfo(byte value) {
		this.climbing.set(value);
	}
}

package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.OcelotNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityOcelot_bB;

public class FakeOcelot extends FakeTameable implements OcelotNPC {

	MetadataObject<Integer> variant;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.variant = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityOcelot_bB, 0); //15
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
	}

	public int getVariantId() {
		return this.variant.get();
	}

	public void setVariant(int id) {
		this.variant.set(id);
	}
}

package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.ParrotNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityParrot_bG;

public class FakeParrot extends FakeTameable implements ParrotNPC {

	MetadataObject<Integer> variant;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.variant = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityParrot_bG, 0); //15
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.addAttribute(Attribute.GENERIC_FLYING_SPEED);
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(6.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
	}

	public int getVariantId() {
		return this.variant.get();
	}

	public void setVariant(int id) {
		this.variant.set(id);
	}
}

package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.RabbitNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityRabbit_bx;

public class FakeRabbit extends FakeAnimal implements RabbitNPC {

	MetadataObject<Integer> variant;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.variant = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityRabbit_bx, 0); // 13
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(3.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
	}

	@Override
	public int getVariantId() {
		return this.variant.get();
	}

	@Override
	public void setVariant(int id) {
		this.variant.set(id);
	}
}

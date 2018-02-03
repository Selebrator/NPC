package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.SlimeNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntitySlime_bv;

public class FakeSlime extends FakeInsentient implements SlimeNPC {

	private MetadataObject<Integer> size;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.size = new MetadataObject<>(this.getDataWatcher(), FIELD_EntitySlime_bv, 1); //12
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3F);
	}

	public int getSize() {
		return this.size.get();
	}

	public void setSize(int size) {
		this.size.set(size);
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(size * size);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2F + 0.1F * size);
	}
}

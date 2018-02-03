package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.WitchNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityWitch_c;

public class FakeWitch extends FakeMonster implements WitchNPC {

	MetadataObject<Boolean> drinking;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.drinking = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityWitch_c, false); //12
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(26.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
	}

	public boolean isDrinking() {
		return this.drinking.get();
	}

	public void setDrinking(boolean drinking) {
		this.drinking.set(drinking);
	}
}

package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.metadata.*;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityHorseChestedAbstract_bH;

public abstract class FakeChestedHorse extends FakeAbstractHorse implements ChestedHorseMetadata {

	MetadataObject<Boolean> chest;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.chest = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityHorseChestedAbstract_bH, false); //15
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((double) this.getModifiedMaxHealth());
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.17499999701976776D);
		this.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(0.5D);
	}

	public boolean hasChest() {
		return this.chest.get();
	}

	public void setChest(boolean chest) {
		this.chest.set(chest);
	}
}

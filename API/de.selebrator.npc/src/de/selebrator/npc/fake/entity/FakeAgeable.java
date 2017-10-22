package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.AgeableNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;

import static de.selebrator.npc.fake.Imports.FIELD_EntityAgeable_bx;

public abstract class FakeAgeable extends FakeCreature implements AgeableNPC {

	MetadataObject<Boolean> baby;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.baby = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityAgeable_bx, false); //12
	}

	public boolean isBaby() {
		return this.baby.get();
	}

	public void setBaby(boolean baby) {
		this.baby.set(baby);
	}
}

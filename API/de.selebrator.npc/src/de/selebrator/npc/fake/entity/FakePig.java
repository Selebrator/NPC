package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.PigNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityPig_bx;
import static de.selebrator.npc.fake.Imports.FIELD_EntityPig_by;

public class FakePig extends FakeAnimal implements PigNPC {

	MetadataObject<Boolean> saddle;
	MetadataObject<Integer> boostTime;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.saddle = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityPig_bx, false); //13
		this.boostTime = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityPig_by, 0); //14
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
	}

	public boolean hasSaddle() {
		return this.saddle.get();
	}

	public void setSaddle(boolean saddle) {
		this.saddle.set(saddle);
	}

	public int getBoostTime() {
		return this.boostTime.get();
	}

	public void setBoostTime(int time) {
		this.boostTime.set(time);
	}
}

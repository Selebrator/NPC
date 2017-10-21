package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.ZombieNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.entity.EntityType;

import static de.selebrator.npc.fake.Imports.FIELD_EntityZombie_bx;
import static de.selebrator.npc.fake.Imports.FIELD_EntityZombie_bz;

public class FakeZombie extends FakeMonster implements ZombieNPC {

	MetadataObject<Boolean> baby;
	MetadataObject<Boolean> handsUp;

	@Override
	public EntityType getType() {
		return EntityType.ZOMBIE;
	}

	@Override
	void initMetadata() {
		super.initMetadata();
		this.baby = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityZombie_bx, false); //12
		this.handsUp = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityZombie_bz, false); //14
	}

	public boolean isBaby() {
		return this.baby.get();
	}

	public void setBaby(boolean baby) {
		this.baby.set(baby);
	}

	public boolean areHandsUp() {
		return this.handsUp.get();
	}

	public void setHandsUp(boolean handsUp) {
		this.handsUp.set(handsUp);
	}
}

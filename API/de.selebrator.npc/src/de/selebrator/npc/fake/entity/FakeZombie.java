package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.ZombieNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityZombie_bx;
import static de.selebrator.npc.fake.Imports.FIELD_EntityZombie_bz;

public class FakeZombie extends FakeMonster implements ZombieNPC {

	MetadataObject<Boolean> baby;
	MetadataObject<Boolean> handsUp;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.baby = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityZombie_bx, false); //12
		this.handsUp = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityZombie_bz, false); //14
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(35.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(3.0D);
		this.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(2.0D);
		this.addAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS, this.random.nextDouble() * 0.10000000149011612D);
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

package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.ZombieVillagerNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;

import static de.selebrator.npc.fake.Imports.FIELD_EntityZombieVillager_b;
import static de.selebrator.npc.fake.Imports.FIELD_EntityZombieVillager_c;

public class FakeZombieVillager extends FakeZombie implements ZombieVillagerNPC {

	MetadataObject<Boolean> converting;
	MetadataObject<Integer> profession;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.converting = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityZombieVillager_b, false); //15
		this.profession = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityZombieVillager_c, 0); //16
	}

	public boolean isConverting() {
		return this.converting.get();
	}

	public void setConverting(boolean converting) {
		this.converting.set(converting);
	}

	public int getProfessionId() {
		return this.profession.get();
	}

	public void setProfession(int id) {
		this.profession.set(id);
	}
}

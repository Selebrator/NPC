package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.VillagerNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityVillager_bz;

public class FakeVillager extends FakeAgeable implements VillagerNPC {

	MetadataObject<Integer> profession;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.profession = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityVillager_bz, 0); //13
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5D);
	}

	@Override
	public int getProfessionId() {
		return this.profession.get();
	}

	@Override
	public void setProfession(int id) {
		this.profession.set(id);
	}
}

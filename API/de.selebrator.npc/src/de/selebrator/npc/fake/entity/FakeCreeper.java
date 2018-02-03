package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.CreeperNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.*;

public class FakeCreeper extends FakeMonster implements CreeperNPC {

	MetadataObject<Integer> state;
	MetadataObject<Boolean> charged;
	MetadataObject<Boolean> ignited;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.state = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityCreeper_a, -1); //12
		this.charged = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityCreeper_b, false); //13
		this.ignited = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityCreeper_c, false); //14
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
	}

	public int getState() {
		return this.state.get();
	}

	public void setState(int state) {
		this.state.set(state);
	}

	public boolean isCharged() {
		return this.charged.get();
	}

	public void setCharged(boolean charged) {
		this.charged.set(charged);
	}

	public boolean idIgnited() {
		return this.ignited.get();
	}

	public void setIgnited(boolean ignited) {
		this.ignited.set(ignited);
	}
}

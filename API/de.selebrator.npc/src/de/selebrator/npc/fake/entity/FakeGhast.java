package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.GhastNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityGhast_a;

public class FakeGhast extends FakeFlying implements GhastNPC {

	MetadataObject<Boolean> attacking;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.attacking = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityGhast_a, false); //12
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10.0D);
		this.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100.0D);
	}

	public boolean isAttacking() {
		return this.attacking.get();
	}

	public void setAttacking(boolean attacking) {
		this.attacking.set(attacking);
	}
}

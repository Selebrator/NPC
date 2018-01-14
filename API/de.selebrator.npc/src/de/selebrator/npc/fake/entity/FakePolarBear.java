package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.PolarBearNPC;
import de.selebrator.npc.entity.metadata.MetadataObject;
import org.bukkit.attribute.Attribute;

import static de.selebrator.npc.fake.Imports.FIELD_EntityPolarBear_bx;

public class FakePolarBear extends FakeAnimal implements PolarBearNPC {

	MetadataObject<Boolean> standing;

	@Override
	void initMetadata() {
		super.initMetadata();
		this.standing = new MetadataObject<>(this.getDataWatcher(), FIELD_EntityPolarBear_bx, false); //13
	}

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(30.0D);
		this.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(20.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
		this.addAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 6.0D);
	}

	public boolean isStanding() {
		return this.standing.get();
	}

	public void setStanding(boolean standing) {
		this.standing.set(standing);
	}
}

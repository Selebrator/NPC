package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.GiantNPC;
import org.bukkit.attribute.Attribute;

public class FakeGiant extends FakeMonster implements GiantNPC {

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5D);
		this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(50.0D);
	}
}

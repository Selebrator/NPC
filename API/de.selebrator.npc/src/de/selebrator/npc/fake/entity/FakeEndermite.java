package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.EndermiteNPC;
import org.bukkit.attribute.Attribute;

public class FakeEndermite extends FakeMonster implements EndermiteNPC {

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(8.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2.0D);
	}
}

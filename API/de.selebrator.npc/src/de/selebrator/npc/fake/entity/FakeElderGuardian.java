package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.ElderGuardianNPC;
import org.bukkit.attribute.Attribute;

public class FakeElderGuardian extends FakeGuardian implements ElderGuardianNPC {

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
		this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(8.0D);
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(80.0D);
	}
}

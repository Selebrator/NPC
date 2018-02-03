package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.SilverfishNPC;
import org.bukkit.attribute.Attribute;

public class FakeSilverfish extends FakeMonster implements SilverfishNPC {

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(8.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1.0D);
	}
}

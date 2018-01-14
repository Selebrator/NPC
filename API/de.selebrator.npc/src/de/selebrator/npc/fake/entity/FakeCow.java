package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.CowNPC;
import org.bukkit.attribute.Attribute;

public class FakeCow extends FakeAnimal implements CowNPC {

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
	}
}

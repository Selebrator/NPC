package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.SquidNPC;
import org.bukkit.attribute.Attribute;

public class FakeSquid extends FakeWaterMob implements SquidNPC {

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10.0D);
	}
}

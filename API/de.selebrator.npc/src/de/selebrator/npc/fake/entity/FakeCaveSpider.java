package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.CaveSpiderNPC;
import org.bukkit.attribute.Attribute;

public class FakeCaveSpider extends FakeSpider implements CaveSpiderNPC {

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(12.0D);
	}
}

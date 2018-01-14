package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.ChickenNPC;
import org.bukkit.attribute.Attribute;

public class FakeChicken extends FakeAnimal implements ChickenNPC {

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(4.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25D);
	}
}

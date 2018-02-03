package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.MagmaCubeNPC;
import org.bukkit.attribute.Attribute;

public class FakeMagmaCube extends FakeSlime implements MagmaCubeNPC {

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
	}

	@Override
	public void setSize(int size) {
		super.setSize(size);
		this.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(size * 3);
	}
}

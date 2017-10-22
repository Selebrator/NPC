package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.SkeletonHorseNPC;
import org.bukkit.attribute.Attribute;

public class FakeSkeletonHorse extends FakeHorse implements SkeletonHorseNPC {

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(15.0D);
		this.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
		this.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
	}
}

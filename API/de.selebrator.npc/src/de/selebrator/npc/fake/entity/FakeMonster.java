package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.MonsterNPC;
import org.bukkit.attribute.Attribute;

public abstract class FakeMonster extends FakeCreature implements MonsterNPC {

	@Override
	void initAttributes() {
		super.initAttributes();
		this.addAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
	}
}

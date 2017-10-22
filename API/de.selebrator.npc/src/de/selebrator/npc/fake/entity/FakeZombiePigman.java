package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.ZombiePigmanNPC;
import org.bukkit.attribute.Attribute;

public class FakeZombiePigman extends FakeZombie implements ZombiePigmanNPC {

	@Override
	void initAttributes() {
		super.initAttributes();
		this.getAttribute(Attribute.ZOMBIE_SPAWN_REINFORCEMENTS).setBaseValue(0.0D);
		this.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(5.0D);
	}
}

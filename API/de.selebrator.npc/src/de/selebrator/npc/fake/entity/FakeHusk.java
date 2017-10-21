package de.selebrator.npc.fake.entity;

import de.selebrator.npc.entity.HuskNPC;
import org.bukkit.entity.EntityType;

public class FakeHusk extends FakeZombie implements HuskNPC {

	@Override
	public EntityType getType() {
		return EntityType.HUSK;
	}
}

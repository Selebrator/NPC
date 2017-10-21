package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.ZombieMetadata;
import org.bukkit.entity.EntityType;

public interface ZombieNPC extends MonsterNPC, ZombieMetadata {

	@Override
	default EntityType getType() {
		return EntityType.ZOMBIE;
	}
}

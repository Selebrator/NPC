package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.HuskMetadata;
import org.bukkit.entity.EntityType;

public interface HuskNPC extends ZombieNPC, HuskMetadata {

	@Override
	default EntityType getType() {
		return EntityType.HUSK;
	}
}

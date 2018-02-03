package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.GiantMetadata;
import org.bukkit.entity.EntityType;

public interface GiantNPC extends MonsterNPC, GiantMetadata {

	@Override
	default EntityType getType() {
		return EntityType.GIANT;
	}
}

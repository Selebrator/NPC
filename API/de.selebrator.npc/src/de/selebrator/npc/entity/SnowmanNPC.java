package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.SnowmanMetadata;
import org.bukkit.entity.EntityType;

public interface SnowmanNPC extends GolemNPC, SnowmanMetadata {

	@Override
	default EntityType getType() {
		return EntityType.SNOWMAN;
	}
}

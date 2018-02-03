package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.CaveSpiderMetadata;
import org.bukkit.entity.EntityType;

public interface CaveSpiderNPC extends SpiderNPC, CaveSpiderMetadata {

	@Override
	default EntityType getType() {
		return EntityType.CAVE_SPIDER;
	}
}

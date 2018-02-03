package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.SpiderMetadata;
import org.bukkit.entity.EntityType;

public interface SpiderNPC extends MonsterNPC, SpiderMetadata {

	@Override
	default EntityType getType() {
		return EntityType.SPIDER;
	}
}

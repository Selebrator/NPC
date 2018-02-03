package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.CreeperMetadata;
import org.bukkit.entity.EntityType;

public interface CreeperNPC extends MonsterNPC, CreeperMetadata {

	@Override
	default EntityType getType() {
		return EntityType.CREEPER;
	}
}

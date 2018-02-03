package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.SilverfishMetadata;
import org.bukkit.entity.EntityType;

public interface SilverfishNPC extends MonsterNPC, SilverfishMetadata {

	@Override
	default EntityType getType() {
		return EntityType.SILVERFISH;
	}
}

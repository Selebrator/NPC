package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.WitherMetadata;
import org.bukkit.entity.EntityType;

public interface WitherNPC extends MonsterNPC, WitherMetadata {

	@Override
	default EntityType getType() {
		return EntityType.WITHER;
	}
}

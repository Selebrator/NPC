package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.EndermanMetadata;
import org.bukkit.entity.EntityType;

public interface EndermanNPC extends MonsterNPC, EndermanMetadata {

	@Override
	default EntityType getType() {
		return EntityType.ENDERMAN;
	}
}

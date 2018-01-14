package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.CowMetadata;
import org.bukkit.entity.EntityType;

public interface CowNPC extends AnimalNPC, CowMetadata {

	@Override
	default EntityType getType() {
		return EntityType.COW;
	}
}

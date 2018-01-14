package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.PigMetadata;
import org.bukkit.entity.EntityType;

public interface PigNPC extends AnimalNPC, PigMetadata {

	@Override
	default EntityType getType() {
		return EntityType.PIG;
	}
}

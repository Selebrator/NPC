package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.ChickenMetadata;
import org.bukkit.entity.EntityType;

public interface ChickenNPC extends AnimalNPC, ChickenMetadata {

	@Override
	default EntityType getType() {
		return EntityType.CHICKEN;
	}
}

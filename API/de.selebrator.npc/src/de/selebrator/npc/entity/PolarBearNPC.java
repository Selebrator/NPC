package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.PolarBearMetadata;
import org.bukkit.entity.EntityType;

public interface PolarBearNPC extends AnimalNPC, PolarBearMetadata {

	@Override
	default EntityType getType() {
		return EntityType.POLAR_BEAR;
	}
}

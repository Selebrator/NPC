package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.HorseMetadata;
import org.bukkit.entity.EntityType;

public interface HorseNPC extends AbstractHorseNPC, HorseMetadata {

	@Override
	default EntityType getType() {
		return EntityType.HORSE;
	}
}

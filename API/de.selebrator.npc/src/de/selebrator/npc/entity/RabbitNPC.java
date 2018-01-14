package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.RabbitMetadata;
import org.bukkit.entity.EntityType;

public interface RabbitNPC extends AnimalNPC, RabbitMetadata {

	@Override
	default EntityType getType() {
		return EntityType.RABBIT;
	}
}

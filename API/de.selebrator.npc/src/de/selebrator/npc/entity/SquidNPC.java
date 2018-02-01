package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.SquidMetadata;
import org.bukkit.entity.EntityType;

public interface SquidNPC extends WaterMobNPC, SquidMetadata {

	@Override
	default EntityType getType() {
		return EntityType.SQUID;
	}
}

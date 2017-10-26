package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.OcelotMetadata;
import org.bukkit.entity.EntityType;

public interface OcelotNPC extends TameableNPC, OcelotMetadata {

	@Override
	default EntityType getType() {
		return EntityType.OCELOT;
	}
}

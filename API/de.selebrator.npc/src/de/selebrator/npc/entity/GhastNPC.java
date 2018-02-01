package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.GhastMetadata;
import org.bukkit.entity.EntityType;

public interface GhastNPC extends FlyingNPC, GhastMetadata {

	@Override
	default EntityType getType() {
		return EntityType.GHAST;
	}
}

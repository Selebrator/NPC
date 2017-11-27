package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.StrayMetadata;
import org.bukkit.entity.EntityType;

public interface StrayNPC extends AbstractSkeletonNPC, StrayMetadata {

	@Override
	default EntityType getType() {
		return EntityType.STRAY;
	}
}

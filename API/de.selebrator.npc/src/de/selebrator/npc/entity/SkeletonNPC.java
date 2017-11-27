package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.SkeletonMetadata;
import org.bukkit.entity.EntityType;

public interface SkeletonNPC extends AbstractSkeletonNPC, SkeletonMetadata {

	@Override
	default EntityType getType() {
		return EntityType.SKELETON;
	}
}

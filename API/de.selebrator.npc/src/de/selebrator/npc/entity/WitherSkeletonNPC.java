package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.WitherSkeletonMetadata;
import org.bukkit.entity.EntityType;

public interface WitherSkeletonNPC extends AbstractSkeletonNPC, WitherSkeletonMetadata {

	@Override
	default EntityType getType() {
		return EntityType.WITHER_SKELETON;
	}
}

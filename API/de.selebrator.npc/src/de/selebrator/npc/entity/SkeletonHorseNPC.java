package de.selebrator.npc.entity;

import de.selebrator.npc.entity.metadata.SkeletonHorseMetadata;
import org.bukkit.entity.EntityType;

public interface SkeletonHorseNPC extends HorseNPC, SkeletonHorseMetadata {

	@Override
	default EntityType getType() {
		return EntityType.SKELETON_HORSE;
	}
}
